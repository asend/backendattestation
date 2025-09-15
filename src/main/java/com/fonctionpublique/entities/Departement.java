package com.fonctionpublique.entities;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(schema = "departements")
public class Departement {

    @Id
    private Integer id;

    private String nom;

    private Integer regionId;


    public Departement() {}

    public Departement(Integer id, String nom, Integer regionId) {
        this.id = id;
        this.nom = nom;
        this.regionId = regionId;
    }

    public int getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Integer getRegionId() { return regionId; }
    public void setRegionId(Integer regionId) { this.regionId = regionId; }
}

