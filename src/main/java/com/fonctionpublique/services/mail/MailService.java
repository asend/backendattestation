package com.fonctionpublique.services.mail;

public interface MailService {
    Integer sendMailRejectInterne(Integer id);
    Integer sendMailRejectExterne(Integer id);
    Integer sendMailApprouvee(Integer id);

    Integer sendMailPdfRejet(Integer id);
}
