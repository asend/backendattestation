package com.fonctionpublique.access.password;

import lombok.Data;

@Data
public class PasswordRequest {

    private String email;
    private String oldPassword;
    private String newPassword;
    private String confirm;
}
