package com.fonctionpublique.services.mail;

import com.fonctionpublique.dto.DemandeDTO;

public interface MailService {
    void sendMailRejectInterne(Integer id);
    void sendMailRejectExterne(Integer id);
    void sendMailApprouvee(Integer id);
}
