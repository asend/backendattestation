package com.fonctionpublique.services.demande;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.entities.Demande;
import io.jsonwebtoken.lang.Arrays;

import java.util.List;
import java.util.Map;

public interface DemandeService {
    List<DemandeDTO> findAll();

    DemandeDTO update(DemandeDTO demandeDTO);

    DemandeDTO getById(int id);

    Integer creerDemande(int id);


    List<DemandeDTO> findByStatut(String status);

    List<DemandeDTO> findByDemandeurId(int id);

    String findAttestation(int id);

    Map<String,Integer> demandeByStatut();

    List<Demande> demandeByDemandeurStatut(Integer id);

    Demande getByCode(String code);

    List<DemandeDTO> demandeActif();
}
