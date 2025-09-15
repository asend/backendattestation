package com.fonctionpublique.services.interco;

import com.fonctionpublique.access.RegistrationRequest;
import com.fonctionpublique.config.JwtService;
import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.entities.*;
import com.fonctionpublique.enumpackage.Constantes;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.enumpackage.TypeDemande;
import com.fonctionpublique.exception.EmailAndNinAlreadyExistException;
import com.fonctionpublique.handleException.EmailAlreadyExistException;
import com.fonctionpublique.handleException.NinAlreadyExistException;
import com.fonctionpublique.repository.*;
import com.fonctionpublique.services.profile.ProfileServiceImpl;
import com.fonctionpublique.services.uploadFile.FileUploadServiceImpl;
import com.fonctionpublique.services.utilisateur.AuthenticationResponse;
import com.fonctionpublique.validators.ObjectValidator;
import com.google.zxing.WriterException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.fonctionpublique.access.RegistrationRequest.convertToEntity;

@RequiredArgsConstructor
@Service
public class IntercoSerciceImpl {


    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;
    private final JwtService utilisateurUtil;
    private final DemandeurRepository demandeurRepository;
    private final DemandeRepository demandeRepository;
    private final AuthenticationManager authenticationManager;
    private final ObjectValidator<RegistrationRequest> validator;
    private final PasswordResetTokenRepository resetTokenRepository;
    private final ProfileServiceImpl profileService;

    private final FileUploadServiceImpl fileUploadServiceimpl;
    private final FileUploadRepositoy fileUploadRepositoy;


    private final AtomicInteger compteur = new AtomicInteger(0);



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





    public AuthenticationResponse registerUtilisateur(RegistrationRequest registrationRequest) throws IOException, WriterException {
        validator.validate(registrationRequest);

        boolean emailExists = utilisateurRepository.existsByEmail(registrationRequest.getEmail());
        boolean ninExists = utilisateurRepository.existsByNin(registrationRequest.getNin());

        if (emailExists && ninExists) {
            Optional<Demandeur> optionalDemandeur = demandeurRepository.findByNin(registrationRequest.getNin());
            if (optionalDemandeur.isPresent()) {
                Demandeur demandeur = optionalDemandeur.get();
                Optional<Demande> optionalDemande = demandeRepository.findById(demandeur.getId());

                if (optionalDemande.isPresent()) {
                    Demande demande = optionalDemande.get();

                    if ("rejetée".equals(demande.getStatut())) {
                        // Mettre à jour la demande
                        demande.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
                        demande.setDatedemande(LocalDateTime.now());
                        demande.setMotifrejet(null);
                        demandeRepository.save(demande);

                        // Ajouter un message de confirmation
                        System.out.println("La demande rejetée a été réactivée.");
                        return AuthenticationResponse.builder()
                                .message("La demande encours...")
                                .build();
                    }
                }
            }
            // Si aucune demande n'a été mise à jour, lever l'exception
            throw new EmailAndNinAlreadyExistException("EMAIL_NIN_EXIST");
        }

        if (emailExists) {
            throw new EmailAlreadyExistException("email exist");
        }

        if (ninExists) {
            throw new NinAlreadyExistException("nin exist");
        }

        // Création de l'utilisateur
        Utilisateur utilisateur = convertToEntity(registrationRequest);
        String generatedPassword = UUID.randomUUID().toString().substring(0, 8);
        String rawPassword = generatedPassword;
        utilisateur.setPassword(passwordEncoder.encode(rawPassword));
        utilisateur.setStatut(true);
        utilisateur.setProfile(findOrCreateProfile("user"));
        utilisateurRepository.save(utilisateur);

        // Création du Demandeur à partir de DemandeurDTO
        Demandeur demandeur = new Demandeur();
        demandeur.setUtilisateur(utilisateur); // Lier le demandeur à l'utilisateur
        demandeur.setTelephone(registrationRequest.getTelephone()); // Téléphone
        demandeur.setDatedenaissance(registrationRequest.getDatedenaissance()); // Date de naissance
        demandeur.setLieudenaissance(registrationRequest.getLieudenaissance()); // Lieu de naissance
        demandeur.setAdresse(registrationRequest.getAdresse()); // Adresse
        demandeur.setSexe(registrationRequest.getSexe()); // Sexe
        demandeur.setFonction(registrationRequest.getFonction()); // Fonction
        demandeur.setNin(registrationRequest.getNin()); // NIN
        demandeur.setScannernin(registrationRequest.getScannernin()); // Scanner du NIN

        // Sauvegarder le demandeur
        demandeurRepository.save(demandeur);

        // Lier le demandeur à l'utilisateur et sauvegarder l'utilisateur
        utilisateur.setDemandeur(demandeur);
        utilisateurRepository.save(utilisateur);

        // Création de la demande en utilisant la logique de la méthode creerDemande
        Optional<Demandeur> demandeurOpt = demandeurRepository.findById(demandeur.getId());
        if (!demandeurOpt.isPresent()) {
            throw new EntityNotFoundException("Demandeur not found");
        }

        Demande demande = new Demande();
        demande.setDemandeur(demandeurOpt.get());
        demande.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
        demande.setDatedemande(LocalDateTime.now());
        demande.setDatetraitement(LocalDateTime.now());
        demande.setValidite(true);
        demande.setObjetdemande(TypeDemande.DEMANDE_NON_APP.getStatut());
        demande.setDescriptiondemande("Description de la demande");
//        demande.setDateexpiration(isExpired());

        // Sauvegarder la demande
        demandeRepository.save(demande);
        // Incrémentation du compteur
        int count = compteur.incrementAndGet();
        System.out.println("Compteur actuel : " + count);


        // Réinitialisation du compteur lorsqu'il atteint 5
        if (count >= 1) {
            compteur.set(0);
            System.out.println("Compteur réinitialisé à 0");
        }
        // Sending notification if count == 1
        if (count == 1) {
            Optional<Profile> profileOpt = profileRepository.findByCode("traitant");

            if (profileOpt.isEmpty()) {
                System.out.println("Le profil TRAITANT n'existe pas.");
                //return 0;
            }

            Profile profilTraitant = profileOpt.get();

            // Récupérer tous les utilisateurs ayant ce profil
            List<Utilisateur> utilisateursTraitants = utilisateurRepository.findByProfil(profilTraitant);

            if (utilisateursTraitants.isEmpty()) {
                System.out.println("Aucun utilisateur avec le profil TRAITANT trouvé.");
                //return 0;
            }

            // Envoyer les notifications en parallèle
            utilisateursTraitants.parallelStream().forEach(utilisateurs -> {
                System.out.println("Envoi de notification à : " + utilisateur.getTelephone());
                Constantes.sendNotificationWhatsapp("Vous avez une nouvelle demande en attente de traitement." + demande.getId() + LocalDateTime.now(), utilisateur.getTelephone());
            });
        }


        // Génération du token avec les informations utilisateur et demandeur
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", utilisateur.getId());
        claims.put("email", utilisateur.getEmail());
        claims.put("nin", utilisateur.getNin());
        claims.put("fullName", utilisateur.getFullName());
        claims.put("profile", utilisateur.getProfile().getCode());
        claims.put("demandeurId", demandeur.getId()); // Ajouter l'ID du demandeur dans le token

        String token = utilisateurUtil.generateToken(utilisateur, claims);

        // Retourner la réponse avec le token et l'ID de la demande
        return AuthenticationResponse.builder()
                .token(token)
                .userId(utilisateur.getId())
                .email(utilisateur.getEmail())
                .password(rawPassword)
                .nin(utilisateur.getNin())
                .fullName(utilisateur.getFullName())
                .profile(utilisateur.getProfile().getCode())
                .demandeurId(demandeur.getId()) // ID du demandeur dans la réponse
                .demandeId(demande.getId()) // ID de la demande dans la réponse
                .build();
    }



//
//    public int registerUtilisateur(RegistrationRequest registrationRequest) throws IOException, WriterException {
//        validator.validate(registrationRequest);
//
//        boolean emailExists = utilisateurRepository.existsByEmail(registrationRequest.getEmail());
//        boolean ninExists = utilisateurRepository.existsByNin(registrationRequest.getNin());
//
//        if (emailExists && ninExists) {
//            Optional<Demandeur> optionalDemandeur = demandeurRepository.findByNin(registrationRequest.getNin());
//            if (optionalDemandeur.isPresent()) {
//                Demandeur demandeur = optionalDemandeur.get();
//                Optional<Demande> optionalDemande = demandeRepository.findById(demandeur.getId());
//
//                if (optionalDemande.isPresent()) {
//                    Demande demande = optionalDemande.get();
//
//                    if ("rejetée".equals(demande.getStatut())) {
//                        demande.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
//                        demande.setDatedemande(LocalDateTime.now());
//                        demande.setMotifrejet(null);
//                        demandeRepository.save(demande);
//
//                        System.out.println("La demande rejetée a été réactivée.");
//                        return 1; // Succès
//                    }
//                }
//            }
//            throw new EmailAndNinAlreadyExistException("EMAIL_NIN_EXIST");
//        }
//
//        if (emailExists) {
//            throw new EmailAlreadyExistException("email exist");
//        }
//
//        if (ninExists) {
//            throw new NinAlreadyExistException("nin exist");
//        }
//
//        // Création de l'utilisateur
//        Utilisateur utilisateur = convertToEntity(registrationRequest);
//        utilisateur.setPassword(passwordEncoder.encode(UUID.randomUUID().toString().substring(0, 8)));
//        utilisateur.setStatut(true);
//        utilisateur.setProfile(findOrCreateProfile("user"));
//        utilisateurRepository.save(utilisateur);
//
//        // Création du demandeur
//        Demandeur demandeur = new Demandeur();
//        demandeur.setUtilisateur(utilisateur);
//        demandeur.setTelephone(registrationRequest.getTelephone());
//        demandeur.setDatedenaissance(registrationRequest.getDatedenaissance());
//        demandeur.setLieudenaissance(registrationRequest.getLieudenaissance());
//        demandeur.setAdresse(registrationRequest.getAdresse());
//        demandeur.setSexe(registrationRequest.getSexe());
//        demandeur.setFonction(registrationRequest.getFonction());
//        demandeur.setNin(registrationRequest.getNin());
//        demandeur.setScannernin(registrationRequest.getScannernin());
//        demandeurRepository.save(demandeur);
//
//        // Création de la demande
//        Demande demande = new Demande();
//        demande.setDemandeur(demandeur);
//        demande.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
//        demande.setDatedemande(LocalDateTime.now());
//        demande.setDatetraitement(LocalDateTime.now());
//        demande.setValidite(true);
//        demande.setObjetdemande(TypeDemande.DEMANDE_NON_APP.getStatut());
//        demande.setDescriptiondemande("Description de la demande");
//        demandeRepository.save(demande);
//
//        System.out.println("Inscription réussie, retour 1");
//        return 1; // Succès
//    }




}
