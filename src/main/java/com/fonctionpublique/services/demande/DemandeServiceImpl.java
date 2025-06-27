package com.fonctionpublique.services.demande;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.entities.*;
import com.fonctionpublique.enumpackage.Constantes;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.enumpackage.TypeDemande;
import com.fonctionpublique.repository.DemandeRepository;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.repository.ProfileRepository;
import com.fonctionpublique.repository.UtilisateurRepository;
import com.fonctionpublique.services.demandeur.DemandeurServiceImpl;
import com.fonctionpublique.services.mail.MailService;
import com.fonctionpublique.validators.ObjectValidator;
import com.google.zxing.WriterException;
import com.itextpdf.text.log.LoggerFactory;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@Service
@Component
@RequiredArgsConstructor
public class DemandeServiceImpl implements DemandeService {

    private final DemandeurRepository demandeurRepository;
    private final DemandeRepository demandeRepository;
    private final DemandeurServiceImpl demandeurService;
    private final ObjectValidator<DemandeDTO> validator;
    private final  ProfileRepository profileRepository;
    private final UtilisateurRepository utilisateurRepository;


    private final AtomicInteger compteur = new AtomicInteger(0);



    /**
     * List all demandes
     *
     * @return
     */
    @Override
    public List<DemandeDTO> findAll() {
        return demandeRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public DemandeDTO update(DemandeDTO demandeDTO) {
        return  convertToDTO(demandeRepository.save(convertToEntity(demandeDTO)));
    }

    /**
     * return a demande by id
     *
     * @param id
     * @return
     */
    @Override
    public DemandeDTO getById(int id) {
        Optional<Demande> demande = demandeRepository.findById(id);
        if (demande.isPresent()) {
            return convertToDTO(demande.get());
        }
        return null;
    }


    /**
     * get the expiration date
     * @return
     */
    public LocalDate isExpired(){
        LocalDate date = LocalDate.now();
        LocalDate expiredDate = date.plusMonths(9);
        return  expiredDate;
    }


    /**
     * create demande
     *
     * @param id
     * @return
     * @throws IOException
     * @throws WriterException
     */
    @Override
    @Transactional
    public Integer creerDemande(int id) {
        Optional<Demandeur> demandeur = demandeurRepository.findById(id);
        if (!demandeur.isPresent()) {
            throw new EntityNotFoundException("NOT_FOUND");
        }

        Demande demande = new Demande();
        demande.setDemandeur(demandeur.get());
        demande.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
        if ("cours".equalsIgnoreCase(demande.getStatut()) && demande.getTempsEcoule() == null) {
            demande.setTempsEcoule(LocalDateTime.now());
            demandeRepository.save(demande);
        }


        demande.setDatedemande(LocalDateTime.now());
        demande.setDatetraitement(LocalDateTime.now());
        demande.setValidite(true);
        demande.setObjetdemande(TypeDemande.DEMANDE_NON_APP.getStatut());
        demande.setDescriptiondemande("Description de la demande");
        demande.setDateexpiration(isExpired());


        // Sauvegarde de la demande
        demandeRepository.save(demande);
        //demandeurRepository.save(demandeur.get());

        // Incrémentation du compteur
        int count = compteur.incrementAndGet();
        System.out.println("Compteur actuel : " + count);


        // Réinitialisation du compteur lorsqu'il atteint 5
        if (count >= 1) {
            compteur.set(0);
            System.out.println("Compteur réinitialisé à 0");
        }
        // Sending notification if count == 1
        if (count == 1) {
            Optional<Profile> profileOpt = profileRepository.findByCode("traitant");

            if (profileOpt.isEmpty()) {
                System.out.println("Le profil TRAITANT n'existe pas.");
                //return 0;
            }

            Profile profilTraitant = profileOpt.get();

        // Récupérer tous les utilisateurs ayant ce profil
            List<Utilisateur> utilisateursTraitants = utilisateurRepository.findByProfil(profilTraitant);

            if (utilisateursTraitants.isEmpty()) {
                System.out.println("Aucun utilisateur avec le profil TRAITANT trouvé.");
                //return 0;
            }

            // Envoyer les notifications en parallèle
            utilisateursTraitants.parallelStream().forEach(utilisateur -> {
                System.out.println("Envoi de notification à : " + utilisateur.getTelephone());
                Constantes.sendNotificationWhatsapp("Vous avez une nouvelle demande en attente de traitement." + demande.getId() + LocalDateTime.now(), utilisateur.getTelephone());
            });
        }

        return demande.getId();
    }






//    public Integer creerDemande(int id) {
//
//        Optional<Demandeur> demandeur = demandeurRepository.findById(id);
//        if (!demandeur.isPresent()) {
//            throw new EntityNotFoundException("NOT_FOUND");
//        }
//
//        Demande demande = new Demande();
//        demande.setDemandeur(demandeur.get());
//        demande.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
//        demande.setDatedemande(LocalDateTime.now());
//        demande.setDatetraitement(LocalDateTime.now());
//        demande.setValidite(true);
//        demande.setObjetdemande(TypeDemande.DEMANDE_NON_APP.getStatut());
//        demande.setDescriptiondemande("Description de la demande");
//        demandeur.get().setDemande(demande.getDemandeur().getDemande());
//        demande.setDateexpiration(isExpired());
//        demandeurRepository.save(demandeur.get());
//        return demandeRepository.save(demande).getId();
//    }






    /**
     * Convert entity demande to dto demande
     *
     * @param demande
     * @return
     */

    public  DemandeDTO convertToDTO(Demande demande) {
        return DemandeDTO.builder()
                .validite(demande.isValide())
                .demandeurDTO(demandeurService.convertToDTO(demande.getDemandeur()))
                .datedemande(demande.getDatedemande())
                .datetraitement(demande.getDatetraitement())
                .descriptiondemande(demande.getDescriptiondemande())
                .id(demande.getId())
                .numerodemande(demande.getNumerodemande())
                .objetdemande(demande.getObjetdemande())
                .statut(demande.getStatut())
                .urlattestation(demande.getUrlattestation())
                .attestationName(demande.getAttestationName())
                .dateexpiration(demande.getDateexpiration())
                .motifrejet(demande.getMotifrejet())
                .build();
    }

    /**
     * Convert dto demande to entity demande
     *
     * @param demande
     * @return
     */
    public Demande convertToEntity(DemandeDTO demande) {
        return Demande.builder()
                .validite(demande.isValidite())
                .datedemande(demande.getDatedemande())
                .datetraitement(demande.getDatetraitement())
                .descriptiondemande(demande.getDescriptiondemande())
                .id(demande.getId())
                .numerodemande(demande.getNumerodemande())
                .objetdemande(demande.getObjetdemande())
                .statut(demande.getStatut())
                .urlattestation(demande.getUrlattestation())
                .attestationName(demande.getAttestationName())
                .motifrejet(demande.getMotifrejet())
                .demandeur(Demandeur.builder()
                        .id(demande.getDemandeurDTO().getId())
                        .build())
                .build();
    }

    /**
     * liste of demandes by status
     *
     * @param status
     * @return
     */

    @Override
    public List<DemandeDTO> findByStatut(String status) {
        return demandeRepository.findByStatut(status).stream().map(this::convertToDTO).collect(Collectors.toList());
    }



    /**
     * get demande by Id demandeur
     *
     * @param id
     * @return
     */
    @Override
    public List<DemandeDTO> findByDemandeurId(int id) {
        return demandeRepository.findByDemandeurId(id).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Map<String, List<DemandeDTO>> findByDemandeurIdByStatut(int id) {
        Map<String, List<DemandeDTO>> list  = new HashMap<>();
        List<DemandeDTO> ldec = new ArrayList<>();
        List<DemandeDTO> ldr = new ArrayList<>();
        List<DemandeDTO> lda = new ArrayList<>();
        list.put("DEC", ldec);
        list.put("DR", ldr);
        list.put("DA", lda);
        for (DemandeDTO d : demandeRepository.findByDemandeurId(id).stream().map(this::convertToDTO).collect(Collectors.toList())) {
            if (d.getStatut().equals("cours")){
                ldec.add(d);
                list.put("DEC", ldec);
            }
            if (d.getStatut().equals("rejetée")){
                ldr.add(d);
                list.put("DR", ldr);
            }
            if (d.getStatut().equals("approuvée")){
                lda.add(d);
                list.put("DA", lda);
            }
        }
        return list;
    }

    @Override
    public String findAttestation(int id) {
        return demandeRepository.findAttestationById(id).getUrlattestation();
    }

    @Override
    public Map<String,Integer> demandeByStatut(){
        List<Demande> demandeDTOS = demandeRepository.findAll();
        Map<String, Integer> list = new HashMap<>();
        AtomicReference<Integer> encours = new AtomicReference<>(0);
        AtomicReference<Integer> rejetees = new AtomicReference<>(0);
        AtomicReference<Integer> approuvees = new AtomicReference<>(0);
        demandeDTOS.forEach(d->{
            if (d.getStatut()=="cours"){
                encours.getAndSet(encours.get() + 1);
            }
            if(d.getStatut()=="approuvée"){
                approuvees.getAndSet(approuvees.get() + 1);
            }if (d.getStatut()=="rejetée"){
                rejetees.getAndSet(rejetees.get() + 1);
            }
        });
        list.put("cours",encours.get());
        list.put("approuvée", approuvees.get());
        list.put("rejetée", rejetees.get());
        list.put("total", demandeDTOS.size());
        return list;
    }

    @Override
    public List<Demande> demandeByDemandeurStatut(Integer id) {
        return demandeRepository.demandeByDemandeurStatut(id);
    }

    @Override
    public Demande getByCode(String code){
        return demandeRepository.findByAttestationName(code);
    }

    @Override
    public List<DemandeDTO> demandeActif() {
        return demandeRepository.demandeActif().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public DemandeDTO annuler(Integer id){
        Demande demandeDTO = demandeRepository.findById(id).orElse(null);
        demandeDTO.setStatut(StatusDemande.DEMANDE_SUPPRIME.getStatut());
        System.out.println("########################### ici ici demandeDTO "+demandeDTO.getId());
        System.out.println("########################### ici ici "+StatusDemande.DEMANDE_SUPPRIME.getStatut());
        return convertToDTO(demandeRepository.save(demandeDTO));
    }


    public Integer updateMotifRejetDemande(Integer id, DemandeDTO demandeDTO) {
        Optional<Demande> demandeOptional = demandeRepository.findById(id);
        if (demandeOptional.isPresent()) {
            Demande demand = demandeOptional.get();
            demand.setMotifrejet(demandeDTO.getMotifrejet());
            demand.setStatut(StatusDemande.DEMANDE_REFUSEE.getStatut());
            Constantes.sendNotificationWhatsapp(demand.getMotifrejet(), demandeOptional.get().getDemandeur().getTelephone());
//            mailService.sendMailRejectInterne(id);
            demandeRepository.save(demand);
            return demand.getId();
        }
        return demandeOptional.get().getId(); // Ou lever une exception
    }


}
