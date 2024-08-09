package com.fonctionpublique.dto;

import com.fonctionpublique.entities.Demandeur;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UtilisateurDTO {

    private int id;
    private String prenom;
    private String nom;
    private String email;
    private String nin;
    private String passPort;
    private String typePieces;
    private boolean statut;
    private String signature;
    private DemandeurDTO demandeurDTO;
    private String fullName;


}
