package com.fonctionpublique.controllers;

import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.dto.EmailDTO;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.services.demandeur.DemandeurServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@CrossOrigin
@RestController
@RequestMapping("/api/demandeur") //api/demandeurs
@RequiredArgsConstructor
@Tag(name="demandeur")
public class DemandeurController {

    private final DemandeurServiceImpl demandeurServiceImpl;
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

}
