package com.fonctionpublique.services.regionDepartement;

import com.fonctionpublique.entities.Departement;
import com.fonctionpublique.entities.Region;
import com.fonctionpublique.repository.DepartementRepository;
import com.fonctionpublique.repository.RegionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RegionDempartemenImpl {

    private final RegionRepository regionRepository;
    private final DepartementRepository departementRepository;


    public List<Region> getAllRegions() {
        return regionRepository.findAll();
    }

    public List<Departement> getDepartementsByRegionId(Integer regionId) {
        return departementRepository.findByRegionId(regionId);
    }
}
