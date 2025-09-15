package com.fonctionpublique.repository;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface DemandeRepository extends JpaRepository<Demande, Integer> {

    List<Demande> findAll();
    @Modifying
    @Transactional
    @Query("UPDATE Demande d SET d.statut = 'cours', d.attestationName = '', d.motifrejet = '' WHERE d.id = :id")
    void updateStatutEnCoursResetAttestationAndMotif(Integer id);



    Optional<Demande> findById(int id);

    List<Demandeur> getByStatut(String statut);

    List<Demande> findByStatut(String status);

    List<Demande> findByDemandeurId(int id);


    Demande findAttestationById(int id);

    @Query("select d from Demande d where d.demandeur.id = :demandeurId and d.statut in ('cours','approuvee')")
    List<Demande> demandeByDemandeurStatut(Integer demandeurId);

    @Query("select d from Demande d where d.statut in ('cours','approuvee', 'rejetee')")
    List<Demande> demandeActif();

    Demande findByAttestationName(String code);

    @Query("SELECT d FROM Demande d WHERE d.utilisateur.signature = :signature AND d.statut = :statut")
    List<Demande> findBySignatureAndStatut(@Param("signature") String signature, @Param("statut") String statut);

    @Query("SELECT d FROM Demande d WHERE d.demandeur.sexe = :sexe")
    List<Demande> findByDemandeurSexe(@Param("sexe") String sexe);

    @Query("SELECT d FROM Demandeur d WHERE d.sexe = :sexe")
    Demande findBySexe(String sexe);

    @Query("SELECT d FROM Demande d WHERE d.demandeur.region = :region")
    List<Demande> findDemandesByRegion(String region);

    @Query(value = "SELECT d.* FROM demande d " +
            "JOIN demandeur dr ON d.id_demandeur = dr.id " +
            "WHERE dr.region = :regionId", nativeQuery = true)
    List<Demande> findDemandesByRegionId(String regionId);



    @Query("SELECT d FROM Demande d JOIN d.demandeur dr WHERE dr.departement = :departementId")
    List<Demande> findDemandesByDepartementId(@Param("departementId") String departementId);



    @Query("SELECT COUNT(d) FROM Demande d JOIN d.demandeur dr WHERE dr.departement = :departementNom")
    Integer countDemandesByDepartementNom(@Param("departementNom") String departementNom);


    List<Demande> findByDatedemandeBetween(LocalDateTime start, LocalDateTime end);


//    @Query("SELECT d.statut, COUNT(d) FROM Demande d WHERE d.datedemande BETWEEN :start AND :end GROUP BY d.statut")
//    List<Object[]> countDemandesByStatutForDate(@Param("start") LocalDateTime start,
//                                                @Param("end") LocalDateTime end);
    @Query("SELECT d.statut, COUNT(d) FROM Demande d " +
            "WHERE d.datedemande >= :start AND d.datedemande < :end " +
            "AND UPPER(d.statut) <> 'SUPPRIMER' " +
            "GROUP BY d.statut")
    List<Object[]> countDemandesByStatutForDate(@Param("start") LocalDateTime start,
                                                @Param("end") LocalDateTime end);


    @Query("SELECT DISTINCT d.statut FROM Demande d")
    List<String> findDistinctStatuts();

    @Query("SELECT DISTINCT d.statut FROM Demande d WHERE d.statut <> 'SUPPRIMER'")
    List<String> findDistinctStatutsExcludingSupprimer();

    boolean existsById(int id);

@Query("SELECT d.demandeur.sexe, COUNT(d) FROM Demande d WHERE LOWER(d.statut) <> 'supprimer' GROUP BY d.demandeur.sexe")
List<Object[]> countDemandesParSexe();



    @Query("SELECT COUNT(d) FROM Demande d WHERE LOWER(d.statut) <> 'supprimer'")
    Long countTotalDemandesSansSupprimer();


    @Query("SELECT d.statut, COUNT(d) FROM Demande d WHERE LOWER(d.statut) <> 'supprimer' GROUP BY d.statut")
    List<Object[]> countDemandesParStatutSansSupprimer();



//
//    @Query("SELECT new map(r.nom as region, COUNT(d) as totalDemandes) " +
//            "FROM Demande d " +
//            "JOIN d.demandeur dem " +
//            "JOIN Region r ON r.id = CAST(dem.region AS integer) " +
//            "GROUP BY r.nom")
//    List<Map<String, Object>> getDemandesParRegionAvecNom();

    @Query("SELECT new map(" +
            "COALESCE(r.nom, 'Autres') as region, " +
            "COUNT(d) as totalDemandes) " +
            "FROM Demande d " +
            "JOIN d.demandeur dem " +
            "LEFT JOIN Region r ON r.id = CAST(dem.region AS integer) " +
            "GROUP BY COALESCE(r.nom, 'Autres')")
    List<Map<String, Object>> getDemandesParRegionAvecNom();



    @Query("SELECT new map(r.nom as region, dep.nom as departement, COUNT(d) as totalDemandes) " +
            "FROM Demande d " +
            "JOIN d.demandeur dem " +
            "JOIN Departement dep ON dep.id = CAST(dem.departement AS integer) " +
            "JOIN Region r ON r.id = dep.regionId " +
            "GROUP BY r.nom, dep.nom " +
            "ORDER BY r.nom, dep.nom")
    List<Map<String, Object>> getDemandesParRegionEtDepartement();

    @Query("SELECT FUNCTION('DATE', d.datedemande) as date, d.statut as statut, COUNT(d) as total " +
            "FROM Demande d " +
            "WHERE d.datedemande >= :startDate " +
            "GROUP BY FUNCTION('DATE', d.datedemande), d.statut " +
            "ORDER BY date")
    List<Object[]> countDemandesByDateAndStatut(@Param("startDate") LocalDateTime startDate);





}
