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
import com.fonctionpublique.services.profile.ProfileServiceImpl;
import com.fonctionpublique.validators.ObjectValidator;
import com.google.zxing.WriterException;
import jakarta.persistence.EntityNotFoundException;
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
    private final ProfileServiceImpl profileService;

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


public Optional<Integer> findById(int id) {
    return utilisateurRepository.findById(id)
            .map(utilisateur -> utilisateur.getId()); // Return the ID of the user as an Optional<Integer>
}

    @Override
    public UtilisateurDTO getByNin(String nin) {
        return  convertDTO(utilisateurRepository.findByNin(nin).orElse(null));
    }


    /**
     * Convert entity to dto
     *
     * @param utilisateur
     * @return
     */
//    private UtilisateurDTO convertToDTO(Utilisateur utilisateur) {
//
//        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
//        utilisateurDTO.setId(utilisateur.getId());
//        utilisateurDTO.setPrenom(utilisateur.getPrenom());
//        utilisateurDTO.setNom(utilisateur.getNom());
//        utilisateurDTO.setEmail(utilisateur.getEmail());
//        utilisateurDTO.setNin(utilisateur.getNin());
//        utilisateurDTO.setTitre(utilisateurDTO.getTitre());
//        utilisateurDTO.setPassPort(utilisateur.getPassPort());
//        utilisateurDTO.setSignature(utilisateur.getSignature());
//        return utilisateurDTO;
//    }

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
                .signature(utilisateur.getSignature())
                .passPort(utilisateur.getPassPort())
                .typePieces(utilisateur.getTypePieces())
                .titre(utilisateur.getTitre())
                .signature(utilisateur.getSignature())
                .telephone(utilisateur.getTelephone())
                .profileDTO(profileService.convertDTO(utilisateur.getProfile()))
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
        utilisateur.setMatriculeSolde(null);
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
     * register a new administrator
     * @param registrationRequest
     * @return
     * @throws IOException
     * @throws WriterException
     */


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

        if(token != null){
            Optional<Profile> profileOpt = profileRepository.findByCode("traitant");
            if("traitant".equalsIgnoreCase(user.getProfile().getCode())){
                String fullName = user.getPrenom() + " " + user.getNom();
                String email = user.getEmail();

                String message = "Un traitant vient de se connecter !\nNom: " + fullName + "\nEmail: " + email;



                // Envoi du message WhatsApp
//                Constantes.sendNotificationWhatsapp(message, "782441635");
            }
        }

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

    @Override
    public List<UtilisateurDTO> getByCode(String code) {
        Profile profile = profileService.getByCodes(code);
        List<Utilisateur> utilisateurs = profile.getUtilisateur();
        if (utilisateurs.isEmpty()) {
            throw new IllegalArgumentException("Aucun utilisateur trouvé pour le code de profil : " + code);
        }
        return utilisateurs.stream()
                .map(this::convertDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Integer updateTraitant(int utilisateurId, UtilisateurDTO utilisateurDTO) {

        Utilisateur utilisateur = utilisateurRepository.findById(utilisateurId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur not found with ID: " + utilisateurId));

        utilisateur.setNom(utilisateurDTO.getNom());
        utilisateur.setPrenom(utilisateurDTO.getPrenom());
        utilisateur.setEmail(utilisateurDTO.getEmail());
        utilisateur.setTitre(utilisateurDTO.getTitre());
        utilisateur.setNin(utilisateurDTO.getNin());
        utilisateur.setTelephone(utilisateurDTO.getTelephone());
        utilisateur.setSignature(utilisateurDTO.getSignature());
        utilisateur = utilisateurRepository.save(utilisateur);
        return utilisateurRepository.save(utilisateur).getId();
    }



    @Override
    public Profile CreateProfileTraitant(String profileTraitant) {
        Profile profile = profileRepository.findByCode(profileTraitant).orElse(null);
        if (profile == null) {
            return profileRepository.save(
                    Profile.builder()
                            .libelle("role de traitant")
                            .code(profileTraitant)
                            .etat("true")
                            .build());
        }
        return profile;
    }

    @Override
    public int registerTraitant(RegistrationRequest registrationRequest){
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
        utilisateur.setProfile(CreateProfileTraitant("traitant"));
        utilisateurRepository.save(utilisateur);
        return utilisateur.getId();
    }


    public void deleteTraitant(int traitantId) {
        Optional<Utilisateur> utilisateurOpt = utilisateurRepository.findById(traitantId);
        if (utilisateurOpt.isEmpty()) {
            throw new EntityNotFoundException("Traitant with ID " + traitantId + " does not exist");
        }

        Utilisateur utilisateur = utilisateurOpt.get();

        if (!"traitant".equalsIgnoreCase(utilisateur.getProfile().getCode())) {
            throw new IllegalArgumentException("Only users with the 'traitant' profile can be deleted");
        }
        utilisateurRepository.delete(utilisateur);
    }



    @Override
    public Profile CreateProfileVisionnage(String profileTraitant) {
        Profile profile = profileRepository.findByCode(profileTraitant).orElse(null);
        if (profile == null) {
            return profileRepository.save(
                    Profile.builder()
                            .libelle("role de visionnaire")
                            .code(profileTraitant)
                            .etat("true")
                            .build());
        }
        return profile;
    }


    @Override
    public int registerVisionnage(RegistrationRequest registrationRequest){
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
        utilisateur.setProfile(CreateProfileVisionnage("visionnage"));
        utilisateurRepository.save(utilisateur);
        return utilisateur.getId();
    }




}






