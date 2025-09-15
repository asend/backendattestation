package com.fonctionpublique.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Demande {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String urlattestation;
    private String statut;
    private String motifrejet;
    private String numerodemande;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime datedemande;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime datetraitement;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate dateexpiration;
    private boolean validite;
    private String objetdemande;
    private String descriptiondemande;
    private String attestationName;
    @Transient
    private String nin;
    @OneToOne
    private Certification certification;
    @ManyToOne
    @JoinColumn(name = "id_demandeur")
    private Demandeur demandeur;
    @ManyToOne
    @JoinColumn(name = "id_utilisateur")
    private Utilisateur utilisateur;
    private LocalDateTime tempsEcoule;
    private String dureeTraitement;

    public boolean isValide() {
        validite=false;
        LocalDate localDateTime = datetraitement.plusMonths(9).toLocalDate();
        Calendar c = Calendar.getInstance();
        //LocalDateTime now = c.getTime().getTime();
        if (localDateTime.isAfter(LocalDate.now())) {
            validite=true;
            return true;
        }
        return false;
    }

    public boolean AttestionExpired(){

        LocalDate currentdate = LocalDate.now();

        if(currentdate.isBefore(dateexpiration)){

            return this.validite= true;
        }
        return this.validite = false;
    }



}
