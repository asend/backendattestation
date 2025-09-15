package com.fonctionpublique.services.dashbord;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.repository.DemandeRepository;
import com.fonctionpublique.services.demande.DemandeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

//@RequiredArgsConstructor
@Service
@RequiredArgsConstructor
public class DashbordServiceImpl implements DashbordService {


    private final DemandeServiceImpl demandeService;
    private final DemandeRepository demandeRepository;



    @Override
    public ResponseEntity<Integer> getCount() {
        List<DemandeDTO> demandes = demandeService.findAll()
                .stream()
                .filter(demande -> !"supprimer".equalsIgnoreCase(demande.getStatut()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(demandes.size(), HttpStatus.OK);
    }

//    public ResponseEntity<Integer> getCount() {
//        List<DemandeDTO> demades = demandeService.findAll();
//        return new ResponseEntity<>(demades.size(), HttpStatus.OK);
//    }

    @Override
    public ResponseEntity<Integer> getApprouved() {
        List<DemandeDTO> demades = demandeService.findByStatut(StatusDemande.DEMANDE_TRAITEE.getStatut());
        return new ResponseEntity<>(demades.size(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Integer> getRejected() {
        List<DemandeDTO> demades = demandeService.findByStatut(StatusDemande.DEMANDE_REFUSEE.getStatut());
        return new ResponseEntity<>(demades.size(), HttpStatus.OK);
    }

    @GetMapping("/getCours")
    public ResponseEntity<Integer> getCours() {
        List<DemandeDTO> demades = demandeService.findByStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
        return new ResponseEntity<>(demades.size(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DemandeDTO>> getDemandeCount() {
        List<DemandeDTO> demades = demandeService.findAll();
        return new ResponseEntity<>(demades, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DemandeDTO>> getDemandeApprouved() {
        List<DemandeDTO> demades = demandeService.findByStatut(StatusDemande.DEMANDE_TRAITEE.getStatut());
        return new ResponseEntity<>(demades, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DemandeDTO>> getDemandeRejected() {
        List<DemandeDTO> demades = demandeService.findByStatut(StatusDemande.DEMANDE_REFUSEE.getStatut());
        return new ResponseEntity<>(demades, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<DemandeDTO>> getDemandeCours() {
        List<DemandeDTO> demades = demandeService.findByStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
        return new ResponseEntity<>(demades, HttpStatus.OK);
    }


    public DemandeDTO convertEntityToDto(Demande demande) {
        return DemandeDTO.builder()
                .id(demande.getId())
                .urlattestation(demande.getUrlattestation())
                .statut(demande.getStatut())
                .numerodemande(demande.getNumerodemande())
                .datedemande(demande.getDatedemande())
                .datetraitement(demande.getDatetraitement())
                .attestationName(demande.getAttestationName())
                .validite(demande.isValide()) // Appel de la méthode isValide() de Demande pour obtenir la validité
                .objetdemande(demande.getObjetdemande())
                .descriptiondemande(demande.getDescriptiondemande())
               // .certificationDTO(certificationToDTO(demande.getCertification())) // Mapping de Certification en CertificationDTO
                //.demandeurDTO(demande.getDemandeur()) // Mapping de Demandeur en DemandeurDTO
                .dateexpiration(demande.getDateexpiration())
                .build();
    }
    public Demande convertDtoToEntity(DemandeDTO demandeDTO) {
        return Demande.builder()
                .id(demandeDTO.getId())
                .urlattestation(demandeDTO.getUrlattestation())
                .statut(demandeDTO.getStatut())
                .numerodemande(demandeDTO.getNumerodemande())
                .datedemande(demandeDTO.getDatedemande())
                .datetraitement(demandeDTO.getDatetraitement())
                .attestationName(demandeDTO.getAttestationName())
                .validite(demandeDTO.isValidite()) // Appel de la méthode isValide() de Demande pour obtenir la validité
                .objetdemande(demandeDTO.getObjetdemande())
                .descriptiondemande(demandeDTO.getDescriptiondemande())
                // .certificationDTO(certificationToDTO(demande.getCertification())) // Mapping de Certification en CertificationDTO
                //.demandeurDTO(demande.getDemandeur()) // Mapping de Demandeur en DemandeurDTO
                .dateexpiration(demandeDTO.getDateexpiration())
                .build();
    }




    public List<DemandeDTO> getDemandeApprouvedBySignature(String signature) {
        // Récupérer les demandes approuvées par un traitant avec une signature spécifique
        List<Demande> demandes = demandeRepository.findBySignatureAndStatut(signature, StatusDemande.DEMANDE_TRAITEE.getStatut());

        // Convertir les demandes en DemandeDTO en utilisant la méthode convertEntityToDto
        return demandes.stream()
                .map(this::convertEntityToDto) // Utilisation de la méthode convertEntityToDto pour chaque Demande
                .collect(Collectors.toList()); // Collecte des résultats dans une liste
    }
}


