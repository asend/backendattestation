package com.fonctionpublique.controllers;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.services.demande.DemandeServiceImpl;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@RestController
@RequestMapping("/api/demande")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "demande")
public class DemandeController {

    private final DemandeServiceImpl demandeServiceImpl;

    /**
     * apply demande
     *
     * @param id
     * @return
     * @throws IOException
     * @throws WriterException
     */
    @PostMapping("/demandez/{id}")
    public Integer demander(@PathVariable("id") int id) {
        return demandeServiceImpl.creerDemande(id);
    }

    /**
     * Detail demande
     *
     * @param id
     * @return
     */
    @GetMapping("/demandeDetails/{id}")
    public ResponseEntity<DemandeDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok(demandeServiceImpl.getById(id));

    }



    /**
     * get demandes by id demandeur
     *
     * @param id
     * @return
     */
    @GetMapping("/findDemandeurById/{id}")
    public List<DemandeDTO> findByDemandeurId(@PathVariable("id") int id) {
        return demandeServiceImpl.findByDemandeurId(id);
    }

    @GetMapping("/attestation/{id}")
    public String findAttestationName(@PathVariable("id") int id) {
        return demandeServiceImpl.findAttestation(id);
    }

    /**
     * list all demandes
     *
     * @return
     */
    @GetMapping("/getDemande")
    public List<DemandeDTO> findAll() {
        return demandeServiceImpl.findAll();
    }

    /**
     * list all demandes
     *
     * @return
     */
    @GetMapping("")
    public List<DemandeDTO> getAllDemande() {
        return demandeServiceImpl.findAll();
    }

    /**
     * list demande by status
     *
     * @param statut
     * @return
     */
    @GetMapping("/getStatut/{statut}")
    public List<DemandeDTO> findAllDemande(@PathVariable("statut") String statut) {
        return demandeServiceImpl.findByStatut(statut);
    }

    /**
     * list demande Map
     */
    @GetMapping("/parstatut")
    public Map<String,Integer> parstatut(){
        return demandeServiceImpl.demandeByStatut();
    }

    @GetMapping("/eligible/{id}")
    public boolean eligible(@PathVariable("id") Integer id){
        final boolean[] result = {true};
        List<Demande> list = new ArrayList<>();
        String pattern = "dd-MM-yyyy";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);


        //LocalDate currentDate = LocalDate.parse(new Date(LocalDate.now()), formatter);
        demandeServiceImpl.demandeByDemandeurStatut(id).forEach(d->{
            //LocalDate expiredDate = LocalDate.parse(d.getDateexpiration(), formatter);
            if(d.getStatut().equals("cours") || (d.getStatut().equals("approuvee"))){
                result[0] = false;
            }
        });


        return result[0];
    }

    @GetMapping("/demandebytab/{id}")
    public Map<String, List<DemandeDTO>> getbyTab(@PathVariable("id") Integer id){
        return demandeServiceImpl.findByDemandeurIdByStatut(id);
    }

    @GetMapping("/annuler/{id}")
    public DemandeDTO annuler(@PathVariable("id") Integer id){
        return demandeServiceImpl.annuler(id);
    }

    @GetMapping("/demandeActif")
    public List<DemandeDTO> findDemandeActif() {
        return demandeServiceImpl.demandeActif();
    }




}
