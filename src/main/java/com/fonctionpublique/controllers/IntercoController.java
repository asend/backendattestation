package com.fonctionpublique.controllers;

import com.fonctionpublique.access.RegistrationRequest;
import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.dto.RegisterRequestDTO;
import com.fonctionpublique.entities.FileUpload;
import com.fonctionpublique.handleException.DemandeurNotExist;
import com.fonctionpublique.services.interco.IntercoSerciceImpl;
import com.fonctionpublique.services.uploadFile.FileUploadService;
import com.fonctionpublique.services.uploadFile.FileUploadServiceImpl;
import com.fonctionpublique.services.utilisateur.AuthenticationResponse;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/interco")
@RequiredArgsConstructor
@CrossOrigin
public class IntercoController {

    private final IntercoSerciceImpl intercoService;
    private final FileUploadServiceImpl fileUploadServiceImpl;


    @PostMapping
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest registrationRequest)
            throws Exception {
        AuthenticationResponse response = intercoService.registerUtilisateur(registrationRequest);
        return ResponseEntity.ok(response);
    }

//    @PostMapping
//    public ResponseEntity<Integer> register(@RequestBody RegistrationRequest registrationRequest) throws Exception {
//        int response = intercoService.registerUtilisateur(registrationRequest);
//        return ResponseEntity.ok(response);
//    }




}
