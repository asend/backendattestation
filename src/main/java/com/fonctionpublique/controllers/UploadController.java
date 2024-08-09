package com.fonctionpublique.controllers;

import com.fonctionpublique.entities.Upload;
import com.fonctionpublique.entities.User;
import com.fonctionpublique.services.upload.UploadServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/upload")
@CrossOrigin
@RequiredArgsConstructor
public class UploadController {

    @Autowired
    private UploadServiceImpl uploadServiceSimpl;



    @PostMapping
    public ResponseEntity<Upload> createNewUser(@RequestPart("file") MultipartFile file) throws IOException {
        return  uploadServiceSimpl.createNewUser(file);
    }

    @GetMapping
    public  ResponseEntity<List<Upload>> getAllUser(){
       return uploadServiceSimpl.getAllUser();

    }

    @GetMapping("/{id}")
    public byte[] findById(@PathVariable int id){
      return uploadServiceSimpl.findById(id);

    }


}
