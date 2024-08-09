package com.fonctionpublique.event;

import com.fonctionpublique.entities.Utilisateur;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationCompleteEvent extends ApplicationEvent {

    private Utilisateur user;
    private String applicationUrl;

    public RegistrationCompleteEvent(Utilisateur user, String applicationUrl) {
        super(user);
        this.user = user;
        this.applicationUrl = applicationUrl;
    }
}
