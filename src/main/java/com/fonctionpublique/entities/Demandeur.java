package com.fonctionpublique.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Demandeur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String telephone;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate datedenaissance;
    private String lieudenaissance;
    private String adresse;
    private String sexe;
    private String fonction;
    @Column(unique = true)
    private String nin;
    private String scannernin;
    private String statut;
    @OneToOne
    @JoinColumn(name = "utilisateur_id")
    private Utilisateur utilisateur;
    @OneToMany
    private List<Demande> demande;
    @Lob
    @Column(length = 1000000)
    private byte[] displayPicture;
    private String type;
    private String region;
    private String departement;
    private String matriculeSolde;

    @OneToMany(mappedBy = "demandeur")
    private List<FileUpload> fileUploads;

    public boolean isCompleted() {
        if (telephone == null || datedenaissance == null || lieudenaissance == null
                || adresse == null || sexe == null || fonction == null || nin == null
                || displayPicture == null) {
            return true;
        }
        return false;

    }


}
