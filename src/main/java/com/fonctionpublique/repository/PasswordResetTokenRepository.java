package com.fonctionpublique.repository;

import com.fonctionpublique.access.password.PasswordResetToken;
import com.fonctionpublique.entities.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken,Long> {
    PasswordResetToken findByToken(String passwordResetToken);
    PasswordResetToken findUserByToken(String passwordResetToken);
}
