package com.fonctionpublique.mailing;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class InscriptionMail {

    private  final  JavaMailSender emailSender;
    private String userName;

    @Autowired
    public InscriptionMail(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    public String sendSimpleMessage(String subject, String text, String s) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("ndongmafale10@gmail.com");

        String to = getCurrentUser();

        if (to != null && isValidEmailAddress(to)) {
            message.setTo(to);
        } else {
            return "fallback@example.com";
        }

        message.setSubject("Activation de Compte Pour l'Access á la Plateforme De Demandes D'Attestations");
        message.setText(text);

        try {
            emailSender.send(message);
            System.out.println("Mail sent successfully ...");
        } catch (Exception e) {
            throw new MailSendException("Failed to send email: " + e.getMessage(), e);
        }
        return to;
    }

    public String getCurrentUser() {
        if (userName != null) {
            return userName;
        } else {
            return "ndongmafale10@gmail.com";
        }
    }

    public boolean isValidEmailAddress(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public void forgetMail(String to, String subject, String password) throws MessagingException {
        MimeMessage message = emailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,true);
        helper.setFrom("ndongmafale10@gmail.com");
        helper.setTo(to);
        helper.setSubject(subject);
        String htmlMsg = "<p><b> Your Login details for fonction publique access account</b><br><b>Email: </b> " + to +
                " <br><b>Password: </b> " + password + "<br>< a href=\"http://localhost:4200/\">Click here to login</a></p>";
        message.setContent(htmlMsg,"test/html");
        emailSender.send(message);
    }

}
