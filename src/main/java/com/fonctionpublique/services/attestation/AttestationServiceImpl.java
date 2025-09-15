package com.fonctionpublique.services.attestation;

import com.fonctionpublique.entities.*;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.exception.EntityNotMatchException;
import com.fonctionpublique.repository.DemandeRepository;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.repository.StructureRepository;
import com.fonctionpublique.repository.UtilisateurRepository;
import com.fonctionpublique.services.certification.CertificationServiceImpl;
import com.fonctionpublique.services.compteur.CompteurServiceImpl;
import com.fonctionpublique.services.mail.MailService;
import com.fonctionpublique.whatsapp.Constantes;
import com.google.zxing.WriterException;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.property.TextAlignment;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AttestationServiceImpl implements AttestationService {

    private final DemandeRepository demandeRepository;
    private final CertificationServiceImpl certificationService;
    private final DemandeurRepository demandeurRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final StructureRepository structureRepository;
    private final CompteurServiceImpl compteurService;
    private final CompteurServiceImpl compteurServiceImpl;
    private final MailService mailService;

    @Override
    public Integer getAttestationPdf(Utilisateur utilisateur, Demandeur demandeur, Demande demande, Structure structure) throws IOException, WriterException {

        String civilite = demandeur.getSexe().equalsIgnoreCase("Masculin") ? "Monsieur" : "Madame";
        String naissance = demandeur.getSexe().equalsIgnoreCase("Masculin") ? "né" : "née";
        String inconnue = demandeur.getSexe().equalsIgnoreCase("Masculin") ? "inconnu" : "inconnue";



        String code = genCode();
        String attestationName = code + ".pdf";
        String path = Params.DIRECTORYATTESTATION + "/" + attestationName;

        String titre = "ATTESTATION";

        LocalDateTime formattedDateTimeS = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String formattedDateTime = formattedDateTimeS.format(dateTimeFormatter);

        String data = "DAKAR le " + formattedDateTime;



        String numeroAttestation = certificationService.generateAttestationNumber(demandeur.getId());

        String parath1 = "REPUBLIQUE DU SENEGAL";
        String para1th = "Un Peuple-Un But-Une Foi";
        String parth1etoil = "**********";

        String parath2 = structure.getNomStructure();
        String parath2etoil = "**********";

        String parath3 = "Direction Générale de la Fonction Publique";

        String fullName = demandeur.getUtilisateur().getFullName().toUpperCase();

//        String dateNaissance = "dd-MM-yyyy";
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateNaissance);
//        String date = simpleDateFormat.format(demandeur.getDatedenaissance());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String date = demandeur.getDatedenaissance().format(formatter);



        String lieudenaissance = demandeur.getLieudenaissance().toUpperCase();



        String auteur = " P. Le Directeur général";
        String titreAuteur = utilisateur.getTitre();
        String sane = utilisateur.getFullName();



        String parath = "Le Directeur général de la Fonction publique, soussigné, atteste que, " +
                civilite + " " + fullName +" "+ naissance +", le " + date + " à " + lieudenaissance + ", " +
                "CNI: " + demandeur.getNin() + ", " +
                 inconnue + " des fichiers du personnel de la Fonction publique, " +
                "n’est pas agent de la Fonction publique.\n" +
                "En foi de quoi, la présente attestation lui est délivrée, à sa demande, " +
                "pour servir et valoir ce que de droit.";



        String parath4 = "NB: l'attestation délivrée est valable pour une durée de neuf (9) mois.";


        String parath5 = structure.getLocalisation() + " " + "Contact :" + " " + structure.getContact() + "\n";
                String parath6 =  " " + structure.getBoitePostale() + " " + "Email : " + structure.getEmail();


        String urlDrapeu = Params.DIRECTORYRESOURCE+"/logo-senegal.png";

        ImageData drapeau = ImageDataFactory.create(urlDrapeu);
        Image imageDrapeau = new Image(drapeau);
        imageDrapeau.setRelativePosition(40, 2, 0, 0);
        imageDrapeau.setWidth(40);
        imageDrapeau.setHeight(25);

        String urlLogoMinister = Params.DIRECTORYRESOURCE+"/logominister.png";

        ImageData logoMinistere = ImageDataFactory.create(urlLogoMinister);

        Image imageLogoMinister = new Image(logoMinistere);
        imageLogoMinister.setRelativePosition(40, -50, 0, 0);
        imageLogoMinister.setWidth(40);
        imageLogoMinister.setHeight(40);


//        String urlcachet = Params.DIRECTORYSIGNATURE+"/"+utilisateur.getSignature();
        String urlcachet = Params.DIRECTORYSIGNATURE+"/" + utilisateur.getSignature();


        ImageData cachet = ImageDataFactory.create(urlcachet);
        Image imageCachet = new Image(cachet);
        imageCachet.setWidth(115);
        imageCachet.setHeight(115);
        imageCachet.setRelativePosition(360, -115, 0, 0);

        Paragraph aut = new Paragraph(auteur);
        aut.setRelativePosition(360, -100, 0, 0).setBold().setFontSize(10);
        Paragraph titreAut = new Paragraph(titreAuteur);
        titreAut.setRelativePosition(370, -108, 0, 0).setFontSize(10);
        Paragraph SANE = new Paragraph(sane);
        SANE.setRelativePosition(375, -125, 0, 0).setFontSize(10);

        demande.setUrlattestation(attestationName);
        demande.setAttestationName(code);
        demande.setDatetraitement(LocalDateTime.now());
        demande.setDateexpiration(LocalDate.now().plusMonths(9));
        demande.setStatut(StatusDemande.DEMANDE_TRAITEE.getStatut());

        Certification certification = certificationService.qRCode(utilisateur, demande);
        String imFile = certification.getCode();

        ImageData qR = ImageDataFactory.create(Params.DIRECTORYQRCOD + "/" + imFile);
        Image imageQR = new Image(qR);
        imageQR.setRelativePosition(0, 25, 0, 0);

        LocalDate databefore = LocalDate.now();
        LocalDate dateAfter = databefore.plusMonths(9);

        demande.setDateexpiration(dateAfter);
        demande.setNumerodemande(certificationService.generateAttestationNumber(demandeur.getId()));
        demande.setUtilisateur(utilisateur);
        demande.setCertification(certification);
        demande.setDatetraitement(LocalDateTime.now());


        ImageData drapeauLineaire = ImageDataFactory.create(urlDrapeu);
        Image imageDrapeauLineaire = new Image(drapeauLineaire);
        imageDrapeauLineaire.setWidth(500);
        imageDrapeauLineaire.setHeight(2);
        imageDrapeauLineaire.setRelativePosition(0, -5, 0, 0);

        Paragraph paraDate = new Paragraph(data);
        paraDate.setRelativePosition(340, -130, 0, 0).setFontSize(10);
        Paragraph paraNumeroAttestation = new Paragraph(numeroAttestation);
        paraNumeroAttestation.setFontSize(9);
        paraNumeroAttestation.setRelativePosition(345, -70, 0, 0);

        Paragraph para1 = new Paragraph(parath1).setFontSize(9);
        Paragraph para11 = new Paragraph(para1th).setFontSize(9);
        para11.setRelativePosition(20, -10, 0, 0).setFontSize(7);
        Paragraph para1etoil = new Paragraph(parth1etoil);
        para1etoil.setRelativePosition(39, -20, 0, 0);

        Paragraph para2 = new Paragraph(parath2).setFontSize(9).setRelativePosition(0, -50, 0, 0);
        Paragraph para2etoil = new Paragraph(parath2etoil);
        para2etoil.setRelativePosition(37, -60, 0, 0);


        Paragraph para3 = new Paragraph(parath3).setFontSize(12);
        para3.setRelativePosition(0, -70, 0, 0);


        Paragraph title = new Paragraph(titre).setFontSize(18).setBold().setBackgroundColor(Color.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER);
        title.setRelativePosition(0, -50, 0, 0);
        title.setBorder(new SolidBorder(1));

        Paragraph para = new Paragraph(parath).setFontSize(11);
        para.setRelativePosition(0, -45, 0, 0);

        Paragraph para4nb = new Paragraph(parath4).setFontSize(10);
        para4nb.setRelativePosition(0, -60, 0, 0);

        Paragraph para5footer = new Paragraph(parath5).setFontSize(9);
        para5footer.setRelativePosition(80, -3, 0, 0);

        Paragraph para6footer = new Paragraph(parath6).setFontSize(9);
        para6footer.setRelativePosition(80, -9, 0, 0);


        System.out.println(path);
        PdfWriter writer = new PdfWriter(path);

        PdfDocument pdfDocument = new PdfDocument(writer);
        pdfDocument.addNewPage();

        Document document = new Document(pdfDocument);

        document.add(imageDrapeau);

        document.add(para1);
        document.add(para11);
        document.add(para1etoil);

        document.add(paraNumeroAttestation);

        document.add(imageLogoMinister);

        document.add(para2);
        document.add(para2etoil);
        document.add(paraDate);

        document.add(para3);

        document.add(title);

        document.add(para);

        document.add(imageQR);

        document.add(aut);
        document.add(titreAut);
        document.add(imageCachet);
        document.add(SANE);

        document.add(para4nb);

        document.add(imageDrapeauLineaire);

        document.add(para5footer);
        document.add(para6footer);


        document.close();

        System.out.println("fichier pdf généré");

        return demandeRepository.save(demande).getId();
    }



    /**
     * generate pdf
     *
     * @param idU
     * @param idDemandeur
     * @param idDemande
     * @param idStructure
     * @return
     * @throws IOException
     * @throws WriterException
     */
    @Override
    public Integer generatePdf(Integer idU, Integer idDemandeur, Integer idDemande, Integer idStructure) throws IOException, WriterException {
        Utilisateur u = utilisateurRepository.findById(idU).orElse(null);
        Demandeur d = demandeurRepository.findById(idDemandeur).orElse(null);
        Structure s = structureRepository.findById(idStructure).orElse(null);
        Demande demande = demandeRepository.findById(idDemande).orElse(null);

        if (d == null || u == null || s == null || demande == null) {
            throw new EntityNotMatchException("missing data");
        }

        getAttestationPdf(u, d, demande, s);

        Compteur optionalCompteur = compteurService.findById(1);
        if (optionalCompteur != null) {
            optionalCompteur.setCurrentCount(compteurServiceImpl.incrementCounter());
            compteurServiceImpl.save(optionalCompteur);
        }

        // Envoi de mail et WhatsApp
        // statutMail.sentMailApprouved(u, demande);
        mailService.sendMailApprouvee(demande.getId());
        Constantes.sendDocumentByWhatsapp(d.getTelephone(), "https://fpsend.mfprsp.com/" + demande.getUrlattestation());

        System.out.println("d.getTelephone() = " + d.getTelephone());
        System.out.println("path file = https://fpsend.mfprsp.com/" + demande.getUrlattestation());

        // ➕ Calcul et enregistrement de la durée de traitement
        if (demande.getDatedemande() != null && demande.getDatetraitement() != null) {
            Duration duration = Duration.between(demande.getDatedemande(), demande.getDatetraitement());

            long days = duration.toDays();
            long hours = duration.minusDays(days).toHours();
            long minutes = duration.minusDays(days).minusHours(hours).toMinutes();
            long seconds = duration.minusDays(days).minusHours(hours).minusMinutes(minutes).getSeconds();

            StringBuilder dureeStr = new StringBuilder();
            if (days > 0) {
                dureeStr.append(days).append(" jour").append(days > 1 ? "s " : " ");
            }
            dureeStr.append(String.format("%02d:%02d:%02d", hours, minutes, seconds));

            demande.setDureeTraitement(dureeStr.toString());
            demandeRepository.save(demande);
        }

        return demande.getId();
    }

//    public Integer generatePdf(Integer idU, Integer idDemandeur, Integer idDemande, Integer idStructure) throws IOException, WriterException {
//        Utilisateur u = utilisateurRepository.findById(idU).orElse(null);
//        Demandeur d = demandeurRepository.findById(idDemandeur).orElse(null);
//        Structure s = structureRepository.findById(idStructure).orElse(null);
//        Demande demande = demandeRepository.findById(idDemande).orElse(null);
//
//        if (d == null || u == null || s == null || demande == null) {
//            throw new EntityNotMatchException("missing data");
//        }
//        getAttestationPdf(u, d, demande, s);
//        Compteur optionalCompteur = compteurService.findById(1);
//        if (optionalCompteur != null) {
//            optionalCompteur.setCurrentCount(compteurServiceImpl.incrementCounter());
//            compteurServiceImpl.save(optionalCompteur);
//        }
//
//        //statutMail.sentMailApprouved(u, demande);
//        mailService.sendMailApprouvee(demande.getId());
//        Constantes.sendDocumentByWhatsapp(d.getTelephone(),"https://fpsend.mfprsp.com/"+ demande.getUrlattestation());
//        System.out.println("d.getTelephone()" + d.getTelephone());
//        System.out.println("path file " +" " + "https://fpsend.mfprsp.com/"+ demande.getUrlattestation());
//
//        return demande.getId();
//
//    }

    /**
     * Generate random string for the attestation name
     *
     * @return
     */
    public String genCode() {
        return UUID.randomUUID().toString();
    }

//    public static void sendDocumentByWhatsapp(String telephone, String document) throws IOException {
//        OkHttpClient client = new OkHttpClient();
//
//
//        RequestBody body = new FormBody.Builder()
//                .add("token", "o5ev9jpddl8saakw")
//                .add("to", telephone)
//                .add("filename", "attestation de non appartenance á la foncton publique")
//                .add("document", document)
//                .add("caption", "au service des usagers !!!")
//
//
//                .build();
//
//        Request request = new Request.Builder()
//                .url("https://api.ultramsg.com/instance40778/messages/document")
//                .post(body)
//                .addHeader("content-type", "application/x-www-form-urlencoded")
//                .build();
//
//        Response response = client.newCall(request).execute();
//
//        System.out.println(response.body().string());
//
//
//    }
//



}
