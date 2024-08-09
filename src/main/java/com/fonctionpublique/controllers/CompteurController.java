package com.fonctionpublique.controllers;

import com.fonctionpublique.entities.Params;
import com.fonctionpublique.services.compteur.CompteurServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/compteur/")
@CrossOrigin
@RequiredArgsConstructor
@Tag(name="compteur")
public class CompteurController {

    private final CompteurServiceImpl compteurServiceImpl;

//    @PostConstruct
//    public void compteurTotalApprouved(){
//        compteurServiceImpl.compteurTotlaApprouved();
//    }

    @GetMapping("")
    public String test(){
        return String.valueOf(ClassLoader.getSystemResource("logominister.png"));
    }

}
