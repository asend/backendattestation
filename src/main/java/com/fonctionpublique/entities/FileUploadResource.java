package com.fonctionpublique.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FileUploadResource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String type;
    private long taille;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @Transient
    private byte[] file;
    @Column(name = "upload_dir")
    private String uploadDir;
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "demandeur_id")
    private Demandeur demandeur;

    public FileUploadResource(String fileName, String contentType, byte[] bytes) {
    }


    public String getImageUrl() {
        return  " " + name;
    }

}
