package com.fonctionpublique.repository;

import com.fonctionpublique.entities.Titre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TitreRepository extends JpaRepository<Titre,Integer> {
}
