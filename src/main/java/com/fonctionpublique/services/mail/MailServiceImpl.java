package com.fonctionpublique.services.mail;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.entities.Params;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.services.ThymeleafService;
import com.fonctionpublique.services.demande.DemandeService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {
    @Autowired
    JavaMailSender mailSender;

    @Autowired
    DemandeService demandeService;

    @Autowired
    ThymeleafService thymeleafService;
    /*@Override
    public void sendMailTest() {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", "aliou ndiaye");
            variables.put("message", "message");

            helper.setFrom("alyndiaye.ag@gmail.com");
            helper.setText(thymeleafService.createContent("mail-sender-test.html", variables), true);
            helper.setCc("alyndiaye.ag@gmail.com");
            helper.setTo("alyndiaye.ag@gmail.com");
            helper.setSubject("attestation");//198800+37450+230000   //674100   //1140350 //750000

            mailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    @Override
    public void sendMailRejectInterne(Integer id) {
        DemandeDTO demandeDTO = demandeService.getById(id);
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", demandeDTO.getDemandeurDTO().getFullName());

            helper.setFrom(Params.EMAILSENDER);
            helper.setText(thymeleafService.createContent("mail-reject-interne.html", variables), true);
            //helper.setCc("alyndiaye.ag@gmail.com");
            helper.setTo(demandeDTO.getDemandeurDTO().getEmail());
            helper.setSubject("attestation");
            demandeDTO.setStatut(StatusDemande.DEMANDE_REFUSEE.getStatut());
            demandeService.update(demandeDTO);
            mailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void sendMailRejectExterne(Integer id) {
        DemandeDTO demandeDTO = demandeService.getById(id);
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", demandeDTO.getDemandeurDTO().getFullName());

            helper.setFrom(Params.EMAILSENDER);
            helper.setText(thymeleafService.createContent("mail-reject-externe.html", variables), true);
            //helper.setCc("alyndiaye.ag@gmail.com");
            helper.setTo(demandeDTO.getDemandeurDTO().getEmail());
            helper.setSubject("attestation");
            demandeDTO.setStatut(StatusDemande.DEMANDE_REFUSEE.getStatut());
            demandeService.update(demandeDTO);
            mailSender.send(message);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void sendMailApprouvee(Integer id) {
        DemandeDTO demandeDTO = demandeService.getById(id);
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(
                    message,
                    MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Map<String, Object> variables = new HashMap<>();
            variables.put("name", demandeDTO.getDemandeurDTO().getFullName());

            helper.setFrom(Params.EMAILSENDER);
            helper.setText(thymeleafService.createContent("mail-approuve.html", variables), true);
            //helper.setCc("alyndiaye.ag@gmail.com");
            helper.setTo(demandeDTO.getDemandeurDTO().getEmail());
            helper.setSubject("attestation");
            File file = new File(Params.DIRECTORYATTESTATION +"/" + demandeDTO.getUrlattestation());
            if (file.exists()) {
                FileSystemResource fileSystemResource = new FileSystemResource(file);
                helper.addAttachment("attestation_de_non_appartenance.pdf", fileSystemResource);
                mailSender.send(message);
                System.out.println("Mail with attachment sent successfully..");
            } else {
                System.out.println("File not found: " + file.getAbsolutePath());
                throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
