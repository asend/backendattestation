package com.fonctionpublique.services.demande;

import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.dto.DepartementDemandesDTO;
import com.fonctionpublique.entities.*;
import com.fonctionpublique.enumpackage.Constantes;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.enumpackage.TypeDemande;
import com.fonctionpublique.repository.*;
import com.fonctionpublique.services.demandeur.DemandeurServiceImpl;
import com.fonctionpublique.validators.ObjectValidator;
import com.google.zxing.WriterException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
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
    private final DepartementRepository departementRepository;
    private  final RegionRepository regionRepository;

    private  final CertificationRepository certificationRepository;


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

//    public Page<DemandeDTO> findAll(Pageable pageable) {
//        return demandeRepository.findAll(pageable).map(this::convertToDTO);
//    }

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
        Optional<Demandeur> optionalDemandeur = demandeurRepository.findById(id);
        if (optionalDemandeur.isEmpty()) {
            throw new EntityNotFoundException("Demandeur introuvable");
        }

        Demandeur demandeur = optionalDemandeur.get();

        // Recherche si une demande avec statut "supprimé" existe pour ce demandeur
        Optional<Demande> demandeSupprimeeOpt = demandeur.getDemande() == null
                ? Optional.empty()
                : demandeur.getDemande().stream()
                .filter(d -> StatusDemande.DEMANDE_SUPPRIME.getStatut().equalsIgnoreCase(d.getStatut()))
                .findFirst();

        if (demandeSupprimeeOpt.isPresent()) {
            // On réactive la demande supprimée au lieu d'en créer une nouvelle
            Demande demande = demandeSupprimeeOpt.get();
            demande.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());

            if ("cours".equalsIgnoreCase(demande.getStatut()) && demande.getTempsEcoule() == null) {
                demande.setTempsEcoule(LocalDateTime.now());
            }

            demande.setDatedemande(LocalDateTime.now());
            demande.setDatetraitement(LocalDateTime.now());
            demande.setValidite(true);
            demande.setObjetdemande(TypeDemande.DEMANDE_NON_APP.getStatut());
            demande.setDescriptiondemande("Description de la demande");
            demande.setDateexpiration(isExpired());

            demandeRepository.save(demande);
            demandeurRepository.save(demandeur);

            // Logique compteur et notification (à conserver)
            int count = compteur.incrementAndGet();
            System.out.println("Compteur actuel : " + count);

            if (count >= 1) {
                compteur.set(0);
                System.out.println("Compteur réinitialisé à 0");
            }

            if (count == 1) {
                Optional<Profile> profileOpt = profileRepository.findByCode("traitant");
                if (profileOpt.isPresent()) {
                    List<Utilisateur> utilisateursTraitants = utilisateurRepository.findByProfil(profileOpt.get());

                    utilisateursTraitants.parallelStream().forEach(utilisateur -> {
                        System.out.println("Envoi de notification à : " + utilisateur.getTelephone());
                        Constantes.sendNotificationWhatsapp(
                                "Vous avez une nouvelle demande en attente de traitement. ID: " + demande.getId() + " - " + LocalDateTime.now(),
                                utilisateur.getTelephone()
                        );
                    });
                } else {
                    System.out.println("Le profil TRAITANT n'existe pas.");
                }
            }

            return demande.getId();

        } else {
            // Pas de demande supprimée trouvée, on crée une nouvelle demande normalement
            Demande demande = new Demande();
            demande.setDemandeur(demandeur);
            demande.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());

            if ("cours".equalsIgnoreCase(demande.getStatut()) && demande.getTempsEcoule() == null) {
                demande.setTempsEcoule(LocalDateTime.now());
            }

            demande.setDatedemande(LocalDateTime.now());
            demande.setDatetraitement(LocalDateTime.now());
            demande.setValidite(true);
            demande.setObjetdemande(TypeDemande.DEMANDE_NON_APP.getStatut());
            demande.setDescriptiondemande("Description de la demande");
            demande.setDateexpiration(isExpired());

            demandeRepository.save(demande);

            if (demandeur.getDemande() == null) {
                demandeur.setDemande(new ArrayList<>());
            }
            demandeur.getDemande().add(demande);

            demandeurRepository.save(demandeur);

            // Logique compteur et notification (même logique)
            int count = compteur.incrementAndGet();
            System.out.println("Compteur actuel : " + count);

            if (count >= 1) {
                compteur.set(0);
                System.out.println("Compteur réinitialisé à 0");
            }

            if (count == 1) {
                Optional<Profile> profileOpt = profileRepository.findByCode("traitant");
                if (profileOpt.isPresent()) {
                    List<Utilisateur> utilisateursTraitants = utilisateurRepository.findByProfil(profileOpt.get());

                    utilisateursTraitants.parallelStream().forEach(utilisateur -> {
                        System.out.println("Envoi de notification à : " + utilisateur.getTelephone());
                        Constantes.sendNotificationWhatsapp(
                                "Vous avez une nouvelle demande en attente de traitement. ID: " + demande.getId() + " - " + LocalDateTime.now(),
                                utilisateur.getTelephone()
                        );
                    });
                } else {
                    System.out.println("Le profil TRAITANT n'existe pas.");
                }
            }

            return demande.getId();
        }
    }

//    @Override
//    @Transactional
//    public Integer creerDemande(int id) {
//        Optional<Demandeur> demandeur = demandeurRepository.findById(id);
//        if (!demandeur.isPresent()) {
//            throw new EntityNotFoundException("NOT_FOUND");
//        }
//
//        Demande demande = new Demande();
//        demande.setDemandeur(demandeur.get());
//        demande.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
//        if ("cours".equalsIgnoreCase(demande.getStatut()) && demande.getTempsEcoule() == null) {
//            demande.setTempsEcoule(LocalDateTime.now());
//            demandeRepository.save(demande);
//        }
//
//
//        demande.setDatedemande(LocalDateTime.now());
//        demande.setDatetraitement(LocalDateTime.now());
//        demande.setValidite(true);
//        demande.setObjetdemande(TypeDemande.DEMANDE_NON_APP.getStatut());
//        demande.setDescriptiondemande("Description de la demande");
//        demande.setDateexpiration(isExpired());
//
//
//        // Sauvegarde de la demande
//        demandeRepository.save(demande);
//        //demandeurRepository.save(demandeur.get());
//
//        // Incrémentation du compteur
//        int count = compteur.incrementAndGet();
//        System.out.println("Compteur actuel : " + count);
//
//
//        // Réinitialisation du compteur lorsqu'il atteint 5
//        if (count >= 1) {
//            compteur.set(0);
//            System.out.println("Compteur réinitialisé à 0");
//        }
//        // Sending notification if count == 1
//        if (count == 1) {
//            Optional<Profile> profileOpt = profileRepository.findByCode("traitant");
//
//            if (profileOpt.isEmpty()) {
//                System.out.println("Le profil TRAITANT n'existe pas.");
//                //return 0;
//            }
//
//            Profile profilTraitant = profileOpt.get();
//
//        // Récupérer tous les utilisateurs ayant ce profil
//            List<Utilisateur> utilisateursTraitants = utilisateurRepository.findByProfil(profilTraitant);
//
//            if (utilisateursTraitants.isEmpty()) {
//                System.out.println("Aucun utilisateur avec le profil TRAITANT trouvé.");
//                //return 0;
//            }
//
//            // Envoyer les notifications en parallèle
//                utilisateursTraitants.parallelStream().forEach(utilisateur -> {
//                System.out.println("Envoi de notification à : " + utilisateur.getTelephone());
//                Constantes.sendNotificationWhatsapp("Vous avez une nouvelle demande en attente de traitement." + demande.getId() + LocalDateTime.now(), utilisateur.getTelephone());
//            });
//        }
//
//        return demande.getId();
//    }
//    @Override
//    @Transactional
//    public Integer creerDemande(int id) {
//        Optional<Demandeur> optionalDemandeur = demandeurRepository.findById(id);
//        if (optionalDemandeur.isEmpty()) {
//            throw new EntityNotFoundException("Demandeur introuvable");
//        }
//
//        Demandeur demandeur = optionalDemandeur.get();
//
//        Demande demande = new Demande();
//        demande.setDemandeur(demandeur);
//        demande.setStatut(StatusDemande.DEMANDE_EN_COURS.getStatut());
//
//        if ("cours".equalsIgnoreCase(demande.getStatut()) && demande.getTempsEcoule() == null) {
//            demande.setTempsEcoule(LocalDateTime.now());
//        }
//
//        demande.setDatedemande(LocalDateTime.now());
//        demande.setDatetraitement(LocalDateTime.now());
//        demande.setValidite(true);
//        demande.setObjetdemande(TypeDemande.DEMANDE_NON_APP.getStatut());
//        demande.setDescriptiondemande("Description de la demande");
//        demande.setDateexpiration(isExpired());
//
//        // Sauvegarder la demande (cela génère son ID)
//        demandeRepository.save(demande);
//
//        // Ajouter la demande à la liste des demandes du demandeur
//        if (demandeur.getDemande() == null) {
//            demandeur.setDemande(new ArrayList<>());
//        }
//        demandeur.getDemande().add(demande);
//
//        // Sauvegarder le demandeur avec la liste mise à jour
//        demandeurRepository.save(demandeur);
//
//        // --- LOGIQUE DE COMPTEUR ET NOTIFICATIONS ---
//        int count = compteur.incrementAndGet();
//        System.out.println("Compteur actuel : " + count);
//
//        if (count >= 1) {
//            compteur.set(0);
//            System.out.println("Compteur réinitialisé à 0");
//        }
//
//        if (count == 1) {
//            Optional<Profile> profileOpt = profileRepository.findByCode("traitant");
//            if (profileOpt.isPresent()) {
//                List<Utilisateur> utilisateursTraitants = utilisateurRepository.findByProfil(profileOpt.get());
//
//                utilisateursTraitants.parallelStream().forEach(utilisateur -> {
//                    System.out.println("Envoi de notification à : " + utilisateur.getTelephone());
//                    Constantes.sendNotificationWhatsapp(
//                            "Vous avez une nouvelle demande en attente de traitement. ID: " + demande.getId() + " - " + LocalDateTime.now(),
//                            utilisateur.getTelephone()
//                    );
//                });
//            } else {
//                System.out.println("Le profil TRAITANT n'existe pas.");
//            }
//        }
//
//        return demande.getId();
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
                .dureeTraitement(demande.getDureeTraitement())
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
                .dureeTraitement(demande.getDureeTraitement())
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


   // @Override
   public int countDemandeByStatut(String status) {
       long count = demandeRepository.findByStatut(status).stream()
               .filter(demande -> !"SUPPRIMER".equalsIgnoreCase(demande.getStatut()))
               .count();
       return (int) count;
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
    //@Override
    public Demande getByGenre(String sexe){
        return  demandeRepository.findBySexe(sexe);

    }

    @Override
    public List<DemandeDTO> demandeActif() {
        return demandeRepository.demandeActif().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

//    public DemandeDTO annuler(Integer id){
//        Demande demandeDTO = demandeRepository.findById(id).orElse(null);
//        demandeDTO.setStatut(StatusDemande.DEMANDE_SUPPRIME.getStatut());
//        System.out.println("########################### ici ici demandeDTO "+demandeDTO.getId());
//        System.out.println("########################### ici ici "+StatusDemande.DEMANDE_SUPPRIME.getStatut());
//        return convertToDTO(demandeRepository.save(demandeDTO));
//    }

    @Transactional
    public DemandeDTO annuler(Integer id) {
        Demande demande = demandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Demande introuvable"));

        // Mise à jour du statut
        demande.setStatut(StatusDemande.DEMANDE_SUPPRIME.getStatut());

        // Vérifier s’il y a une certification associée
        Certification certification = demande.getCertification();
        if (certification != null) {
            certification.setDemande(null); // enlever la FK
            certificationRepository.save(certification);
        }

        Demande saved = demandeRepository.save(demande);
        return convertToDTO(saved);
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

    public Integer updateTempsEcoule(Integer id, DemandeDTO demandeDTO) {
        Optional<Demande> demandeOptional = demandeRepository.findById(id);
        if (demandeOptional.isPresent()) {
            Demande demande = demandeOptional.get();
            demande.setTempsEcoule(demandeDTO.getTempsEcoule());
            demandeRepository.save(demande);
            return demande.getId();
        } else {
            throw new EntityNotFoundException("Demande with ID " + id + " not found.");
        }
    }


    public Integer getDemandesBySexe(String sexe) {
        return demandeRepository.findByDemandeurSexe(sexe).size();
    }
    public List<Demande> getDemandesByRegion(String region) {
        return demandeRepository.findDemandesByRegion(region);
    }

    public Integer getDemandesByRegionId(String regionId) {
        return demandeRepository.findDemandesByRegionId(regionId).size();
    }


    public Integer getDemandesByDepartement(String departementId) {
        return demandeRepository.findDemandesByDepartementId(departementId).size();
    }

    public Integer getDemandesByDepartementByNom(String departementNom) {
        return demandeRepository.countDemandesByDepartementNom(departementNom);
    }

    public Integer getDemandesByDate(LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = LocalDateTime.now();
        return demandeRepository.findByDatedemandeBetween(start, end).size();
    }

    public Map<String, Long> getNombreDemandesParStatutPourDate(LocalDate date) {
        List<String> allStatuts = getAllStatutsExistants();
        List<Object[]> results = demandeRepository.countDemandesByStatutForDate(date.atStartOfDay(), date.plusDays(1).atStartOfDay());
        Map<String, Long> countsMap = new HashMap<>();
        for (Object[] row : results) {
            String statut = (String) row[0];
            Long count = (Long) row[1];
            countsMap.put(statut, count);
        }

        Map<String, Long> finalMap = new LinkedHashMap<>();
        for (String statut : allStatuts) {
            if (!"SUPPRIMER".equalsIgnoreCase(statut)) {
                finalMap.put(statut, countsMap.getOrDefault(statut, 0L));
            }
        }

        return finalMap;
    }

    public List<String> getAllStatutsExistants() {
        return demandeRepository.findDistinctStatutsExcludingSupprimer();
    }


    public String verifierUniciteId(int id) {
        if (demandeRepository.existsById(id)) {
            return "ok"; // ID déjà existant
        } else {
            return "ko"; // ID unique
        }
    }


    public List<Map<String, Object>> getDemandesParSexe() {
        List<Object[]> results = demandeRepository.countDemandesParSexe();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("sexe", row[0] != null ? row[0] : "Non spécifié");
            map.put("totalDemandes", row[1]);
            response.add(map);
        }

        return response;
    }



    /***
     * Cette methode permet la representation
     * graphique et tous les demandes
     * ainsi que par statut
     * @return
     */
    public Map<String, Object> getStatistiquesGlobalesDemandes() {
        Long totalGlobal = demandeRepository.countTotalDemandesSansSupprimer();
        List<Object[]> groupResults = demandeRepository.countDemandesParStatutSansSupprimer();

        List<Map<String, Object>> parStatut = new ArrayList<>();

        for (Object[] row : groupResults) {
            Map<String, Object> map = new HashMap<>();
            map.put("statut", row[0]);
            map.put("total", row[1]);
            parStatut.add(map);
        }

        Map<String, Object> response = new HashMap<>();
        response.put("totalGlobal", totalGlobal);
        response.put("parStatut", parStatut);

        return response;
    }




   // @Override
    public List<Map<String, Object>> getDemandesParRegionAvecNom() {
        return demandeRepository.getDemandesParRegionAvecNom();
    }
    public List<Map<String, Object>> convertRegionIdToName(List<Map<String, Object>> rawData) {
        // Récupérer toutes les régions dans une Map id -> nom pour optimisation
        Map<Integer, String> regionMap = regionRepository.findAll().stream()
                .collect(Collectors.toMap(Region::getId, Region::getNom));

        // Parcourir la liste pour remplacer l'id par le nom
        rawData.forEach(entry -> {
            Object regionIdObj = entry.get("region");
            if (regionIdObj != null) {
                try {
                    Integer regionId = Integer.valueOf(regionIdObj.toString());
                    String nomRegion = regionMap.get(regionId);
                    entry.put("region", nomRegion != null ? nomRegion : "Inconnu");
                } catch (NumberFormatException e) {
                    entry.put("region", "Inconnu");
                }
            } else {
                entry.put("region", "Inconnu");
            }
        });
        return rawData;
    }




    public List<Map<String, Object>> getDemandesGroupByRegionAvecDepartements() {
        // Récupère toutes les régions
        List<Region> regions = regionRepository.findAll();

        // Récupère les statistiques réelles
        List<Map<String, Object>> rawData = demandeRepository.getDemandesParRegionEtDepartement();

        // On crée une Map clé = "region|departement" pour retrouver le total
        Map<String, Long> demandesMap = new HashMap<>();
        for (Map<String, Object> entry : rawData) {
            String region = (String) entry.get("region");
            String departement = (String) entry.get("departement");
            Long totalDemandes = (Long) entry.get("totalDemandes");
            demandesMap.put(region + "|" + departement, totalDemandes);
        }

        // Construction finale
        List<Map<String, Object>> result = new ArrayList<>();
        for (Region region : regions) {
            List<Departement> departements = departementRepository.findByRegionId(region.getId());
            List<Map<String, Object>> departementsData = new ArrayList<>();

            for (Departement dep : departements) {
                String key = region.getNom() + "|" + dep.getNom();
                Long total = demandesMap.getOrDefault(key, 0L);

                Map<String, Object> depEntry = new HashMap<>();
                depEntry.put("departement", dep.getNom());
                depEntry.put("totalDemandes", total);
                departementsData.add(depEntry);
            }

            Map<String, Object> regionEntry = new HashMap<>();
            regionEntry.put("region", region.getNom());
            regionEntry.put("departements", departementsData);

            result.add(regionEntry);
        }

        return result;
    }
    public List<Map<String, Object>> getDemandesDes7DerniersJoursParStatut() {
        LocalDateTime ilYA7Jours = LocalDateTime.now().minusDays(6).withHour(0).withMinute(0).withSecond(0);

        List<Object[]> resultats = demandeRepository.countDemandesByDateAndStatut(ilYA7Jours);

        // 1. Récupérer tous les statuts SAUF "supprimer"
        Set<String> tousLesStatuts = new LinkedHashSet<>();
        for (Object[] row : resultats) {
            String statut = (String) row[1];
            if (!"supprimer".equalsIgnoreCase(statut)) {
                tousLesStatuts.add(statut);
            }
        }

        // 2. Regrouper les résultats par date
        Map<LocalDate, Map<String, Long>> stats = new HashMap<>();
        for (Object[] row : resultats) {
            LocalDate date = ((java.sql.Date) row[0]).toLocalDate();
            String statut = (String) row[1];
            Long total = (Long) row[2];

            if (!"supprimer".equalsIgnoreCase(statut)) {
                stats.computeIfAbsent(date, k -> new HashMap<>()).put(statut, total);
            }
        }

        // 3. Construire le JSON de sortie
        List<Map<String, Object>> reponse = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            LocalDate date = LocalDate.now().minusDays(6 - i);
            Map<String, Object> jour = new LinkedHashMap<>();
            jour.put("date", date);

            Map<String, Long> valeursDuJour = stats.getOrDefault(date, new HashMap<>());
            Map<String, Object> statutsMap = new LinkedHashMap<>();

            long totalDemandes = 0;

            for (String statut : tousLesStatuts) {
                Long val = valeursDuJour.getOrDefault(statut, 0L);
                statutsMap.put(statut, val);
                totalDemandes += val;
            }

            jour.put("totalDemandes", totalDemandes);
            jour.put("parStatut", statutsMap);

            reponse.add(jour);
        }

        return reponse;
    }



public String verifierDateDemande(int demandeId) {
    Optional<Demande> optionalDemande = demandeRepository.findById(demandeId);

    if (optionalDemande.isPresent()) {
        Demande demande = optionalDemande.get();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateDemande = demande.getDatedemande();

        if (dateDemande != null
                && now.getYear() == dateDemande.getYear()
                && now.getMonth() == dateDemande.getMonth()
                && now.getDayOfMonth() == dateDemande.getDayOfMonth()
                && now.getHour() == dateDemande.getHour()
                && now.getMinute() == dateDemande.getMinute()
                && now.getSecond() == dateDemande.getSecond()) {
            return "ok";
        }
    }

    return "ko";
}

    public String getDureeTraitement(int demandeId) {
        Optional<Demande> optionalDemande = demandeRepository.findById(demandeId);

        if (optionalDemande.isEmpty()) {
            return "❌ Demande non trouvée";
        }

        Demande demande = optionalDemande.get();
        LocalDateTime dateDemande = demande.getDatedemande();
        LocalDateTime dateTraitement = demande.getDatetraitement();
        String statut = demande.getStatut();

        if (statut == null || statut.trim().isEmpty()) {
            return "❌ Statut inconnu";
        }

        // Vérifie si la demande est encore en cours
        if (statut.equalsIgnoreCase(StatusDemande.DEMANDE_EN_COURS.getStatut())) {
            return "⌛ Demande en cours de traitement";
        }

        // Ne calcule la durée que si le statut est 'approuvée' ou 'rejetée'
        boolean statutValide = statut.equalsIgnoreCase(StatusDemande.DEMANDE_TRAITEE.getStatut()) ||
                statut.equalsIgnoreCase(StatusDemande.DEMANDE_REFUSEE.getStatut());

        if (!statutValide) {
            return "⛔ Statut non concerné par le calcul de durée";
        }

        if (dateDemande == null || dateTraitement == null) {
            return "❌ Dates manquantes pour le calcul de durée";
        }

        if (dateTraitement.isBefore(dateDemande) || dateTraitement.equals(dateDemande)) {
            return "⚠️ Date de traitement invalide ou identique à la date de demande";
        }

        // ✅ Calcul de la durée
        Duration duration = Duration.between(dateDemande, dateTraitement);

        long jours = duration.toDays();
        long heures = duration.toHours() % 24;
        long minutes = duration.toMinutes() % 60;
        long secondes = duration.getSeconds() % 60;

        StringBuilder sb = new StringBuilder();
        if (jours > 0) {
            sb.append(jours).append(jours > 1 ? " jours " : " jour ");
        }
        sb.append(String.format("%02d:%02d:%02d", heures, minutes, secondes));

        String resultat = sb.toString();

        // ✅ Mise à jour des champs
        demande.setTempsEcoule(dateTraitement.minus(duration));
        demande.setDureeTraitement(resultat); // à stocker si champ non @Transient

        // ✅ Sauvegarde
        demandeRepository.save(demande);

        return resultat;
    }

//    public void mettreEnCoursEtResetAttestationMotif(Integer id) {
//        demandeRepository.updateStatutEnCoursResetAttestationAndMotif(id);
//    }
@Transactional
public void annulerEtDetacherCertification(Integer demandeId) {
    // 1. Mettre à jour la demande
    demandeRepository.updateStatutEnCoursResetAttestationAndMotif(demandeId);

    // 2. Détacher la certification
    certificationRepository.detachDemandeFromCertification(demandeId);
}



}
