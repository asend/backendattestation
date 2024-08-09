package com.fonctionpublique.services.compteur;

import com.fonctionpublique.entities.Compteur;
import com.fonctionpublique.repository.CompteurRepository;
import com.fonctionpublique.repository.DemandeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CompteurServiceImpl implements  CompteurService{

    private final CompteurRepository compteurRepository;
    private final DemandeRepository demandeRepository;

    @PostConstruct
    public void compteurTotalApprouved(){
        String compteurName = "total_Approuved";
        compteurRepository.findByName(compteurName).orElseGet(() -> {
            Compteur compteur = new Compteur();
            compteur.setName(compteurName);
            compteur.setCurrentCount(1);
            return compteurRepository.save(compteur);
        });
    }
    public int incrementCounter() {
        Compteur compteur = compteurRepository.findById(1).orElseThrow(()-> new IllegalArgumentException("not found"));
        int num = compteur.getCurrentCount() + 1;
        compteur.setCurrentCount(num);
        compteurRepository.save(compteur);
        return num ;
    }
    public int resetCounter() {
        Compteur compteur = compteurRepository.findById(1).orElseThrow(()-> new IllegalArgumentException("not found"));
        int num = 1;
        compteur.setCurrentCount(num);
        compteurRepository.save(compteur);
        return num;
    }
    public void compteurTotlaApprouved(){
        Compteur compteur = new Compteur();
        compteur.setName("Total_Approuved");
        compteurRepository.save(compteur);
    }

    public Compteur findById(Integer id){
        return compteurRepository.findById(id).orElse(null);
    }

    @Override
    public Integer save(Compteur compteur) {
        return compteurRepository.save(compteur).getId();
    }
}
