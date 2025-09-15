package com.fonctionpublique.controllers;

import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.mapdatadocumentgenerator.DataMapDocumentGenerator;
import com.fonctionpublique.services.demandeur.DemandeurServiceImpl;
import com.fonctionpublique.services.documentgenerator.DocumentGeneratorServiceImpl;
import com.fonctionpublique.services.utilisateur.UtilisateurServiceImpl;
import com.google.zxing.WriterException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/thymeleaf")
public class DocumentGeneratorController {

    @Autowired
    private DocumentGeneratorServiceImpl documentGenerator;

    @Autowired
    private UtilisateurServiceImpl demandeurService;


    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    @Autowired
    private DataMapDocumentGenerator dataMapper;

    @PostMapping("/generate/document")
    public String generateDocument() throws IOException, WriterException {
        List<UtilisateurDTO> employeeList = demandeurService.findAll();

        if (employeeList == null || employeeList.isEmpty()) {
            throw new IllegalArgumentException("Aucun demandeur trouvé en base de données");
        }

        Context dataContext = dataMapper.setData(employeeList);
        String finalHtml = springTemplateEngine.process("template", dataContext);
        documentGenerator.htmlToPdf(finalHtml);
        return "Success";
    }

}
