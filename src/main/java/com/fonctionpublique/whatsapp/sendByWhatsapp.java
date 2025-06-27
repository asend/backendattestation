package com.fonctionpublique.whatsapp;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
@Service
public class sendByWhatsapp {

    @Async
    public void sendDocumentByWhatsapp(String telephone, String document) throws IOException {
        System.out.println("############################ START DOCUMENT WHATSAPP SEND #######################################");
        Constantes.sendDocumentByWhatsapp(telephone,document);
        System.out.println("############################ END DOCUMENT WHATSAPP SEND #######################################");
    }

}
