package com.fonctionpublique.repository;

import com.fonctionpublique.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Integer> {
    boolean existsByEmail(String email);
    Optional<Utilisateur> findByEmail(String email);
    boolean existsByNin(String nin);

    List<Utilisateur> findAll();
    @Query("select d.email from Utilisateur d ")
    List<String> getAllAdmin();
    @Query("select d.email from Utilisateur d ")
    Utilisateur findAllEmail(String email);


    Optional<Utilisateur> findByNin(String nin);

    //Optional<Utilisateur> findByStatut(String statut);

//    Utilisateur findById();
}
