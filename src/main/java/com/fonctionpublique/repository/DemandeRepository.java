package com.fonctionpublique.repository;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Integer> {

    List<Demande> findAll();

    Optional<Demande> findById(int id);

    List<Demandeur> getByStatut(String statut);

    List<Demande> findByStatut(String status);

    List<Demande> findByDemandeurId(int id);


    Demande findAttestationById(int id);

    @Query("select d from Demande d where d.demandeur.id = :demandeurId and d.statut in ('cours','approuvee')")
    List<Demande> demandeByDemandeurStatut(Integer demandeurId);

    Demande findByAttestationName(String code);
}
