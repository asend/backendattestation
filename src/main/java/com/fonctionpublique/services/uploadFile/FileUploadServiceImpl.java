package com.fonctionpublique.services.uploadFile;

import com.fonctionpublique.dto.IntercoDto;
import com.fonctionpublique.entities.*;
import com.fonctionpublique.handleException.DemandeurNotExist;
import com.fonctionpublique.handleException.ImagaNotFound;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.repository.FileUploadRepositoy;
import com.fonctionpublique.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

import static java.nio.file.Paths.get;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileUploadServiceImpl implements  FileUploadService{

    private final FileUploadRepositoy fileUploadRepositoy;
    //private final Path uploadLocaton;
    private final DemandeurRepository demandeurRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public FileUploadResponse uploadFile(MultipartFile file, int id) throws IOException {

        if (demandeurRepository.findById(id).get()==null){
            throw new IOException("not found");
        }
        String fileName = UUID.randomUUID().toString()+"."+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String originalFileName = StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename()));
        Path targetLocation = get(Params.DIRECTORYCNI, fileName).toAbsolutePath().normalize();// ,  this.uploadLocaton.resolve(originalFileName);

        if (Files.copy(file.getInputStream(), targetLocation,
                StandardCopyOption.REPLACE_EXISTING)!=0){
            Demandeur d = demandeurRepository.findById(id).get();
            d.setScannernin(fileName);
            demandeurRepository.save(d);
        }

        return FileUploadResponse.builder()
                .fileName(originalFileName)
                .fileType(file.getContentType())
                .size(file.getSize())
                .build();

    }

    @Override
    public FileUpload uploadImage(MultipartFile file) throws IOException {

        String fileName = UUID.randomUUID().toString() + "" + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String originalFileName = StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename()));
        Path targetLocation = get(Params.DIRECTORYCNI, fileName).toAbsolutePath().normalize();
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        return fileUploadRepositoy.save(
                FileUpload
                        .builder()
                        .type(file.getContentType())
                        .name(fileName)
                        .taille(file.getSize())
                        .build()
        );
    }


//    public FileUpload uploadImageDemandeur(MultipartFile file, Integer id) throws Exception {
//        Optional<Demandeur> d = demandeurRepository.findById(id);//orElse(null);// new Demandeur();
//        //d.setId(id);
//        if(d.isPresent()) {
//            String fileName = UUID.randomUUID().toString() + "" + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//            String originalFileName = StringUtils.cleanPath(
//                    Objects.requireNonNull(file.getOriginalFilename()));
//            Path targetLocation = get(Params.DIRECTORYCNI, fileName).toAbsolutePath().normalize();
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            return fileUploadRepositoy.save(
//                    FileUpload
//                            .builder()
//                            .demandeur(d.get())
//                            .type(file.getContentType())
//                            .name(fileName)
//                            .taille(file.getSize())
//                            .build()
//            );
//        }else {
//            throw new DemandeurNotExist("le demandeur n'existe pas !");
//        }
//    }
@Override
public FileUpload uploadImageDemandeur(MultipartFile file, String base64Image, Integer id) throws Exception {
    if ((file == null || file.isEmpty()) && (base64Image == null || base64Image.isBlank())) {
        throw new IllegalArgumentException("Aucun fichier ou image base64 fourni.");
    }

    Optional<Demandeur> optionalDemandeur = demandeurRepository.findById(id);
    if (optionalDemandeur.isEmpty()) {
        throw new DemandeurNotExist("Le demandeur n'existe pas !");
    }
    Demandeur demandeur = optionalDemandeur.get();

    String fileName;
    Path targetLocation;
    String contentType;
    long fileSize;

    if (file != null && !file.isEmpty()) {
        // 📂 Cas 1 : Fichier Multipart (upload classique)
        contentType = file.getContentType();
        if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
            throw new IllegalArgumentException("Seuls les fichiers PDF et images sont autorisés.");
        }

        // Vérification de la taille (max 4 Mo)
        if (file.getSize() > 4 * 1024 * 1024) {
            throw new IllegalArgumentException("Le fichier dépasse la taille maximale autorisée (4 Mo).");
        }

        // Génération du nom de fichier
        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        fileName = UUID.randomUUID().toString().substring(0, 13) + extension;

        // Définition du chemin d'enregistrement
        targetLocation = Paths.get(Params.DIRECTORYCNI).resolve(fileName).toAbsolutePath().normalize();
        Files.createDirectories(targetLocation.getParent());

        // Copie du fichier sur le disque
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
        fileSize = file.getSize();
    } else {
        // 📸 Cas 2 : Image Base64 (prise directe)
        if (!base64Image.startsWith("data:image/")) {
            throw new IllegalArgumentException("Format base64 invalide !");
        }

        // Extraction du type MIME
        String mimeType = base64Image.substring(5, base64Image.indexOf(";"));
        contentType = mimeType;
        String extension = mimeType.replace("image/", ".");

        // Décodage de l'image
        byte[] decodedBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);

        // Génération du nom unique
        fileName = UUID.randomUUID().toString().substring(0, 8) + extension;
        targetLocation = Paths.get(Params.DIRECTORYCNI).resolve(fileName).toAbsolutePath().normalize();
        Files.createDirectories(targetLocation.getParent());

        // Écriture de l'image sur le disque
        Files.write(targetLocation, decodedBytes);
        fileSize = decodedBytes.length;
    }

    // 🔖 Enregistrement dans la base de données
    FileUpload fileUpload = FileUpload.builder()
            .demandeur(demandeur)
            .type(contentType)
            .name(fileName)
            .taille(fileSize)
            .build();

    return fileUploadRepositoy.save(fileUpload);
}



    public FileUpload uploadImageInterco(MultipartFile file, String base64Image, Integer id, String intercoDto) throws Exception {
        if ((file == null || file.isEmpty()) && (base64Image == null || base64Image.isBlank())) {
            throw new IllegalArgumentException("Aucun fichier ou image base64 fourni.");
        }

        Optional<Demandeur> optionalDemandeur = demandeurRepository.findById(id);
        if (optionalDemandeur.isEmpty()) {
            throw new DemandeurNotExist("Le demandeur n'existe pas !");
        }
        Demandeur demandeur = optionalDemandeur.get();

        String fileName;
        Path targetLocation;
        String contentType;
        long fileSize;

        if (file != null && !file.isEmpty()) {
            // 📂 Cas 1 : Fichier Multipart (upload classique)
            contentType = file.getContentType();
            if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
                throw new IllegalArgumentException("Seuls les fichiers PDF et images sont autorisés.");
            }

            // Vérification de la taille (max 4 Mo)
            if (file.getSize() > 4 * 1024 * 1024) {
                throw new IllegalArgumentException("Le fichier dépasse la taille maximale autorisée (4 Mo).");
            }

            // Génération du nom de fichier
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            fileName = UUID.randomUUID().toString().substring(0, 13) + extension;

            // Définition du chemin d'enregistrement
            targetLocation = Paths.get(Params.DIRECTORYCNI).resolve(fileName).toAbsolutePath().normalize();
            Files.createDirectories(targetLocation.getParent());

            // Copie du fichier sur le disque
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            fileSize = file.getSize();
        } else {
            // 📸 Cas 2 : Image Base64 (prise directe)
            if (!base64Image.startsWith("data:image/")) {
                throw new IllegalArgumentException("Format base64 invalide !");
            }

            // Extraction du type MIME
            String mimeType = base64Image.substring(5, base64Image.indexOf(";"));
            contentType = mimeType;
            String extension = mimeType.replace("image/", ".");

            // Décodage de l'image
            byte[] decodedBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);

            // Génération du nom unique
            fileName = UUID.randomUUID().toString().substring(0, 8) + extension;
            targetLocation = Paths.get(Params.DIRECTORYCNI).resolve(fileName).toAbsolutePath().normalize();
            Files.createDirectories(targetLocation.getParent());

            // Écriture de l'image sur le disque
            Files.write(targetLocation, decodedBytes);
            fileSize = decodedBytes.length;
        }

        // 🔖 Enregistrement dans la base de données
        FileUpload fileUpload = FileUpload.builder()
                .demandeur(demandeur)
                .type(contentType)
                .name(fileName)
                .taille(fileSize)
                .build();

        return fileUploadRepositoy.save(fileUpload);
    }

//    public FileUpload uploadImageDemandeur(MultipartFile file, String base64Image, Integer id) throws Exception {
//        if ((file == null || file.isEmpty()) && (base64Image == null || base64Image.isBlank())) {
//            throw new IllegalArgumentException("Aucun fichier ou image base64 fourni.");
//        }
//
//        Optional<Demandeur> optionalDemandeur = demandeurRepository.findById(id);
//        if (optionalDemandeur.isEmpty()) {
//            throw new DemandeurNotExist("Le demandeur n'existe pas !");
//        }
//        Demandeur demandeur = optionalDemandeur.get();
//
//        String fileName;
//        Path targetLocation;
//        String contentType;
//        long fileSize;
//
//        if (file != null && !file.isEmpty()) {
//            // 📂 Cas 1 : Fichier Multipart (upload classique)
//            contentType = file.getContentType();
//            if (contentType == null || (!contentType.startsWith("image/") && !contentType.equals("application/pdf"))) {
//                throw new IllegalArgumentException("Seuls les fichiers PDF et images sont autorisés.");
//            }
//
//            // Vérification de la taille (max 4 Mo)
//            if (file.getSize() > 4 * 1024 * 1024) {
//                throw new IllegalArgumentException("Le fichier dépasse la taille maximale autorisée (4 Mo).");
//            }
//
//            // Génération du nom de fichier
//            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//            fileName = UUID.randomUUID().toString().substring(0, 13) + extension;
//
//            // Définition du chemin d'enregistrement
//            targetLocation = Paths.get(Params.DIRECTORYCNI).resolve(fileName).toAbsolutePath().normalize();
//            Files.createDirectories(targetLocation.getParent());
//
//            // Copie du fichier sur le disque
//            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
//            fileSize = file.getSize();
//        } else {
//            // 📸 Cas 2 : Image Base64 (prise directe)
//            if (!base64Image.startsWith("data:image/")) {
//                throw new IllegalArgumentException("Format base64 invalide !");
//            }
//
//            // Extraction du type MIME
//            String mimeType = base64Image.substring(5, base64Image.indexOf(";"));
//            contentType = mimeType;
//            String extension = mimeType.replace("image/", ".");
//
//            // Décodage de l'image
//            byte[] decodedBytes = Base64.getDecoder().decode(base64Image.split(",")[1]);
//
//            // Génération du nom unique
//            fileName = UUID.randomUUID().toString().substring(0, 8) + extension;
//            targetLocation = Paths.get(Params.DIRECTORYCNI).resolve(fileName).toAbsolutePath().normalize();
//            Files.createDirectories(targetLocation.getParent());
//
//            // Écriture de l'image sur le disque
//            Files.write(targetLocation, decodedBytes);
//            fileSize = decodedBytes.length;
//        }
//
//        // 🔖 Enregistrement dans la base de données
//        FileUpload fileUpload = FileUpload.builder()
//                .demandeur(demandeur)
//                .type(contentType)
//                .name(fileName)
//                .taille(fileSize)
//                .build();
//
//        return fileUploadRepositoy.save(fileUpload);
//    }


    @Override
    public List<FileUpload> getImageDemandeur(Integer id) {
        Demandeur d = demandeurRepository.findById(id).orElse(null);
        return d.getFileUploads();
    }

    @Override
    public FileUpload getImageDetails(Integer id) throws IOException {
        return null;
    }

    @Override
    public FileUpload save(FileUpload fileUpload) {
        return fileUploadRepositoy.save(fileUpload);
    }

    @Override
    public void deleteImage(Long id) {
        Optional<FileUpload> fileUpload = fileUploadRepositoy.findById(id);
        if (fileUpload.isPresent()) {
            fileUploadRepositoy.delete(fileUpload.get());
            try {
                Files.delete(Paths.get(Params.DIRECTORYCNI + "/" + fileUpload.get().getName()));

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            throw new ImagaNotFound("IMAGE_NOT_FOUND");
        }
    }

    @Override
    public FileUpload getById(Long id) {
        return fileUploadRepositoy.findById(id).orElse(null);
    }


    public FileUploadResponse uploadFileTraitant(MultipartFile file, int id) throws IOException {

        if (utilisateurRepository.findById(id).get()==null){
            throw new IOException("not found");
        }
        String fileName = UUID.randomUUID().toString()+"."+file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        String originalFileName = StringUtils.cleanPath(
                Objects.requireNonNull(file.getOriginalFilename()));
        Path targetLocation = get(Params.DIRECTORYSIGNATURE, fileName).toAbsolutePath().normalize();// ,  this.uploadLocaton.resolve(originalFileName);

        if (Files.copy(file.getInputStream(), targetLocation,
                StandardCopyOption.REPLACE_EXISTING)!=0){
            Utilisateur u = utilisateurRepository.findById(id).get();
            u.setSignature(fileName);
            utilisateurRepository.save(u);
        }

        return FileUploadResponse.builder()
                .fileName(originalFileName)
                .fileType(file.getContentType())
                .size(file.getSize())
                .build();

    }




    public List<FileUpload> getImagesByDemandeurId(Long id) {
        return fileUploadRepositoy.findByDemandeurId(id);
    }

    public FileUpload getImageByIdAndName(Long id, String name) {
        return fileUploadRepositoy.findByName(id, name).orElse(null);
    }







}
