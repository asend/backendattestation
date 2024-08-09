package com.fonctionpublique.security;

import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtUtilisateur implements UserDetailsService {
    private final UtilisateurRepository utilisateurRepository;
    @Autowired
    public JwtUtilisateur(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email utilisateur non disponible : " + email));

        return new User(utilisateur.getEmail(), utilisateur.getPassword() , Collections.emptyList());
    }
}
