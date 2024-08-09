package com.fonctionpublique.services.utilisateur;

import com.fonctionpublique.access.AuthenticationRequest;
import com.fonctionpublique.access.RegistrationRequest;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.entities.Utilisateur;
import com.google.zxing.WriterException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UtilisateurService {

    UtilisateurDTO getByNin(String nin);

    List<UtilisateurDTO> findAll() throws IOException, WriterException;
    UtilisateurDTO getById(int id);
    AuthenticationResponse registerUtilisateur(RegistrationRequest registrationRequest) throws IOException, WriterException;
    AuthenticationResponse authenticate(AuthenticationRequest registrationRequest);
    Integer changePassword(String email, String oldPassword, String newPassword, String confirm);
    boolean oldPasswordIsValid(Utilisateur utilisateur, String oldPassword);
    Optional<Utilisateur> findByEmail(String email);
    Integer resetPassword(String token, String newPassword, String confirmPassword);
}
