package com.fonctionpublique.controllers;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.dto.EmailDTO;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.FileUpload;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.services.demandeur.DemandeurServiceImpl;
import com.fonctionpublique.services.uploadFile.FileUploadServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/demandeur") //api/demandeurs
@RequiredArgsConstructor
@Tag(name="demandeur")
public class DemandeurController {

    private final DemandeurServiceImpl demandeurServiceImpl;
    private final FileUploadServiceImpl fileUploadServiceImpl;
    @PostMapping("/demander")  //
    public ResponseEntity<Integer> incription(@RequestBody DemandeurDTO demandeurDTO){
            return ResponseEntity.ok(demandeurServiceImpl.creerDemandeur(demandeurDTO));
    }

    @PostMapping("")  //
    public ResponseEntity<Integer> register(@RequestBody DemandeurDTO demandeurDTO){
        return ResponseEntity.ok(demandeurServiceImpl.creerDemandeur(demandeurDTO));
    }

    @GetMapping("/getDemandeur")
    public List<DemandeurDTO> findAll(){
        return demandeurServiceImpl.findAll();
    }

    @GetMapping("")
    public List<DemandeurDTO> getAllDemandeurs(){
        return demandeurServiceImpl.findAll();
    }

    @GetMapping("/details/{nin}")
    public ResponseEntity<DemandeurDTO> getByNin(@PathVariable String nin){
        return  ResponseEntity.ok(demandeurServiceImpl.getByNin(nin));
    }

    @GetMapping("/demandeurDetails/{id}")
    public ResponseEntity<DemandeurDTO> getById(@PathVariable int id){
        return  ResponseEntity.ok(demandeurServiceImpl.getById(id));
    }
    @PutMapping("/update")
    public ResponseEntity<Integer> updateDemandeur(@RequestBody DemandeurDTO demandeurDTO){
        return  ResponseEntity.ok(demandeurServiceImpl.upadateDemandeur(demandeurDTO));
    }

    @PutMapping("/updateDemandeurUser")
    public ResponseEntity<Integer> updateDemandeurUser(@RequestBody DemandeurDTO demandeurDTO){
        return  ResponseEntity.ok(demandeurServiceImpl.upadateDemandeurUser(demandeurDTO));
    }

@PutMapping("/update-matricule-solde/{id}")
public ResponseEntity<Integer> updateMatriculeSoldeDemandeur(
        @PathVariable Integer id,
        @RequestBody Map<String, String> body) {

    String matriculeSolde = body.get("matriculeSolde");

    // Log pour debug
    System.out.println("Reçu matriculeSolde: " + matriculeSolde);

    if (matriculeSolde == null || matriculeSolde.trim().isEmpty()) {
        return ResponseEntity.badRequest().build();
    }

    Integer updatedId = demandeurServiceImpl.updateMatriculeSoldeDemandeur(id, matriculeSolde.trim());
    return ResponseEntity.ok(updatedId);
}


    @GetMapping("/getImageByIdAndName/{id}/{name}")
    public ResponseEntity<Map<String, Object>> getImageByIdAndName(
            @PathVariable Long id,
            @RequestParam  String name) {
        FileUpload file = fileUploadServiceImpl.getImageByIdAndName(id, name);

        if (file == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> fileData = new HashMap<>();
        fileData.put("id", file.getId());
        fileData.put("name", file.getName());
        fileData.put("type", file.getType());
        fileData.put("taille", file.getTaille());
        //fileData.put("content", file.getContent()); // Assuming `content` stores the image bytes or link

        return ResponseEntity.ok(fileData);
    }

    @PutMapping("/updateDemandeurAndUtilisateur/{id}")
    public ResponseEntity<Demandeur> updateDemandeur(@PathVariable int id, @RequestBody Demandeur demandeur) {
        Demandeur updated = demandeurServiceImpl.updateDemandeurAndUser(id, demandeur);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/demandeurs/{id}")
    public ResponseEntity<Demandeur> getDemandeurById(@PathVariable int id) {
        Demandeur demandeur = demandeurServiceImpl.getDemandeurById(id);
        return ResponseEntity.ok(demandeur);
    }




}
