package com.fonctionpublique.services.structure;

import com.fonctionpublique.entities.Structure;
import com.fonctionpublique.repository.StructureRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StructureServiceImpl  implements  StructureService{

    private  final StructureRepository structureRepository;
    @PostConstruct
    public void StructureExistingData(){
        int num = 1;
        structureRepository.findById(num).orElseGet(() ->{

            Structure structure = new Structure();
            structure.setNomStructure("Ministère de la Fonction Publique " + "\n" + " et de la Réforme du Service Public");
            structure.setAbreviationNomStructure("MFPRSP");
            structure.setReference("/DGFP/CE");
            structure.setNatureAttestation("_SN_DNAFP_");
            structure.setEmail("fonctionpublique@fonctionpublique.gouv.sn");
            structure.setContact("33 839 66 04 / 33 869 66 00 ");
            structure.setBoitePostale("BP:4007 Buiding Administratif");
            structure.setLocalisation("Dakar Plateau-52 rue Vincens x Abdou Karim BOURGI");
            return structureRepository.save(structure);

        });




    }
}
