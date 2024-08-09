package com.fonctionpublique.controllers;

import com.fonctionpublique.dto.ProfileDTO;
import com.fonctionpublique.services.profile.ProfileServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin
@RestController
@Tag(name="profile")
@RequestMapping("/api/profile")
public class ProfileController {

}
