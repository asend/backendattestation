package com.fonctionpublique.controllers;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.dto.DepartementDemandesDTO;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Region;
import com.fonctionpublique.repository.DemandeRepository;
import com.fonctionpublique.repository.RegionRepository;
import com.fonctionpublique.services.demande.DemandeServiceImpl;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
    private final DemandeRepository demandeRepository;
    private final RegionRepository regionRepository;


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


//    @GetMapping("/getDemande")
//    public Page<DemandeDTO> findAll(
//            @RequestParam(defaultValue = "1") int page,      // numéro de page (0 = première page)
//            @RequestParam(defaultValue = "10") int size,     // taille de la page
//            @RequestParam(defaultValue = "id") String sortBy // champ de tri
//    ) {
//        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
//        return demandeServiceImpl.findAll(pageable);
//    }

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
            if(d.getStatut().equals("cours") || (d.getStatut().equals("approuvée"))){
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

    @GetMapping("/qrcode/{code}")
    public DemandeDTO getByQrCode(@PathVariable("code") String code){
        return demandeServiceImpl.convertToDTO(demandeServiceImpl.getByCode(code));
    }

    @GetMapping("/getByGenre/{sexe}")
    public DemandeDTO getByGenre(@PathVariable("sexe") String sexe){
        return demandeServiceImpl.convertToDTO(demandeServiceImpl.getByGenre(sexe));
    }


    @PutMapping("/update/{id}")
    public Integer updateMotifRejet(@PathVariable Integer id, @RequestBody DemandeDTO demandeDTO) {
        return demandeServiceImpl.updateMotifRejetDemande(id, demandeDTO);
    }
    @PutMapping("/en-cours/{id}")
    public ResponseEntity<Void> annulerDemande(@PathVariable Integer id) {
        demandeServiceImpl.annulerEtDetacherCertification(id);
        return ResponseEntity.ok().build();
    }
//    public String mettreEnCours(@PathVariable Integer id) {
//        demandeServiceImpl.mettreEnCoursEtResetAttestationMotif(id);
//        return "Statut mis en cours, attestation et motif réinitialisés pour la demande " + id;
//    }

    @PutMapping("/updateTempsEcoule/{id}")
    public Integer updateTempsEcoule(@PathVariable Integer id, @RequestBody DemandeDTO demandeDTO) {
        return demandeServiceImpl.updateTempsEcoule(id, demandeDTO);
    }


    @GetMapping("/getDemandesParSexe/{sexe}")
    public Integer getDemandesParSexe(@PathVariable String sexe) {
         return demandeServiceImpl.getDemandesBySexe(sexe);

    }
    @GetMapping("/region/{region}")
    public List<Demande> getDemandesByRegion(@PathVariable String region) {
        return demandeServiceImpl.getDemandesByRegion(region);
    }

    @GetMapping("/par-region/{regionId}")
    public Integer geByRegion(@PathVariable String regionId) {
        return demandeServiceImpl.getDemandesByRegionId(regionId);
    }

    @GetMapping("/par-departement/{departementId}")
    public Integer getDemandesByDepartement(@PathVariable String departementId) {
        return demandeServiceImpl.getDemandesByDepartement(departementId);
    }


    @GetMapping("/par-departement-nom/{departementNom}")
    public Integer getDemandesByDepartementNom(@PathVariable String departementNom) {
        return demandeServiceImpl.getDemandesByDepartementByNom(departementNom);
    }




    @GetMapping("/by-date")
    public Integer getDemandesByDate(@RequestParam("date") String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return demandeServiceImpl.getDemandesByDate(date);
    }


    @GetMapping("/statut-par-date")
    public Map<String, Long> getNombreDemandesParStatutPourDate(@RequestParam("date") String dateStr) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate date = LocalDate.parse(dateStr, formatter);
        return demandeServiceImpl.getNombreDemandesParStatutPourDate(date);
    }


    @GetMapping("/getAllstatuts")
    public List<String> getStatutsExistants() {
        return demandeServiceImpl.getAllStatutsExistants();
    }

    @GetMapping("/count-by-statut")
    public int getCountByStatut(@RequestParam String statut) {
        return demandeServiceImpl.countDemandeByStatut(statut);
    }

    @GetMapping("/check-id/{id}")
    public ResponseEntity<String> checkIdUnique(@PathVariable int id) {
        String result = demandeServiceImpl.verifierUniciteId(id);
        return ResponseEntity.ok(result);
    }


    /***
     * Cette methode permet la representation
     * graphique des sexes m et f
     * en fonction du nombre total
     * de demande
     */
    @GetMapping("/demandes-par-sexe")
    public ResponseEntity<List<Map<String, Object>>> getDemandesParSexe() {
        return ResponseEntity.ok(demandeServiceImpl.getDemandesParSexe());
    }



    /***
     * Cette methode permet la representation
     * graphique et tous les demandes
     * ainsi que par statut
     * @return
     */
    @GetMapping("/statuts-avec-nombre-demandes")
    public ResponseEntity<Map<String, Object>> getStatistiquesDemandes() {
        return ResponseEntity.ok(demandeServiceImpl.getStatistiquesGlobalesDemandes());
    }

    /***
     * Cette methode permet la representation la
     * graphique et tous les regions
     * en fonction du nombre de demande
     * @return
     */
    @GetMapping("/demandes-par-region")
    public ResponseEntity<List<Map<String, Object>>> getDemandesParRegionAvecNom() {
        List<Map<String, Object>> result = demandeServiceImpl.getDemandesParRegionAvecNom();
        return ResponseEntity.ok(result);
    }

    /***
     * Cette methode permet la representation la
     * graphique et tous les Departements
     * en fonction du nombre de demande
     * @return
     */
    @GetMapping("/demandes-par-region-departement")
    public ResponseEntity<List<Map<String, Object>>> getDemandesParRegionEtDepartement() {
        List<Map<String, Object>> result = demandeServiceImpl.getDemandesGroupByRegionAvecDepartements();
        return ResponseEntity.ok(result);
    }

    /***
     * Cette methode permet la representation la
     * graphique et tous les demandes
     * en fonction de la semaine
     * @return
     */
    @GetMapping("/demandes/statut-sur-7-jours")
    public ResponseEntity<List<Map<String, Object>>> getStatutDemandesSur7Jours() {
        return ResponseEntity.ok(demandeServiceImpl.getDemandesDes7DerniersJoursParStatut());
    }

    @GetMapping("/verifier-date/{id}")
    public String verifierDateDemande(@PathVariable int id) {
        return demandeServiceImpl.verifierDateDemande(id);
    }


    @GetMapping("/duree-traitement/{id}")
    public ResponseEntity<String> getDureeTraitement(@PathVariable int id) {
        String duree = demandeServiceImpl.getDureeTraitement(id);
        return ResponseEntity.ok(duree);
    }





}
