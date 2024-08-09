package com.fonctionpublique.services.dashbord;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.services.demande.DemandeServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

//@RequiredArgsConstructor
@Service
public class DashbordServiceImpl implements DashbordService {


    private final DemandeServiceImpl demandeService;

    public DashbordServiceImpl(@Lazy DemandeServiceImpl demandeService) {
        this.demandeService = demandeService;
    }

    @Override
    public ResponseEntity<Integer> getCount() {
        List<DemandeDTO> demades = demandeService.findAll();
        return new ResponseEntity<>(demades.size(), HttpStatus.OK);
    }

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

}


