package com.fonctionpublique.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Entity
@Data
@Table(name = "file_upload")
@Component
@ConfigurationProperties(prefix = "file")
public class FileUpload  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String type;
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    @Transient
    private byte[] file;
    @Column(name = "upload_dir")
    private String uploadDir;

}