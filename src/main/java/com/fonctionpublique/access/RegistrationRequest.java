package com.fonctionpublique.access;

import com.fonctionpublique.entities.Utilisateur;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @NotNull(message = "password ne doit pas etre null")
    @NotEmpty(message = "password ne doit pas etre null")
    @NotBlank(message = "password ne doit pas etre vide")
    private String password;
    private String status;
    @NotNull(message = "nin ne doit pas etre null")
    @NotEmpty(message = "nin ne doit pas etre null")
    @NotBlank(message = "nin ne doit pas etre vide")
    private String nin;
    private String passePort;

    public  String fullName(){
        return prenom + " " + nom;
    }

    public static Utilisateur convertToEntity(RegistrationRequest entity){
        return  Utilisateur.builder()
                .nin(entity.getNin())
                .prenom(entity.getPrenom())
                .nom(entity.getNom())
                .email(entity.getEmail())
                .build();
    }


}
