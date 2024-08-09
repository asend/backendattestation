package com.fonctionpublique.services.upload;

import com.fonctionpublique.entities.Upload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface UploadService {
    ResponseEntity<Upload> createNewUser(@RequestPart("file") MultipartFile file) throws IOException;

    ResponseEntity<List<Upload>> getAllUser();

    //    public byte[] findById(@PathVariable int id){
    //        Optional<Upload> uploadOptional = uploadReposity.findById(id);
    //        return uploadOptional.get().getDisplayPicture();
    //
    //    }
    byte[] findById(int id);
}
