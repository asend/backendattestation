package com.fonctionpublique.services.utilisateur;

import com.fonctionpublique.access.AuthenticationRequest;
import com.fonctionpublique.access.RegistrationRequest;
import com.fonctionpublique.access.password.PasswordResetToken;
import com.fonctionpublique.config.JwtService;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.entities.Profile;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.exception.*;
import com.fonctionpublique.handleException.EmailAlreadyExistException;
import com.fonctionpublique.handleException.NinAlreadyExistException;
import com.fonctionpublique.repository.PasswordResetTokenRepository;
import com.fonctionpublique.repository.ProfileRepository;
import com.fonctionpublique.repository.UtilisateurRepository;
import com.fonctionpublique.validators.ObjectValidator;
import com.google.zxing.WriterException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final JwtService utilisateurUtil;
    private final AuthenticationManager authenticationManager;
    private final ObjectValidator<RegistrationRequest> validator;
    private final PasswordResetTokenRepository resetTokenRepository;

    /**
     * find utilisateur by id
     *
     * @param id
     * @return
     */
    @Override
    public UtilisateurDTO getById(int id) {
        return convertDTO(utilisateurRepository.findById(id).orElse(null));
    }
    @Override
    public UtilisateurDTO getByNin(String nin) {
        return  convertToDTO(utilisateurRepository.findByNin(nin).orElse(null));
    }

    /**
     * Convert entity to dto
     *
     * @param utilisateur
     * @return
     */
    private UtilisateurDTO convertToDTO(Utilisateur utilisateur) {

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setId(utilisateur.getId());
        utilisateurDTO.setPrenom(utilisateur.getPrenom());
        utilisateurDTO.setNom(utilisateur.getNom());
        utilisateurDTO.setEmail(utilisateur.getEmail());
        utilisateurDTO.setNin(utilisateur.getNin());
        utilisateurDTO.setPassPort(utilisateur.getPassPort());
        return utilisateurDTO;
    }

    /**
     * Convert dto to entity
     *
     * @param utilisateur
     * @return
     */
    private Utilisateur convertToEntity(UtilisateurDTO utilisateur) {

        return Utilisateur.builder()
                .id(utilisateur.getId())
                .nom(utilisateur.getNom())
                .prenom(utilisateur.getPrenom())
                .nin(utilisateur.getNin())
                .email(utilisateur.getEmail())
                .statut(utilisateur.isStatut())
                .build();

    }

    /**
     * Convert entity to dto
     *
     * @param utilisateur
     * @return
     */
    public UtilisateurDTO convertDTO(Utilisateur utilisateur) {

        return UtilisateurDTO.builder()
                .id(utilisateur.getId())
                .prenom(utilisateur.getPrenom())
                .nom(utilisateur.getNom())
                .email(utilisateur.getEmail())
                .nin(utilisateur.getNin())
                .statut(utilisateur.isStatut())
                .fullName(utilisateur.getFullName())
//                .demandeurDTO(demandeurService.convertToDTO(utilisateur.getDemandeur()))
                .signature(utilisateur.getSignature())
                .passPort(utilisateur.getPassPort())
                .typePieces(utilisateur.getTypePieces())
                .build();
    }

    /**
     * List of users
     *
     * @return
     * @throws IOException
     * @throws WriterException
     */
    @Override
    public List<UtilisateurDTO> findAll() throws IOException, WriterException {
        return utilisateurRepository.findAll().stream().map(this::convertDTO).collect(Collectors.toList());
    }

    /**
     * find profil by profileName if exist else create it
     *
     * @param profileName
     * @return
     */
    private Profile findOrCreateProfile(String profileName) {
        Profile profile = profileRepository.findByCode(profileName).orElse(null);
        if (profile == null) {
            return profileRepository.save(
                    Profile.builder()
                            .libelle(" Role par default de l'utilisateur")
                            .code(profileName)
                            .etat("true")
                            .build());
        }
        return profile;
    }

    /**
     * register user
     *
     * @param registrationRequest
     * @return
     * @throws IOException
     * @throws WriterException
     */
    @Override
    public AuthenticationResponse registerUtilisateur(RegistrationRequest registrationRequest) throws IOException, WriterException {
        validator.validate(registrationRequest);
        if (utilisateurRepository.existsByEmail(registrationRequest.getEmail()) && utilisateurRepository.existsByNin(registrationRequest.getNin())) {
            throw new EmailAndNinAlreadyExistException("EMAIL_NIN_EXIST");
        }
        if (utilisateurRepository.existsByEmail(registrationRequest.getEmail())) {
            throw new EmailAlreadyExistException("email exist");
        }
        if (utilisateurRepository.existsByNin(registrationRequest.getNin())) {
            throw new NinAlreadyExistException("nin exist");
        }

        Utilisateur utilisateur = RegistrationRequest.convertToEntity(registrationRequest);
        utilisateur.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        utilisateur.setStatut(true);
        utilisateur.setProfile(findOrCreateProfile("user"));
        utilisateurRepository.save(utilisateur);
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", utilisateur.getId());
        claims.put("email", utilisateur.getEmail());
        claims.put("nin", utilisateur.getNin());
        claims.put("fullName", utilisateur.getFullName());
        claims.put("profile", utilisateur.getProfile().getCode());
        String token = utilisateurUtil.generateToken(utilisateur, claims);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
    }

    /**
     * Authentication
     *
     * @param registrationRequest
     * @return
     */
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest registrationRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(registrationRequest.getEmail(), registrationRequest.getPassword())
        );
        final Utilisateur user = utilisateurRepository.findByEmail(registrationRequest.getEmail()).get();
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.getId());
        claims.put("email", user.getEmail());
        claims.put("nin", user.getNin());
        claims.put("fullName", user.getPrenom() + " " + user.getNom());
        claims.put("profile", user.getProfile().getCode());
        String token = utilisateurUtil.generateToken(user, claims);

        return AuthenticationResponse.builder()
                .token(token)
                .build();


    }

    /**
     * changement de mot de passe
     *
     * @param email
     * @param oldPassword
     * @param newPassword
     * @param confirm
     */
    @Override
    public Integer changePassword(String email, String oldPassword, String newPassword, String confirm) {

        Optional<Utilisateur> utilisateur = utilisateurRepository.findByEmail(email);
        if (!utilisateur.isPresent()) {
            throw new EmailNotExistException("Email not exist");
        }

        if (!oldPasswordIsValid(utilisateur.get(), oldPassword)) {
            throw new NotMatchPasswordOldException("L'ancien mot de passe saisi n'est pas correct");
        }

        if (!newPassword.equals(confirm)) {
            throw new NotMatchPasswordException("Le nouveua mot de passe et la confirmation ne correspond pas");
        }

        utilisateur.get().setPassword(passwordEncoder.encode(newPassword));
        return utilisateurRepository.save(utilisateur.get()).getId();
    }

    /**
     * Verify if old password match the typing password
     *
     * @param utilisateur
     * @param oldPassword
     * @return
     */
    @Override
    public boolean oldPasswordIsValid(Utilisateur utilisateur, String oldPassword) {
        return passwordEncoder.matches(oldPassword, utilisateur.getPassword());
    }

    /**
     * find user by email
     *
     * @param email
     * @return
     */
    @Override
    public Optional<Utilisateur> findByEmail(String email) {
        return utilisateurRepository.findByEmail(email);
    }


    /**
     * Reset password
     *
     * @param token
     * @param newPassword
     * @param confirmPassword
     * @return UtilisateurDTO
     */
    @Override
    public Integer resetPassword(String token, String newPassword, String confirmPassword) {

        PasswordResetToken verification = resetTokenRepository.findByToken(token);
        if (verification == null) {
            throw new InvalidTokenException("Invalid token");
        }
        Utilisateur utilisateur = verification.getUtilisateur();
        Calendar calendar = Calendar.getInstance();
        System.out.println("expires " + verification.getTokenExpirationTime() + "current" + calendar.getTime());
        //if (verification.getTokenExpirationTime().getTime()-calendar.getTime().getTime()<=0){
        if (verification.getTokenExpirationTime().before(calendar.getTime())) {
            resetTokenRepository.delete(verification);
            throw new ExpirationTokenException("expired token");
        }
        if (!newPassword.equals(confirmPassword)) {
            throw new NotMatchPasswordException("not conform");
        }
        utilisateur.setPassword(passwordEncoder.encode(newPassword));
        return utilisateurRepository.save(utilisateur).getId();


    }


}






