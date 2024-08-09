package com.fonctionpublique.services.demandeur;

import com.fonctionpublique.dto.DemandeurDTO;

import java.util.List;

public interface DemandeurService {
    int creerDemandeur(DemandeurDTO demandeurDTO);

    DemandeurDTO getByNin(String nin);
    DemandeurDTO getById(int id);
    public List<DemandeurDTO> findAll();
    Integer upadateDemandeur(DemandeurDTO demandeurDTO);
}
