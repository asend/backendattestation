package com.fonctionpublique.access;

import lombok.Getter;

@Getter
public class TokenPasswordRequest {
    private String token;
    private String newPassword;
    private String confirmPassword;
}
