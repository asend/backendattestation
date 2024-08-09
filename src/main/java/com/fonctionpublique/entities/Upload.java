package com.fonctionpublique.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Upload {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  int id;
    private  String userName;
    @Lob
    @Column(length = 1000000)
    private byte[] displayPicture;
}
