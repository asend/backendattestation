package com.fonctionpublique.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FileUploadResponse {
        private String fileName;
        private String fileDownloadUrl;
        private String fileType;
        private long size;
    }

