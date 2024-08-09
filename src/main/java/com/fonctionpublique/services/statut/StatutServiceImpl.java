package com.fonctionpublique.services.statut;

import com.fonctionpublique.entities.Demande;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.mailing.StatutMail;
import com.fonctionpublique.repository.DemandeRepository;
import com.fonctionpublique.repository.UtilisateurRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.FileNotFoundException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CrossOrigin
public class StatutServiceImpl implements StatutService {

    private final UtilisateurRepository utilisateurRepository;
    private final DemandeRepository demandeRepository;
    private final StatutMail statutMail;



    /**
     * send mail reject demande
     *
     * @param id
     * @return
     */
    @Override
    public Integer rejetedStatut(Integer id) {
        Optional<Demande> demande = demandeRepository.findById(id);
        if (!demande.isPresent()) {
            throw new EntityNotFoundException("NOT_FOUND");
        }
        statutMail.sentMailRejeted(demande.get().getDemandeur());
        demande.get().setStatut(StatusDemande.DEMANDE_REFUSEE.getStatut());
        demandeRepository.save(demande.get());
        return demande.get().getId();
    }

}
