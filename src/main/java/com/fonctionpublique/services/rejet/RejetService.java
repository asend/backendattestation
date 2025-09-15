package com.fonctionpublique.services.rejet;

import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Structure;
import com.fonctionpublique.entities.Utilisateur;
import com.google.zxing.WriterException;

import java.io.IOException;

public interface RejetService {
    Integer getRejetPdf(Utilisateur utilisateur, Demandeur demandeur, Demande demande, Structure structure) throws IOException, WriterException;

    Integer generatePdfRejet(Integer idU, Integer idDemandeur, Integer idDemande, Integer idStructure) throws IOException, WriterException;
}
