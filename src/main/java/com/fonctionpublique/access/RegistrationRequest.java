package com.fonctionpublique.access;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Utilisateur;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegistrationRequest {

    private int id;
    @NotNull(message = "prenom ne doit pas etre null")
    @NotEmpty(message = "prenom ne doit pas etre null")
    @NotBlank(message = "prenom ne doit pas etre vide")
    private String prenom;
    @NotNull(message = "nom ne doit pas etre null")
    @NotEmpty(message = "nom ne doit pas etre null")
    @NotBlank(message = "nom ne doit pas etre vide")
    private String nom;
    @NotNull(message = "email ne doit pas etre null")
    @NotEmpty(message = "email ne doit pas etre null")
    @NotBlank(message = "email ne doit pas etre vide")
    @Email(message = "email incorrect")
    private String email;
    private String password;
    private String status;
    @NotNull(message = "nin ne doit pas etre null")
    @NotEmpty(message = "nin ne doit pas etre null")
    @NotBlank(message = "nin ne doit pas etre vide")
    private String nin;
    private String passePort;
    private String titre;
    private String signature;
    private String telephone;
    private LocalDate datedenaissance;
    private String lieudenaissance;
    private String adresse;
    private String sexe;
    private String fonction;
    private String scannernin;
    private String statut;
    private UtilisateurDTO utilisateurDTO;

//    private MultipartFile file;
//    private String base64Image;


    public  String fullName(){
        return prenom + " " + nom;
    }

    public static Utilisateur convertToEntity(RegistrationRequest entity){
        return  Utilisateur.builder()
                .id(entity.getId())
                .nin(entity.getNin())
                .prenom(entity.getPrenom())
                .nom(entity.getNom())
                .email(entity.getEmail())
                .titre((entity.getTitre()))
                .signature(entity.getSignature())
                .telephone(entity.getTelephone())
                .build();
    }





}
