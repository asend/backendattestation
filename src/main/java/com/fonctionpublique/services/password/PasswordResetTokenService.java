package com.fonctionpublique.services.password;

import com.fonctionpublique.access.password.PasswordResetToken;
import com.fonctionpublique.entities.Utilisateur;

import java.util.Optional;

public interface PasswordResetTokenService {
    Long createPasswordResetTokenForUser(String email);

}
