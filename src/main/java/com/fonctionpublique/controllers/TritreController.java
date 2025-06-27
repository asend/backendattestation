package com.fonctionpublique.controllers;

import com.fonctionpublique.dto.TitreDTO;
import com.fonctionpublique.services.titre.TitreServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/titre")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name = "titre")
public class TritreController {

    @Autowired
    private TitreServiceImpl titreService;

    @PostMapping
    public ResponseEntity<Integer> createTitre(@RequestBody TitreDTO titreDTO) {
        Integer createdTitreId = titreService.createTitre(titreDTO);
        return ResponseEntity.ok(createdTitreId);
    }
//    public ResponseEntity<TitreDTO> createTitre(@RequestBody TitreDTO titreDTO) {
//        TitreDTO createdTitre = titreService.createTitre(titreDTO);
//        return ResponseEntity.ok(createdTitre);
//    }

    // Endpoint pour afficher tous les titres
    @GetMapping
    public ResponseEntity<List<TitreDTO>> getAllTitres() {
        List<TitreDTO> titres = titreService.getAllTitres();
        return ResponseEntity.ok(titres);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Integer> deleteTitre(@PathVariable("id") Integer titreId) {
        titreService.deleteTitre(titreId);
        return ResponseEntity.ok(titreId); // Return the ID of the deleted titre
    }
//    public ResponseEntity<String> deleteTitre(@PathVariable("id") Integer titreId) {
//            titreService.deleteTitre(titreId);
//            return ResponseEntity.ok("Titre deleted successfully");
//    }
}
