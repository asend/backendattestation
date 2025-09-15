package com.fonctionpublique.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepartementDemandesDTO {

    private Integer departementId;
    private String nomDepartement;
    private Long totalDemandes;

    public DepartementDemandesDTO(String id, String nom, Long totalDemandes) {
    }
}
