package com.fonctionpublique.services.attestation;

import com.fonctionpublique.entities.*;
import com.fonctionpublique.enumpackage.StatusDemande;
import com.fonctionpublique.exception.EntityNotMatchException;
import com.fonctionpublique.mailing.StatutMail;
import com.fonctionpublique.repository.DemandeRepository;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.repository.StructureRepository;
import com.fonctionpublique.repository.UtilisateurRepository;
import com.fonctionpublique.services.certification.CertificationServiceImpl;
import com.fonctionpublique.services.compteur.CompteurServiceImpl;
import com.fonctionpublique.services.mail.MailService;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy" + "  " + " à " + " " + "HH:mm");
        LocalDateTime dateTime = LocalDateTime.now();
        String formattedDateTime = dateTime.format(formatter);

        String data = "Générée le " + formattedDateTime;


        String numeroAttestation = certificationService.generateAttestationNumber(demandeur.getId());

        String parath1 = "REPUBLIQUE DU SENEGAL";
        String para1th = "Un Peuple-Un But-Une Foi";
        String parth1etoil = "**********";

        String parath2 = structure.getNomStructure();
        String parath2etoil = "**********";

        String parath3 = "La Direction Générale de la Fonction Publique";

        String fullName = demandeur.getUtilisateur().getFullName().toUpperCase();
        String date = demandeur.getDatedenaissance();

      String auteur = " P. Le Directeur général, po";
      String titreAuteur = "Le chargé d'Etudes";
      String sane = utilisateur.getFullName();


       // SimpleDateFormat formattage = new SimpleDateFormat("dd-MM-yyyy");
        String parath = "Le Directeur général de la Fonction publique,soussigné, atteste que, " + civilite + "\n" + " " + " " + fullName  + " " + naissance + " " + "le" + " " + " " + date + " " + "á" + " " + demandeur.getLieudenaissance().toUpperCase() + " " +
                "est" + " " + inconnue + " " + "au Fichier de la Fonction publique." + "\n" + "En foi de quoi, la présente attestation lui est délivrée pour servir et valoir ce que de droit.";


        String parath4 = "NB: l'attestation délivrée est valable pour une durée de neuf (9) mois.";

       // String phrasefooter = "NB: l'attestation délivrée est valable pour une durée de neuf (9) mois."+ "\n"+
                                //"attestation délivrée est valable pour une durée de neuf (9) mois.";

        String parath5 = structure.getLocalisation() + " " + "Contact :" + " " + structure.getContact() + "\n" +
                 "BP :" + " " + structure.getBoitePostale() + " " + "Email :" + structure.getEmail();


        String urlDrapeu = Params.DIRECTORYRESOURCE+"/logo-senegal.png";
        // String.valueOf(ClassLoader.getSystemResource("logo-senegal.png")); //"/Users/7maksacodpc/Downloads/logo-drapeua-du-senegal.png";

        ImageData drapeau = ImageDataFactory.create(urlDrapeu);
        Image imageDrapeau = new Image(drapeau);
        imageDrapeau.setRelativePosition(20, 0, 0, 0);
        imageDrapeau.setWidth(100);
        imageDrapeau.setHeight(25);

        String urlLogoMinister = Params.DIRECTORYRESOURCE+"/logominister.png";

        ImageData logoMinistere = ImageDataFactory.create(urlLogoMinister);

        Image imageLogoMinister = new Image(logoMinistere);
        imageLogoMinister.setRelativePosition(55, -50, 0, 0);
        imageLogoMinister.setWidth(40);
        imageLogoMinister.setHeight(40);


        String urlcachet = Params.DIRECTORYSIGNATURE+"/"+utilisateur.getSignature();

        ImageData cachet = ImageDataFactory.create(urlcachet);
        Image imageCachet = new Image(cachet);
        imageCachet.setWidth(130);
        imageCachet.setHeight(130);
        imageCachet.setRelativePosition(360, -80, 0, 0);


        Paragraph aut = new Paragraph(auteur);
        aut.setRelativePosition(350, -65, 0, 0).setBold().setFontSize(11);
        Paragraph titreAut = new Paragraph(titreAuteur);
        titreAut.setRelativePosition(380, -74, 0, 0).setFontSize(10);
        Paragraph SANE = new Paragraph(sane);
        SANE.setRelativePosition(375, -80, 0, 0);

        demande.setUrlattestation(attestationName);
        demande.setAttestationName(code);
        demande.setDatetraitement(LocalDateTime.now());
        demande.setDateexpiration(LocalDate.now().plusMonths(9));
        demande.setStatut(StatusDemande.DEMANDE_TRAITEE.getStatut());

        Certification certification = certificationService.qRCode(utilisateur, demande);
        String imFile = certification.getCode();

        ImageData qR = ImageDataFactory.create(Params.DIRECTORYQRCOD+"/"+imFile);
        Image imageQR = new Image(qR);
        imageQR.setRelativePosition(0, 80, 0, 0);

        LocalDate databefore = LocalDate.now();
        LocalDate dateAfter = databefore.plusMonths(9);

        demande.setDateexpiration(dateAfter);
        demande.setNumerodemande(certificationService.generateAttestationNumber(demandeur.getId()));
        demande.setUtilisateur(utilisateur);
        demande.setCertification(certification);
        demande.setDatetraitement(LocalDateTime.now());

        //String urlDrapeaulineaire = "/Users/7maksacodpc/Downloads/logo-drapeua-du-senegal.png";

        ImageData drapeauLineaire = ImageDataFactory.create(urlDrapeu);
        Image imageDrapeauLineaire = new Image(drapeauLineaire);
        imageDrapeauLineaire.setWidth(500);
        imageDrapeauLineaire.setHeight(2);
        imageDrapeauLineaire.setRelativePosition(0, 15, 0, 0);

        Paragraph paraDate = new Paragraph(data);
        paraDate.setRelativePosition(320, -130, 0, 0);
        Paragraph paraNumeroAttestation = new Paragraph(numeroAttestation);
        paraNumeroAttestation.setFontSize(9);
        paraNumeroAttestation.setRelativePosition(345, -70, 0, 0);

        Paragraph para1 = new Paragraph(parath1).setFontSize(11);
        Paragraph para11 = new Paragraph(para1th).setFontSize(11);
        para11.setRelativePosition(25, -10, 0, 0).setFontSize(8);
        Paragraph para1etoil = new Paragraph(parth1etoil);
        para1etoil.setRelativePosition(45, -17, 0, 0);

        Paragraph para2 = new Paragraph(parath2).setFontSize(11).setRelativePosition(0, -50, 0, 0);
        Paragraph para2etoil = new Paragraph(parath2etoil).setFontSize(11);
        para2etoil.setRelativePosition(60, -60, 0, 0);

        Paragraph para3 = new Paragraph(parath3).setFontSize(12).setItalic();
        para3.setRelativePosition(130, -70, 0, 0);

        Paragraph title = new Paragraph(titre).setFontSize(20).setBold().setBackgroundColor(Color.LIGHT_GRAY).setTextAlignment(TextAlignment.CENTER);
        title.setRelativePosition(0, -40, 0, 0);
        title.setBorder(new SolidBorder(1));

        Paragraph para = new Paragraph(parath).setFontSize(11);
        para.setRelativePosition(0, -30, 0, 0);

        Paragraph para4nb = new Paragraph(parath4).setFontSize(10);
        para4nb.setRelativePosition(0, -25, 0, 0);

        Paragraph para5footer = new Paragraph(parath5).setFontSize(8);
        para5footer.setRelativePosition(80, 20, 0, 0);
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

        //statutMail.sentMailApprouved(u, demande);
        mailService.sendMailApprouvee(demande.getId());

        return demande.getId();

    }

    /**
     * Generate random string for the attestation name
     *
     * @return
     */
    public String genCode() {

        return UUID.randomUUID().toString();
    }


}
