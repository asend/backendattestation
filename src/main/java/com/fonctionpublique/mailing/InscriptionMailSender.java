package com.fonctionpublique.mailing;

import com.fonctionpublique.entities.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class InscriptionMailSender {

    private final InscriptionMail inscriptionMail;
    //private final UtilisateurFilter utilisateurFilter;
    @Autowired
    public InscriptionMailSender(InscriptionMail inscriptionMail ) {
        this.inscriptionMail = inscriptionMail;
    }

    public void sendMailToAllAdmin(boolean status, Utilisateur user){
       // allAdmin.remove(utilisateurFilter.getCurrentUser());
        String s = "ndongmafale10@gmail.com";
        List<String> allAdmins = new ArrayList<>();
        allAdmins.add(s);//["ndongmafale10@gmail.com"]
        if(status){
            inscriptionMail.sendSimpleMessage(
                    user.getEmail(),
                    "Bonjour Mr/Mme,\n" + user.getFullName() +
                            "Votre inscription à la plateforme de demande d'attestation de non-appartenance à la fonctin publique \n" +
                            "a été éffectuée avec success.\n" +
                            "Veillez vous connectez grâce á ce lien: \n" +
                            ""
                            + user.getFullName() + " " + " et mot de passe pour continuer.",
                    "User:- " + user + "\n Test the mailing application work if you received this mail, thanks ! \n Admin:- " + user.getEmail());
        }else{
            inscriptionMail.sendSimpleMessage(
                    user.getEmail(),
                    "Account Disable",
                    "User:- " + user.getFullName() + "\n is disable by \n Admin:- " + user.getEmail());
        }

    }

}
