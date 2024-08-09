package com.fonctionpublique.repository;

import com.fonctionpublique.entities.Compteur;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompteurRepository extends JpaRepository<Compteur,Integer> {
    Optional<Compteur> findByName(String compteurName);
}
