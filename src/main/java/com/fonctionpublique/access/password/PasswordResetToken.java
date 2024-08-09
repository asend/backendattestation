package com.fonctionpublique.access.password;

import com.fonctionpublique.entities.Utilisateur;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Password_Token")
public class PasswordResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long token_id;
    private String token;
    private Date expirationTime;
    private static final int EXPIRATION_TIME = 15;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private Utilisateur utilisateur;

    public PasswordResetToken(String token, Utilisateur user) {
        super();
        this.token = token;
        this.utilisateur = user;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public PasswordResetToken(String token) {
        super();
        this.token = token;
        this.expirationTime = this.getTokenExpirationTime();
    }

    public Date getTokenExpirationTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new Date().getTime());
        calendar.add(Calendar.MINUTE, EXPIRATION_TIME);
        return new Date(calendar.getTime().getTime());
    }
}
