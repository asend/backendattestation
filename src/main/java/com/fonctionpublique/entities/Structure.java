package com.fonctionpublique.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Structure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String nomStructure;
    private String abreviationNomStructure;
    private String email;
    private String contact;
    private String boitePostale;
    private String localisation;
    private String reference;
    private String natureAttestation;


}
