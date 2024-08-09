package com.fonctionpublique.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScannerFileResponse {

    private String id;
    private String name;
    private String urlTelechargement;
    private String type;
    private long data;

    public ScannerFileResponse(String name, String fileDownloadUri, String type, int length) {
    }
}

