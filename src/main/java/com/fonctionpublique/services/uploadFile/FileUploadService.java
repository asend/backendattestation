package com.fonctionpublique.services.uploadFile;

import com.fonctionpublique.entities.FileUpload;
import com.fonctionpublique.entities.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileUploadService {
    FileUploadResponse uploadFile(MultipartFile file, int id) throws IOException;
    FileUpload download(long fileId) throws Exception;
}