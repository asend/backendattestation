package com.fonctionpublique.controllers;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.dto.ProfileDTO;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.services.profile.ProfileServiceImpl;
import com.fonctionpublique.services.utilisateur.UtilisateurServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@Tag(name="profile")
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileServiceImpl profileService;



    @GetMapping("/code/{code}")
    public ResponseEntity<ProfileDTO> getByCode(@PathVariable String code) {
        return ResponseEntity.ok(profileService.getByCode(code));
    }
    @GetMapping("/getByProfile/{code}")
    public List<ProfileDTO> findByProfile(@PathVariable String code) {
        return profileService.findUtilisateurByProfile(code);
    }


    @PostMapping("/createProfile")
    public ResponseEntity<ProfileDTO> createProfile(@RequestBody ProfileDTO profileDTO) {
        ProfileDTO createdProfile = profileService.createProfile(profileDTO);
        return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileDTO> updateProfile(@PathVariable int id,@RequestBody ProfileDTO profileDTO) {
        ProfileDTO updatedProfile = profileService.updateProfile(id, profileDTO);
        return new ResponseEntity<>(updatedProfile, HttpStatus.OK);
    }



}
