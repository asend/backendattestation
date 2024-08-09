package com.fonctionpublique.services.attestation;

import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Structure;
import com.fonctionpublique.entities.Utilisateur;
import com.google.zxing.WriterException;

import java.io.IOException;

public interface AttestationService {
//    void getAttestationPdf(String email) throws IOException;

//    void getAttestationPdf(Utilisateur utilisateur , Demandeur demandeur) throws IOException, WriterException;

    Integer getAttestationPdf(Utilisateur utilisateur , Demandeur demandeur, Demande demande, Structure structure) throws IOException, WriterException;
    Integer generatePdf(Integer idU, Integer idDemandeur, Integer idDemande, Integer idStructure ) throws IOException, WriterException;
}
