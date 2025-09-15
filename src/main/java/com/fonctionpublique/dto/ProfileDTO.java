package com.fonctionpublique.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileDTO {

    private int id;
    private String code;
    private String etat;
    private String libelle;
    private  UtilisateurDTO utilisateurDTO;

}
