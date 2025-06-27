package com.fonctionpublique.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
@Entity
public class Titre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String titre;
}
