package com.fonctionpublique.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDTO {

    private String to;
    private String subject;
    private String text;

}
