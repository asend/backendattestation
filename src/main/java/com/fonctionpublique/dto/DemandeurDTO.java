package com.fonctionpublique.dto;

import com.fonctionpublique.entities.Demande;
import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeurDTO {

    private int id;
    @NotNull(message = "telephone ne doit pas etre null")
    @NotEmpty(message = "telephone ne doit pas etre null")
    @NotBlank(message = "telephone ne doit pas etre vide")
    private String telephone;
    private String datedenaissance;
    @NotNull(message = "lieu de naissance ne doit pas etre null")
    @NotEmpty(message = "lieu de naissance ne doit pas etre null")
    @NotBlank(message = "lieu de naissance ne doit pas etre vide")
    private String lieudenaissance;
    @NotNull(message = "adresse ne doit pas etre null")
    @NotEmpty(message = "adresse ne doit pas etre null")
    @NotBlank(message = "adresse ne doit pas etre vide")
    private String adresse;
    @NotNull(message = "sexe ne doit pas etre null")
    @NotEmpty(message = "sexe ne doit pas etre null")
    @NotBlank(message = "sexe ne doit pas etre vide")
    private String sexe;
    private String fonction;
    private String nin;
    private String scannernin;
    private String statut;
    private UtilisateurDTO utilisateurDTO;
    private String email;
    private String prenom;
    private String nom;
    private String fullName;
    private boolean isCompleted;
    private int userId;
    private Demande demandeDTO;
    private byte[] displayPicture;
    private String type;
}
