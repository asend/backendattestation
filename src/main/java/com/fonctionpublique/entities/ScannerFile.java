package com.fonctionpublique.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Data
@NoArgsConstructor
public class ScannerFile {
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;
    private String name;
    private String type;
    @Lob
    @Column(name = "data", columnDefinition="LONGTEXT")
//    @Column( length = 100000 )
    private byte[] data;

    public ScannerFile(String name, String type, byte[] data) {
        this.name = name;
        this.type = type;
        this.data = data;
    }
}
