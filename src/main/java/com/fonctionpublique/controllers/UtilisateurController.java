package com.fonctionpublique.controllers;

import com.fonctionpublique.access.AuthenticationRequest;
import com.fonctionpublique.access.EmailRequest;
import com.fonctionpublique.access.RegistrationRequest;
import com.fonctionpublique.access.TokenPasswordRequest;
import com.fonctionpublique.access.password.PasswordRequest;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.services.password.PasswordResetTokenServiceImpl;
import com.fonctionpublique.services.utilisateur.AuthenticationResponse;
import com.fonctionpublique.services.utilisateur.UtilisateurServiceImpl;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/utilisateur")
@RequiredArgsConstructor
@Slf4j
@Tag(name="utilisateur")
public class UtilisateurController {

    private final UtilisateurServiceImpl utilisateurServiceImpl;
    private final PasswordResetTokenServiceImpl passwordResetTokenService;

    @GetMapping("/utilisateurDetails/{id}")
    public ResponseEntity<UtilisateurDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok(utilisateurServiceImpl.getById(id));
    }
    @GetMapping("/nin/{nin}")
    public ResponseEntity<UtilisateurDTO> getByNin(@PathVariable String nin) {
        return ResponseEntity.ok(utilisateurServiceImpl.getByNin(nin));
    }

    /**
     * register
     *
     * @param registrationRequest
     * @return
     * @throws IOException
     * @throws WriterException
     */
    @PostMapping("/register")
    public AuthenticationResponse registration(@RequestBody RegistrationRequest registrationRequest) throws IOException, WriterException {
        return utilisateurServiceImpl.registerUtilisateur(registrationRequest);
    }

    /**
     * Authentication
     *
     * @param registrationRequest
     * @return
     */
    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> authentication(@RequestBody AuthenticationRequest registrationRequest) {
        return ResponseEntity.ok(utilisateurServiceImpl.authenticate(registrationRequest));
    }

    /**
     * All users
     *
     * @return
     * @throws IOException
     * @throws WriterException
     */
    @GetMapping("/getUtilisateur")
    public List<UtilisateurDTO> findAll() throws IOException, WriterException {
        return utilisateurServiceImpl.findAll();
    }

    /**
     * Change password
     *
     * @param passwordRequest
     * @return
     */
    @PostMapping("/change-password")
    public Integer changePassword(@RequestBody PasswordRequest passwordRequest) {
        return utilisateurServiceImpl.changePassword(passwordRequest.getEmail(), passwordRequest.getOldPassword(), passwordRequest.getNewPassword(), passwordRequest.getConfirm());
    }


    /**
     * reset password
     *
     * @param tokenPasswordRequest
     * @return
     */
    @PostMapping("/reset-password")
    public Integer changePasswordTest(@RequestBody TokenPasswordRequest tokenPasswordRequest) {
        return utilisateurServiceImpl.resetPassword(tokenPasswordRequest.getToken(), tokenPasswordRequest.getNewPassword(), tokenPasswordRequest.getConfirmPassword());
    }


    /**
     * send token for reset password
     *
     * @param emailRequest
     * @return
     * @throws MessagingException
     * @throws UnsupportedEncodingException
     */
    @PostMapping("/password-reset-request")
    public Long resetPasswordRequest(@RequestBody EmailRequest emailRequest) {
        return passwordResetTokenService.createPasswordResetTokenForUser(emailRequest.getEmail());
    }

}
