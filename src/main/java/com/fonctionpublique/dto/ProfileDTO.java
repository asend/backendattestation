package com.fonctionpublique.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDTO {

    private int id;
    private String code;
    private String etat;
    private String libelle;

}
