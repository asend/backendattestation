package com.fonctionpublique.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DemandeDTO {

    private int id;
    private String urlattestation;
    private String statut;
    private String numerodemande;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDateTime datedemande;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDateTime datetraitement;
    private String attestaionName;
    private boolean validite;
    private String objetdemande;
    private String descriptiondemande;
    private CertificationDTO certificationDTO;
    private DemandeurDTO demandeurDTO;
    @JsonFormat(pattern="dd-MM-yyyy")
    private LocalDate dateexpiration;

}
