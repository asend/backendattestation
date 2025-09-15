package com.fonctionpublique.controllers;

import com.fonctionpublique.services.EnvoiOpt;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/envoi-whatsapp")
@CrossOrigin
public class SendOptController {

    @Autowired
    private EnvoiOpt envoiOpt;

    @GetMapping("")
    public ResponseEntity<String> getById(@RequestParam String number, @RequestParam String message) {
        envoiOpt.sendWhatsapp(message, number);
        return ResponseEntity.ok("envoyé ");

    }

}
