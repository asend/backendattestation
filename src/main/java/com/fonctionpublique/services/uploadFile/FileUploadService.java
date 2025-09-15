package com.fonctionpublique.services.uploadFile;

import com.fonctionpublique.entities.FileUpload;
import com.fonctionpublique.entities.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileUploadService {
    FileUploadResponse uploadFile(MultipartFile file, int id) throws IOException;
//    FileUpload download(long fileId) throws Exception;

    FileUpload uploadImage(MultipartFile file) throws IOException;
//    FileUpload uploadImageDemandeur(MultipartFile file, Integer id) throws Exception;

    FileUpload uploadImageDemandeur(MultipartFile file, String base64Image, Integer id) throws Exception;

    List<FileUpload> getImageDemandeur(Integer id);
    FileUpload getImageDetails(Integer id) throws IOException;

    FileUpload save(FileUpload fileUpload);
    void deleteImage(Long id);
    FileUpload getById(Long id);
}