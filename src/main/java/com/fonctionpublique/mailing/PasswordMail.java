package com.fonctionpublique.mailing;

import com.fonctionpublique.entities.Params;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.apache.coyote.http11.Constants.a;
@Service
public class PasswordMail {

    private final JavaMailSender javaMailSender;
    @Autowired
    public PasswordMail(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

public void sentSetPasswordEmail(String email) {
    try {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Set Password");
        String url = "http://localhost:4200/setPassword/" + email;
        mimeMessageHelper.setText(
                   " <div>" +
                      "  <a href="+url+" target=\"_blank\">click pour modifier votre mot de passe</a>" +
                   " </div>"
                    .formatted(email), true);

        javaMailSender.send(mimeMessage);
    } catch (MessagingException e) {
        e.printStackTrace();
        throw new RuntimeException("Unable to send email to " + email);
    }
}
public void sentSetPasswordEmail(String email, String token, String fullName) {
    try {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject("Set Password");
        String url = Params.LIENRESTPASSWORD + token;
        mimeMessageHelper.setText(
                   " <div> Bonjour " +fullName+
                      "  <a href="+url+" target=\"_blank\">click pour modifier votre mot de passe</a>" +
                   " </div>"
                    .formatted(email), true);

        javaMailSender.send(mimeMessage);
    } catch (MessagingException e) {
        e.printStackTrace();
        throw new RuntimeException("Unable to send email to " + email);
    }
}
}
