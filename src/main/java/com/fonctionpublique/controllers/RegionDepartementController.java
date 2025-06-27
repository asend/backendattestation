package com.fonctionpublique.controllers;

import com.fonctionpublique.entities.Departement;
import com.fonctionpublique.entities.Region;
import com.fonctionpublique.services.regionDepartement.RegionDempartemenImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api/RegionDepartement")
@RequiredArgsConstructor
public class RegionDepartementController {

    private final RegionDempartemenImpl regionDepartementService;


    @GetMapping
    public List<Region> getAllRegions() {
        return regionDepartementService.getAllRegions();
    }


    @GetMapping("/departemtnByRegion/{regionId}")
    public List<Departement> getDepartementsByRegionId(@PathVariable Integer regionId) {
        return regionDepartementService.getDepartementsByRegionId(regionId);
    }
}
