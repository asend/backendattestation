package com.fonctionpublique.services.certification;

import com.fonctionpublique.entities.Certification;
import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Utilisateur;
import com.google.zxing.WriterException;

import java.io.IOException;

public interface CertificationService {

    Certification qRCode(Utilisateur utilisateur, Demande demande) throws WriterException, IOException;
    String generateAttestationNumber(int id);
}
