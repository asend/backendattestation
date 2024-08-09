package com.fonctionpublique.services.password;

import com.fonctionpublique.access.password.PasswordResetToken;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.exception.EmailNotExistException;
import com.fonctionpublique.mailing.PasswordMail;
import com.fonctionpublique.repository.PasswordResetTokenRepository;
import com.fonctionpublique.services.utilisateur.UtilisateurServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UtilisateurServiceImpl utilisateurService;
    private final PasswordMail passwordMail;

    @Override
    public Long createPasswordResetTokenForUser(String email) {
        Optional<Utilisateur> user = utilisateurService.findByEmail(email);// passwordResetTokenRepository. .findByEmail(emailRequest.getEmail());
        if (!user.isPresent()){
            throw new EmailNotExistException("EMAIL_NOT_EXIST");
        }
        String passwordResetToken = UUID.randomUUID().toString();

        PasswordResetToken passwordRestToken = new PasswordResetToken(passwordResetToken, user.get());
        passwordMail.sentSetPasswordEmail(email, passwordResetToken, user.get().getFullName());

        return passwordResetTokenRepository.save(passwordRestToken).getToken_id();
    }
}
