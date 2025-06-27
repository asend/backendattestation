//package com.fonctionpublique.services.schedule;
//
//import com.fonctionpublique.entities.Demande;
//import com.fonctionpublique.repository.DemandeRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@RequiredArgsConstructor
//public class surveillerDemandesEnCours {
//
//    private final DemandeRepository demandeRepository;
//    @Scheduled(fixedRate = 1000)// toutes les 60 secondes
//    public void surveillerDemandesEnCours() {
//        List<Demande> enCours = demandeRepository.findByStatut("cours");
//
//        for (Demande d : enCours) {
//            if (d.getTempsEcoule() != null) {
//                Duration ecoule = Duration.between(d.getTempsEcoule(), LocalDateTime.now());
//                long minutes = ecoule.toMinutes();
//
//                System.out.println("Demande ID " + d.getId() + " en cours depuis : " + minutes + " minutes");
//
//                // Exemple : alerte si plus de 60 minutes
//                if (minutes > 60) {
//                    // envoyer une notification ou mettre à jour le statut
//                }
//            }
//        }
//    }
//
//}
