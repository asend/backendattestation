package com.fonctionpublique.dto;

import lombok.*;

@Builder
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TitreDTO {

    private int id;
    private String titre;
//    private  UtilisateurDTO utilisateurDTO;
}
