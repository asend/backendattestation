package com.fonctionpublique.repository;

import com.fonctionpublique.entities.Certification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CertificationRepository extends JpaRepository<Certification,Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Certification c SET c.demande = null WHERE c.demande.id = :demandeId")
    void detachDemandeFromCertification(@Param("demandeId") Integer demandeId);
}
