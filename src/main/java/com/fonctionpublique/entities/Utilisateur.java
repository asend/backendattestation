package com.fonctionpublique.entities;

import com.fonctionpublique.access.password.PasswordResetToken;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Utilisateur implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NonNull
    @NotBlank
    private String prenom;
    @NonNull
    @NotBlank
    private String nom;
    @Column(unique = true)
    private String email;
    private String password;
    private String typePieces;
    private String nin;
    private String telephone;
    private String passPort;
    private boolean statut;
    private String signature;
    private String titre;
    private String matriculeSolde;

    @OneToOne
    private Demandeur demandeur;
    @OneToMany(mappedBy = "utilisateur")
    private List<Demande> demande;
    @ManyToOne
    @JoinColumn(name = "profile_id")
    private Profile profile;
    @OneToMany(mappedBy = "utilisateur")
    private List<PasswordResetToken> passwordResetTokens;
    public String getFullName() {
        return prenom + " " + nom;
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(profile.getCode()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return statut;
    }

}
