package com.fonctionpublique.mailing;

import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.Params;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.repository.UtilisateurRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class StatutMail {

    private final JavaMailSender javaMailSender;
    private final UtilisateurRepository utilisateurRepository;
    public void sentMailApprouved(Utilisateur u, Demande dm) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(dm.getDemandeur().getUtilisateur().getEmail());
            mimeMessageHelper.setSubject("Ministére de la fonction public et de la Réforme du Secteur Public.");
//            String url = "http://localhost:4200/setPassword/" + token;
            mimeMessageHelper.setText(
                    "<img height=\"auto\"\n" +
                            "src=\"http://res.cloudinary.com/dpdwdstqt/image/upload/v1524010315/senegalflag.png\"\n" +
                            "style=\"border:0;display:block;outline:none;text-decoration:none;height:auto;width:15%;margin-left: 25%\"\n" +
                            "width=\"15\">" +
                            "<h2>Bonjour</h2> " + ":" + dm.getDemandeur().getUtilisateur().getFullName() +
                            "<i style=\"font-size: .9rem\"> La réponse suite à votre demande: </i> <br> <br>\n" +
                            "<h5>Date de la demande</h5> :" +
                            "<h5>Object de la demande</h5> :" +
                            "<b>Votre demande a été validée.</b>" +
                            "<p>Voici en dessous votre attestation en fichier pdf.</p>" +
                            "<p>Cette attestation est egalement disponible dans votre espace personnel oú serez en mesure de l'utiliser tand qu'elle reste valid,</p>" +
                            "<a href=\"#\" target=\\\"_blank\\\">click pour donner votre avis</a>\"".formatted(dm.getDemandeur().getUtilisateur().getEmail()), true);

            File file = new File(Params.DIRECTORYATTESTATION +"/" + dm.getUrlattestation());
                if (file.exists()) {
                FileSystemResource fileSystemResource = new FileSystemResource(file);
                mimeMessageHelper.addAttachment("attestation_de_non_appartenance.pdf", fileSystemResource);
                javaMailSender.send(mimeMessage);
                System.out.println("Mail with attachment sent successfully..");
            } else {
                System.out.println("File not found: " + file.getAbsolutePath());
                throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
            }
            System.out.println("The mail is" + ":" + u.getEmail() + "The fullName is" + ":" + u.getFullName());
                System.out.println("the mail is" + ":" + u.getEmail() + "the fullName is" + ":" + u.getFullName());
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Unable to send email to " + u.getEmail());
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void sentMailRejeted(Demandeur demandeur) {
        System.out.println("the mail is:" + demandeur.getUtilisateur().getEmail() + "the fullName is:" + demandeur.getUtilisateur().getFullName());
        //Optional<Demandeur> optionalDemandeur = utilisateurRepository.findByEmail(utilisateur.getEmail());
        //System.out.println("the mail is:" + utilisateur.getEmail() + "the fullName is:" + optionalUtilisateur.get().getFullName());
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(demandeur.getUtilisateur().getEmail());
            mimeMessageHelper.setSubject("Ministère de la Fonction Publique et de la Réforme du Secteur Public");
            mimeMessageHelper.setText(
                        "<img height=\"auto\"\n" +
                                "src=\"http://res.cloudinary.com/dpdwdstqt/image/upload/v1524010315/senegalflag.png\"\n" +
                                "style=\"border:0;display:block;outline:none;text-decoration:none;height:auto;width:15%;margin-left: 25%\"\n" +
                                "width=\"15\" margin-left=5%>" +
                                "<h2 style=margin-left:25%><small>Bonjour</small>:"+ " " + demandeur.getUtilisateur().getFullName() +"</h2>"+"\n"+
                                "<h5 style=margin-left:25%>Merci de votre votre confiance.</h5>"+"\n"+
                                "<p style=margin-left:15%>Nous regretons de vous informer que votre demande a été rejetée.<br> Parceque vous-êtes connu(e) au fichier de la Fonction publique.</p><br>"+
                                "<p></p>".formatted(demandeur.getUtilisateur().getEmail()), true);
            javaMailSender.send(mimeMessage);
            System.out.println("the mail is:" + demandeur.getUtilisateur().getEmail() + "the fullName is:" + demandeur.getUtilisateur().getFullName());
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("mail no envoyé á " + demandeur.getUtilisateur().getEmail());
        }
    }

    public String genCode(){
        return UUID.randomUUID().toString();
    }
}
