package com.fonctionpublique.repository;

import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface DemandeurRepository extends JpaRepository<Demandeur,Integer> {
    Optional<Demandeur> findByNin(String nin);
    List<Demandeur> findAll();
    Optional<Demandeur> findById(int id);

    Optional<Demandeur> findByStatut(String statut);

}
