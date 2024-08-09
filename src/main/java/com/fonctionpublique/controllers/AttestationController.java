package com.fonctionpublique.controllers;

import com.fonctionpublique.mailing.StatutMail;
import com.fonctionpublique.repository.*;
import com.fonctionpublique.services.attestation.AttestationServiceImpl;
import com.fonctionpublique.services.compteur.CompteurServiceImpl;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin
@RequestMapping("/api/attestation")
@RequiredArgsConstructor
@Tag(name = "attestation")
public class AttestationController {

    private final AttestationServiceImpl attestationServiceImpl;

    /**
     * Generate attestation and send mail to demandeur
     * @param idUser
     * @param idDemandeur
     * @param idDemande
     * @param idStructure
     * @return
     * @throws IOException
     * @throws WriterException
     */
    @GetMapping("/pdf_genere")
    public Integer generate(@RequestParam("idUser") int idUser, @RequestParam("idDemandeur") int idDemandeur, @RequestParam("idDemande") int idDemande, @RequestParam("idStructure") int idStructure) throws IOException, WriterException {
        return attestationServiceImpl.generatePdf(idUser, idDemandeur, idDemande, idStructure);
    }


}
