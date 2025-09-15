package com.fonctionpublique.services.utilisateur;

import com.fonctionpublique.entities.FileUpload;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private String token;
    private int userId;
    private String email;
    private String password;
    private String nin;
    private String fullName;
    private String profile;
    private int demandeurId;
    private int demandeId;
    private String message;
    private String name;
    private String type;
    private long taille;
    private Long fileUploadId;
}
