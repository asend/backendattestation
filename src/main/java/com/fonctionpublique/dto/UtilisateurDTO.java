package com.fonctionpublique.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String titre;
    private DemandeurDTO demandeurDTO;
    private ProfileDTO profileDTO;
    private String fullName;
    private String telephone;


}
