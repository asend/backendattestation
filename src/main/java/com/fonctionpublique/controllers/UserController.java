package com.fonctionpublique.controllers;


import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.User;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.repository.UserRepository;
import com.fonctionpublique.services.mail.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@CrossOrigin
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DemandeurRepository demandeurRepository;
    @Autowired
    private MailService mailService;

    @PostMapping("/{id}")
    public Integer createNewUser(@PathVariable("id") int id, @RequestPart("file") MultipartFile file) throws IOException {
        Demandeur demandeur= demandeurRepository.findById(id).orElse(null); //User.builder().userName(name).displayPicture(file.getBytes()).build();
        demandeur.setDisplayPicture(file.getBytes());
        demandeur.setType(file.getContentType());
        demandeurRepository.save(demandeur);
        return  demandeur.getId();
    }
@PostMapping
public ResponseEntity<User> createNewUser(@RequestPart("file") MultipartFile file) throws IOException {
    User user= User.builder().displayPicture(file.getBytes()).build();
    userRepository.save(user);
    user.setDisplayPicture(null);
    return  ResponseEntity.ok(user);
}

    @GetMapping
    public  ResponseEntity<List<User>> getAllUser(){
        List<User> userList = userRepository.findAll();
        return  ResponseEntity.ok(userList);
    }

    @GetMapping("/{id}")
    public byte[] findById(@PathVariable int id){
        Optional<User> userList = userRepository.findById(id);
        return userList.get().getDisplayPicture();
    }

    @GetMapping("/send-test")
    public String sendMailTest(){
        //mailService.sendMailTest();
        return "Success";
    }


}

