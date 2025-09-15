package com.fonctionpublique.services.profile;

import com.fonctionpublique.dto.ProfileDTO;
import com.fonctionpublique.entities.Profile;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.repository.ProfileRepository;
import com.fonctionpublique.repository.UtilisateurRepository;
import com.fonctionpublique.validators.ObjectValidator;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final ObjectValidator<ProfileDTO> validator;

    @PostConstruct
    public void roleExisting() {
        int num = 1;
        profileRepository.findAllById(1).orElseGet(() -> {
            Profile adminProfile = new Profile();
            adminProfile.setCode("admin");
            adminProfile.setLibelle("Admininstrateur");
            adminProfile.setEtat("true");
            profileRepository.save(adminProfile);
            return null;
        });
    }

    @PostConstruct
    public void adminExisting() {
        Optional<Profile> admin = profileRepository.findByCode("admin");
        if (admin.isPresent()) {
            Profile adminProfile = admin.get();
            Optional<Utilisateur> existingUser = utilisateurRepository.findByEmail("admin@gmail.com");
            if (existingUser.isEmpty()) {
                Utilisateur adminee = new Utilisateur();
                adminee.setPrenom("ALY");
                adminee.setNom("NDIAYE");
                adminee.setEmail("admin@gmail.com");
                adminee.setPassword(passwordEncoder.encode("passer"));
                adminee.setNin("1234567890987");
                adminee.setStatut(true);
                adminee.setProfile(adminProfile);
                utilisateurRepository.save(adminee);
            }
        }
    }

//    @PostConstruct
//    public void adminExisting() {
//        Optional<Profile> admin = profileRepository.findByCode("admin");
//        if (admin.isPresent()) {
//            Profile adminProfile = admin.get();
//                Utilisateur adminee = new Utilisateur();
//                adminee.setPrenom("ALY");
//                adminee.setNom("NDIAYE");
//                adminee.setEmail("admin@gmail.com");
//                adminee.setPassword(passwordEncoder.encode("passer"));
//                adminee.setNin("1234567890987");
//                adminee.setStatut(true);
//                adminee.setProfile(adminProfile);
//                utilisateurRepository.save(adminee);
//            }
//        }

    public ProfileDTO convertDTO(Profile profile) {

        return ProfileDTO.builder()
                .id(profile.getId())
                .code(profile.getCode())
                .libelle(profile.getLibelle())
                .etat(profile.getEtat())
                .build();
    }

    public static Profile convertToEntity(ProfileDTO profileDTO) {
        return Profile.builder()
                .id(profileDTO.getId())
                .code(profileDTO.getCode())
                .libelle(profileDTO.getLibelle())
                .etat(profileDTO.getEtat())
                .build();
    }


    public ProfileDTO getByCode(String code) {
        return  convertDTO(profileRepository.findByCode(code).orElse(null));
    }


    public List<ProfileDTO> findUtilisateurByProfile(String code) {
        return profileRepository.findByEtat(code).stream().map(this::convertDTO).collect(Collectors.toList());
    }

    public Profile getByCodes(String code) {
        return profileRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Profile non trouvé pour le code : " + code));
    }

    @Override
    public ProfileDTO createProfile(ProfileDTO profileDTO) {
        if (profileDTO.getCode() == null || profileDTO.getCode().isEmpty()) {
            throw new IllegalArgumentException("Le code du profil est obligatoire.");
        }
        if (profileDTO.getLibelle() == null || profileDTO.getLibelle().isEmpty()) {
            throw new IllegalArgumentException("Le libellé du profil est obligatoire.");
        }
        if (profileDTO.getEtat() == null) {
            throw new IllegalArgumentException("L'état du profil est obligatoire.");
        }
        Profile profile = convertToEntity(profileDTO);
        Profile savedProfile = profileRepository.save(profile);
        return convertDTO(savedProfile);
    }
@Override
    public ProfileDTO updateProfile(int profileId, ProfileDTO profileDTO) {
        Profile existingProfile = profileRepository.findById(profileId)
                .orElseThrow(() -> new IllegalArgumentException("Profil introuvable avec l'ID : " + profileId));
        existingProfile.setCode(profileDTO.getCode());
        existingProfile.setLibelle(profileDTO.getLibelle());
        existingProfile.setEtat(profileDTO.getEtat());
        Profile updatedProfile = profileRepository.save(existingProfile);
        return convertDTO(updatedProfile);
    }




}



