package com.fonctionpublique.services.upload;

import com.fonctionpublique.entities.Upload;
import com.fonctionpublique.repository.UploadReposity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class UploadServiceImpl implements UploadService{

    @Autowired
    private UploadReposity uploadReposity;


    @Override
    public ResponseEntity<Upload> createNewUser(@RequestPart("file") MultipartFile file) throws IOException {
        Upload upload= Upload.builder().displayPicture(file.getBytes()).build();
        uploadReposity.save(upload);
        upload.setDisplayPicture(null);
        return  ResponseEntity.ok(upload);
    }

    @Override
    public  ResponseEntity<List<Upload>> getAllUser(){
        List<Upload> uploadList = uploadReposity.findAll();
        return  ResponseEntity.ok(uploadList);

    }


   @Override
   public byte[] findById(int id) {
     Optional<Upload> uploadOptional = uploadReposity.findById(id);
    return uploadOptional.map(Upload::getDisplayPicture).orElse(null);
}

}
