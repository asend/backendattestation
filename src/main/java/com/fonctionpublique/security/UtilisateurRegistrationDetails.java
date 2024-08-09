package com.fonctionpublique.security;

import com.fonctionpublique.entities.Utilisateur;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class UtilisateurRegistrationDetails implements UserDetails {

    private String email;
    private String password;
    private boolean isEnable;
    private List<GrantedAuthority> authorities;

//    public UtilisateurRegistrationDetails(Utilisateur user) {
//        this.email = user.getEmail();
//        this.password = user.getPassword();
////        this.isEnable = user.isEnable();
//        this.authorities = Arrays.stream(user.isStatut()
//                .split(","))
//                .map(SimpleGrantedAuthority::new).collect(Collectors.toList());
//    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
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
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled();
    }
}
