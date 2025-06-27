package com.fonctionpublique.repository;

import com.fonctionpublique.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository  extends JpaRepository<Region, Integer> {
}
