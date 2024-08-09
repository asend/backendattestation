package com.fonctionpublique.controllers;

import com.fonctionpublique.access.RegistrationRequest;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Structure;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.mailing.StatutMail;
import com.fonctionpublique.services.statut.StatutServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
@CrossOrigin
@Tag(name="sendMail")
public class SendMailController {

    private final StatutServiceImpl statutServiceImpl;
    @GetMapping("/mail_rejete/{id}")
    public Integer rejeted(@PathVariable("id") Integer id){

        return statutServiceImpl.rejetedStatut(id);
    }


}
