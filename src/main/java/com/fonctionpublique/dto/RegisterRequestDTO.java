package com.fonctionpublique.dto;

import com.fonctionpublique.access.RegistrationRequest;
import lombok.Data;

@Data
public class RegisterRequestDTO {
    private RegistrationRequest registrationRequest;
    private DemandeurDTO demandeurDTO;
}
