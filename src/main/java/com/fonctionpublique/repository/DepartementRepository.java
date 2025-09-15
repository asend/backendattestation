package com.fonctionpublique.repository;

import com.fonctionpublique.entities.Departement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartementRepository extends JpaRepository<Departement, Integer> {
    List<Departement> findByRegionId(Integer regionId);



}
