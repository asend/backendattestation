package com.fonctionpublique.controllers;

import com.fonctionpublique.access.AuthenticationRequest;
import com.fonctionpublique.access.EmailRequest;
import com.fonctionpublique.access.RegistrationRequest;
import com.fonctionpublique.access.TokenPasswordRequest;
import com.fonctionpublique.access.password.PasswordRequest;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.repository.UtilisateurRepository;
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
    private final UtilisateurRepository utilisateurRepository;

    @GetMapping("/utilisateurDetails/{id}")
    public ResponseEntity<UtilisateurDTO> getById(@PathVariable int id) {
        return ResponseEntity.ok(utilisateurServiceImpl.getById(id));
    }
    @GetMapping("/{id}")
    public ResponseEntity<Integer> getUserId(@PathVariable int id) {
        return utilisateurRepository.findById(id)
                .map(utilisateur -> ResponseEntity.ok(utilisateur.getId())) // Return user ID
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 if not found
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
    @PostMapping("/register/traitant")
    public ResponseEntity<Integer> registeTraitant(@RequestBody RegistrationRequest registrationRequest) throws IOException, WriterException {
        return ResponseEntity.ok(utilisateurServiceImpl.registerTraitant(registrationRequest));
    }

    @PostMapping("/register/visionnaire")
    public ResponseEntity<Integer> registeVisionnaire(@RequestBody RegistrationRequest registrationRequest) throws IOException, WriterException {
        return ResponseEntity.ok(utilisateurServiceImpl.registerVisionnage(registrationRequest));
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

    @GetMapping("/getUtilisateurProfile/{code}")
    public ResponseEntity<List<UtilisateurDTO>> getByProfileCode(@PathVariable String code) {
        try {
            List<UtilisateurDTO> utilisateurs = utilisateurServiceImpl.getByCode(code);
            return ResponseEntity.ok(utilisateurs);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @PutMapping("/update-traitant/{id}")
    public ResponseEntity<Integer> updateTraitant(@PathVariable("id") int utilisateurId, @RequestBody UtilisateurDTO utilisateurDTO) {
        Integer updatedUtilisateurId = utilisateurServiceImpl.updateTraitant(utilisateurId, utilisateurDTO);
        return ResponseEntity.ok(updatedUtilisateurId);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTraitant(@PathVariable int id) {
        utilisateurServiceImpl.deleteTraitant(id);
        return ResponseEntity.noContent().build();
    }



}
