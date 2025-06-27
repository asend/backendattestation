package com.fonctionpublique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class IntercoDto {

    private String prenom;
    private String nom;
    private String email;
    private String confirmemail;
    private String scannernin;
    private String password;
    private String nin;
    private String telephone;
    private Date datedenaissance;
    private String lieudenaissance;
    private String adresse;
    private String sexe;
    private String fonction;

}
