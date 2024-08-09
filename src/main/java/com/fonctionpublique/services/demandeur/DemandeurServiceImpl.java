package com.fonctionpublique.services.demandeur;

import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.repository.UtilisateurRepository;
import com.fonctionpublique.services.demande.DemandeServiceImpl;
import com.fonctionpublique.services.utilisateur.UtilisateurServiceImpl;
import com.fonctionpublique.validators.ObjectValidator;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DemandeurServiceImpl implements DemandeurService {

    private final DemandeurRepository demandeurRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final ObjectValidator<DemandeurDTO> validator;


    @Override
    public int creerDemandeur(DemandeurDTO demandeurDTO) {
        validator.validate(demandeurDTO);
        Optional<Utilisateur> optionalUtilisateurs =
                utilisateurRepository.findByNin(demandeurDTO.getNin());
        if (!optionalUtilisateurs.isPresent()) {
            throw new EntityNotFoundException("NOT_FOUND");
        }
        Demandeur demandeur = convertToEntity(demandeurDTO);
        demandeur.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
        demandeur.setUtilisateur(optionalUtilisateurs.get());
        demandeurRepository.save(demandeur);
        optionalUtilisateurs.get().setDemandeur(demandeur);
        utilisateurRepository.save(optionalUtilisateurs.get());
        return demandeur.getId();
    }

    @Override
    public DemandeurDTO getByNin(String nin) {
        return demandeurRepository.findByNin(nin)
                .map(this::convertToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Not found"));
    }

    @Override
    public DemandeurDTO getById(int id) {
        return demandeurRepository.findById(id).map(this::convertToDTO).orElseThrow(() -> new EntityNotFoundException("not found"));
    }

    @Override
    public List<DemandeurDTO> findAll() {
        return demandeurRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public DemandeurDTO convertToDTO(Demandeur demandeur) {

        return DemandeurDTO.builder()
                .id(demandeur.getId())
                .adresse(demandeur.getAdresse())
                .nom(demandeur.getUtilisateur().getNom())
                .prenom(demandeur.getUtilisateur().getPrenom())
                .email(demandeur.getUtilisateur().getEmail())
                .sexe(demandeur.getSexe())
                .datedenaissance(demandeur.getDatedenaissance())
                .lieudenaissance(demandeur.getLieudenaissance())
                .fonction(demandeur.getFonction())
                .nin(demandeur.getNin())
                .telephone(demandeur.getTelephone())
                .scannernin(demandeur.getScannernin())
                .userId(demandeur.getUtilisateur().getId())
                .statut(demandeur.getStatut())
                .fullName(demandeur.getUtilisateur().getFullName())
                .isCompleted(demandeur.isCompleted())
                .displayPicture(demandeur.getDisplayPicture())
                .type(demandeur.getType())
                .build();

    }

    public Demandeur convertToEntity(DemandeurDTO demandeurDTO) {
        return Demandeur.builder()
                .id(demandeurDTO.getId())
                .nin(demandeurDTO.getNin())
                .adresse(demandeurDTO.getAdresse())
                .datedenaissance(demandeurDTO.getDatedenaissance())
                .fonction(demandeurDTO.getFonction())
                .lieudenaissance(demandeurDTO.getLieudenaissance())
                .statut(demandeurDTO.getStatut())
                .scannernin(demandeurDTO.getScannernin())
                .sexe(demandeurDTO.getSexe())
                .telephone(demandeurDTO.getTelephone())
                .displayPicture(demandeurDTO.getDisplayPicture())
                .type(demandeurDTO.getType())
                .build();

    }


    @Override
    @Transactional
    public Integer upadateDemandeur(DemandeurDTO demandeurDTO) {
        validator.validate(demandeurDTO);
        Utilisateur utilisateur = demandeurRepository.findById(demandeurDTO.getId()).get().getUtilisateur();
        utilisateur.setPrenom(demandeurDTO.getPrenom());
        utilisateur.setNom(demandeurDTO.getNom());
        utilisateur.setNin(demandeurDTO.getNin());
        Demandeur demandeur = convertToEntity(demandeurDTO);
        demandeur.setUtilisateur(utilisateur);
        return demandeurRepository.save(demandeur).getId();
    }
}
