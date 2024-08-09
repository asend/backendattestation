package com.fonctionpublique.controllers;

import com.fonctionpublique.services.structure.StructureServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Tag(name="structure")
public class StructureController {

}
