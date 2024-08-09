package com.fonctionpublique.controllers;

import com.fonctionpublique.services.mail.MailService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
@Tag(name="mail")
public class MailController {

    @Autowired
    MailService mailService;

    @GetMapping("/reject-interne/{id}")
    public String sendMailRejectInterne(@PathVariable("id") Integer id){
        mailService.sendMailRejectInterne(id);
        return "Success";
    }

    @GetMapping("/reject-externe/{id}")
    public String sendMailRejectexterne(@PathVariable("id") Integer id){
        mailService.sendMailRejectExterne(id);
        return "Success";
    }

    @GetMapping("/approuve/{id}")
    public String sendMailApprouve(@PathVariable("id") Integer id){
        mailService.sendMailApprouvee(id);
        return "Success";
    }
}
