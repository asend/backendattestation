package com.fonctionpublique.controllers;

import com.fonctionpublique.services.demande.DemandeServiceImpl;
import com.fonctionpublique.services.mail.MailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
@Tag(name="mail")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;
    private final DemandeServiceImpl demandeService;

    @GetMapping("/reject-interne/{id}")
    public Integer sendMailRejectInterne(@PathVariable("id") Integer id){
        return mailService.sendMailRejectInterne(id);
}



    @GetMapping("/reject-externe/{id}")
    public Integer sendMailRejectexterne(@PathVariable("id") Integer id){
    return mailService.sendMailRejectExterne(id);
}

    @GetMapping("/approuve/{id}")
    public Integer sendMailApprouve(@PathVariable("id") Integer id){
        return mailService.sendMailApprouvee(id);
    }
}
