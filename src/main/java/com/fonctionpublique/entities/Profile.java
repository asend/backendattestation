package com.fonctionpublique.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@SuperBuilder
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String code;
    private String etat;
    private String libelle;

    @OneToMany(mappedBy = "profile")
    private List<Utilisateur> utilisateur;


}
