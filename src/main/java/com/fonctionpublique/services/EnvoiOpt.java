package com.fonctionpublique.services;

import com.fonctionpublique.enumpackage.Constantes;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EnvoiOpt {

    @Async
    public void sendWhatsapp(String message, String phone) {
        System.out.println("############################ START WHATSAPP SEND #######################################");
                Constantes.sendNotificationWhatsapp(message, phone);
        System.out.println("############################ END WHATSAPP SEND #######################################");
    }

    @Async
    public void sendWhatsappNotification(String message, String phone) {
        System.out.println("############################ START WHATSAPP SEND #######################################");
        Constantes.sendNotificationWhatsapp(message, phone);
        System.out.println("############################ END WHATSAPP SEND #######################################");
    }

}
