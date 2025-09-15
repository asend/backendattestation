package com.fonctionpublique.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fonctionpublique.dto.DemandeDTO;
import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.dto.IntercoDto;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.entities.*;
import com.fonctionpublique.handleException.DemandeurNotExist;
import com.fonctionpublique.services.demande.DemandeService;
import com.fonctionpublique.services.demandeur.DemandeurService;
import com.fonctionpublique.services.uploadFile.FileUploadService;
import com.fonctionpublique.services.uploadFile.FileUploadServiceImpl;
import com.fonctionpublique.services.utilisateur.UtilisateurService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.nio.file.Paths.get;
import static org.springframework.http.HttpHeaders.CONTENT_DISPOSITION;

@RestController
@RequestMapping("/api/uploads")
@Tag(name="fileupload")
@RequiredArgsConstructor
@CrossOrigin
public class FileUploadController {

    @Autowired
    DemandeurService demandeurService;
    @Autowired
    UtilisateurService utilisateurService;

    @Autowired
    DemandeService demandeService;

    private final FileUploadServiceImpl fileUploadServiceImpl;
    private final FileUploadService fileUploadService;

//    @Autowired
//    public FileUploadController(FileUploadServiceImpl fileUploadServiceImpl ) {
//        this.fileUploadServiceImpl = fileUploadServiceImpl;
//
//    }

    @PostMapping("/{id}")
    public ResponseEntity<FileUploadResponse> uploadFile(
            @RequestParam("file") MultipartFile file, @PathVariable("id") int id) throws IOException {
        FileUploadResponse theFile = fileUploadServiceImpl.uploadFile(file,id);

        return new ResponseEntity<>(theFile, HttpStatus.OK);
    }

//    @PostMapping("/uploadImageDemandeur/{id}")
//    public ResponseEntity<FileUpload> uploadFileImage(
//            @RequestParam("file") MultipartFile file, @PathVariable("id") int id) throws Exception {
//        FileUpload theFile = fileUploadServiceImpl.uploadImageDemandeur(file,id);
//
//        return new ResponseEntity<>(theFile, HttpStatus.OK);
//    }
    @PostMapping("/traitant/{id}")
    public ResponseEntity<FileUploadResponse> uploadFileTraitant(
            @RequestParam("file") MultipartFile file, @PathVariable("id") int id) throws IOException {
        FileUploadResponse theFile = fileUploadServiceImpl.uploadFileTraitant(file,id);

        return new ResponseEntity<>(theFile, HttpStatus.OK);
    }

    //    @PostMapping("/{id}")
//    public String uploadFile(
//            @RequestParam("file") MultipartFile file, @PathVariable("id") int id) throws IOException {
//        FileUploadResponse theFile = fileUploadServiceImpl.uploadFile(file,id);
//
//        return "file";// new ResponseEntity<>(theFile, HttpStatus.OK);
//    }


//    @RequestMapping(value = "/loadfromFS/{id}" , method = RequestMethod.GET)
//    public ResponseEntity<byte[]> getImageFS(@PathVariable("id") Integer id) throws IOException {
//        DemandeurDTO demandeur = demandeurService.getById(id);
//
//        if(demandeur.getScannernin().contains(".pdf")){
//            return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(Files.readAllBytes(get(Params.DIRECTORYCNI+"/"+demandeur.getScannernin())));
//        }else{
//            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(Files.readAllBytes(get(Params.DIRECTORYCNI+"/"+demandeur.getScannernin())));
//        }
//
//        //return  ;
//    }

//    ################
@RequestMapping(value = "/loadfromFS/{id}", method = RequestMethod.GET)
public ResponseEntity<byte[]> getImageFS(@PathVariable("id") Integer id) throws IOException {
    DemandeurDTO demandeur = demandeurService.getById(id);
    String filePath = Params.DIRECTORYCNI + "/" + demandeur.getScannernin();
    String fileExtension = demandeur.getScannernin().substring(demandeur.getScannernin().lastIndexOf(".") + 1).toLowerCase();

    MediaType mediaType;
    switch (fileExtension) {
        case "pdf":
            mediaType = MediaType.APPLICATION_PDF;
            break;
        case "png":
            mediaType = MediaType.IMAGE_PNG;
            break;
        case "jpeg":
        case "jpg":
            mediaType = MediaType.IMAGE_JPEG;
            break;
        default:
            mediaType = MediaType.APPLICATION_OCTET_STREAM; // default for unknown types
            break;
    }

    byte[] fileContent = Files.readAllBytes(get(filePath));

    return ResponseEntity.ok()
            .contentType(mediaType)
            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + demandeur.getScannernin() + "\"")
            .body(fileContent);
}

    //####################
    @RequestMapping(value = "/loadAttestation/{id}" , method = RequestMethod.GET)
    public ResponseEntity<byte[]> loadAttestation(@PathVariable("id") Integer id) throws IOException {
        DemandeDTO demande = demandeService.getById(id);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(Files.readAllBytes(get(Params.DIRECTORYATTESTATION+"/"+demande.getUrlattestation())));
    }

    @RequestMapping(value = "/loadAttestationByCode/{code}" , method = RequestMethod.GET)
    public ResponseEntity<byte[]> loadAttestationByCode(@PathVariable("code") String code) throws IOException {
        Demande demande = demandeService.getByCode(code);
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(Files.readAllBytes(get(Params.DIRECTORYATTESTATION+"/"+demande.getUrlattestation())));
    }


//    @GetMapping("/downoad/{file}")
//    public ResponseEntity<Resource> download(@PathVariable("file") long fileId) throws Exception {
//        FileUpload fileUpload = null;
//        fileUpload = fileUploadServiceImpl.download(fileId);
//        return  ResponseEntity.ok()
//                .contentType(MediaType.parseMediaType(fileUpload.getType()))
//                .header(CONTENT_DISPOSITION, "fileupload; fileName\"" + fileUpload.getName()+"\"")
//                .body(new ByteArrayResource(fileUpload.getFile()));
//
//    }
    @DeleteMapping("{id}")
    public void deleteImage(@PathVariable Long id){
        fileUploadServiceImpl.deleteImage(id);

    }

//    @PostMapping("/create")
//    public FileUpload createImage(@RequestParam("file") MultipartFile file) throws IOException {
//        return fileUploadService.uploadImage(file);
//        //return  ResponseEntity.ok("CREATED");
//    }

//    @PostMapping("/create/{idDemandeur}")
//    public FileUpload createImageDemandur(@RequestParam("file") MultipartFile file, @PathVariable("idDemandeur") Integer idDemandeur) throws Exception {
//        return fileUploadServiceImpl.uploadImageDemandeur(file,idDemandeur);
//        //return  ResponseEntity.ok("CREATED");
//    }

    @PostMapping("/create/{idDemandeur}")
    public ResponseEntity<?> createImageDemandeur(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "base64Image", required = false) String base64Image,
            @PathVariable("idDemandeur") Integer idDemandeur) {
        try {
            FileUpload uploadedFile = fileUploadServiceImpl.uploadImageDemandeur(file, base64Image, idDemandeur);
            return ResponseEntity.ok(uploadedFile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DemandeurNotExist e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec de l'upload de l'image.");
        }
    }

















//    @PostMapping("/create1/{idDemandeur}")
//    public ResponseEntity<?> createImageInterco(
//            @RequestParam(value = "file", required = false) MultipartFile file,
//            @RequestParam(value = "base64Image", required = false) String base64Image,
//            @PathVariable("idDemandeur") Integer idDemandeur,
//            @RequestParam("intercoDto") String intercoDto) {
//
//        System.out.println("intercoDto "+intercoDto);
//        try {
//
//            System.out.println("interco" + intercoDto);
//            FileUpload uploadedFile = fileUploadServiceImpl.uploadImageInterco(file, base64Image, idDemandeur,intercoDto);
//            return ResponseEntity.ok(uploadedFile);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (DemandeurNotExist e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec de l'upload de l'image.");
//        }
//    }

    @PostMapping("/create1/{idDemandeur}")
    public ResponseEntity<?> createImageInterco(
            @RequestParam(value = "file", required = false) MultipartFile file,
            @RequestParam(value = "base64Image", required = false) String base64Image,
            @PathVariable("idDemandeur") Integer idDemandeur,
            @RequestParam("intercoDto") String intercoDto) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            IntercoDto intercoDtoObject = objectMapper.readValue(intercoDto, IntercoDto.class);
            System.out.println("FirstName " + intercoDtoObject.getPrenom());
            System.out.println("LastName " + intercoDtoObject.getNom());
            System.out.println("Email Adresse  " + intercoDtoObject.getEmail());
        } catch (JsonProcessingException e) {
            System.out.println("e " + e.getMessage());
            throw new RuntimeException(e);
        }

        try {
            // Afficher le JSON brut
            System.out.println("intercoDto: " + intercoDto);
            FileUpload uploadedFile = fileUploadServiceImpl.uploadImageInterco(file, base64Image, idDemandeur, intercoDto);
            return ResponseEntity.ok(uploadedFile);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (DemandeurNotExist e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec de l'upload de l'image.");
        }
    }


//    @PostMapping("/create1/{idDemandeur}")
//    public ResponseEntity<?> createImageInterco(
//            @RequestParam(value = "file", required = false) MultipartFile file,
//            @RequestParam(value = "base64Image", required = false) String base64Image,
//            @PathVariable("idDemandeur") Integer idDemandeur,
//            @RequestParam("intercoDto") String intercoDto) {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            IntercoDto intercoDtoObject = objectMapper.readValue(intercoDto, IntercoDto.class);
//            System.out.println("FirstName " + intercoDtoObject.getPrenom());
//            System.out.println("LastName " + intercoDtoObject.getNom());
//            System.out.println("Email Adresse  " + intercoDtoObject.getEmail());
//
//        } catch (JsonProcessingException e) {
//            System.out.println("e " + e.getMessage());
//            throw new RuntimeException(e);
//        }
//
//        try {
//            // Afficher le JSON brut
//            System.out.println("intercoDto: " + intercoDto);
//            FileUpload uploadedFile = fileUploadServiceImpl.uploadImageInterco(file, base64Image, idDemandeur, intercoDto);
//            return ResponseEntity.ok(uploadedFile);
//        } catch (IllegalArgumentException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        } catch (DemandeurNotExist e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Échec de l'upload de l'image.");
//        }
//    }
//



    @PostMapping("/create/imagemultiple")
    public ResponseEntity<String> createImage(@RequestParam("file") MultipartFile[] files){
        Arrays.stream(files).map(file->{

            try {
                return fileUploadService.uploadImage(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        //files.f
        //return fileUploadService.uploadImage(file);
        return  ResponseEntity.ok("CREATED");
    }


    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFiles(@PathVariable("filename") String filename) throws IOException {
        Path filePath = get(Params.DIRECTORYATTESTATION).toAbsolutePath().normalize().resolve(filename);
        if(!Files.exists(filePath)) {
            throw new FileNotFoundException(filename + " was not found on the server");
        }
        Resource resource = new UrlResource(filePath.toUri());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("File-Name", filename);
        httpHeaders.add(CONTENT_DISPOSITION, "attachment;File-Name=" + resource.getFilename());
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(Files.probeContentType(filePath)))
                .headers(httpHeaders).body(resource);
    }




//    @RequestMapping(value = "/loadImageTraitant/{id}", method = RequestMethod.GET)
//    public ResponseEntity<byte[]> getTraitant(@PathVariable("id") Integer id) throws IOException {
//        UtilisateurDTO utilisateurDTO = utilisateurService.getById(id);
//        String filePath = Params.DIRECTORYSIGNATURE + "/" + utilisateurDTO.getSignature();
//        String fileExtension = utilisateurDTO.getSignature().substring(utilisateurDTO.getSignature().lastIndexOf(".") + 1).toLowerCase();
//
//        MediaType mediaType;
//        switch (fileExtension) {
//            case "pdf":
//                mediaType = MediaType.APPLICATION_PDF;
//                break;
//            case "png":
//                mediaType = MediaType.IMAGE_PNG;
//                break;
//            case "jpeg":
//            case "jpg":
//                mediaType = MediaType.IMAGE_JPEG;
//                break;
//            default:
//                mediaType = MediaType.APPLICATION_OCTET_STREAM; // default for unknown types
//                break;
//        }
//
//        byte[] fileContent = Files.readAllBytes(get(filePath));
//
//        return ResponseEntity.ok()
//                .contentType(mediaType)
//                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + utilisateurDTO.getSignature() + "\"")
//                .body(fileContent);
//    }
//


    @RequestMapping(value = "/loadImageTraitant/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getTraitant(@PathVariable("id") Integer id) {
        try {
            // Récupérer l'utilisateur par ID
            UtilisateurDTO utilisateurDTO = utilisateurService.getById(id);

            // Vérifier si la signature est null
            if (utilisateurDTO == null || utilisateurDTO.getSignature() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Signature introuvable pour l'utilisateur avec ID : " + id);
            }

            // Construire le chemin du fichier
            String filePath = Params.DIRECTORYSIGNATURE + "/" + utilisateurDTO.getSignature();
            String fileExtension = utilisateurDTO.getSignature()
                    .substring(utilisateurDTO.getSignature().lastIndexOf(".") + 1).toLowerCase();

            // Déterminer le type MIME
            MediaType mediaType;
            switch (fileExtension) {
                case "pdf":
                    mediaType = MediaType.APPLICATION_PDF;
                    break;
                case "png":
                    mediaType = MediaType.IMAGE_PNG;
                    break;
                case "jpeg":
                case "jpg":
                    mediaType = MediaType.IMAGE_JPEG;
                    break;
                default:
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                    break;
            }

            byte[] fileContent = Files.readAllBytes(get(filePath));

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + utilisateurDTO.getSignature() + "\"")
                    .body(fileContent);

        } catch (IOException e) {
            // Gérer les erreurs de lecture de fichier
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la lecture du fichier : " + e.getMessage());
        } catch (Exception e) {
            // Gérer d'autres exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur inattendue est survenue : " + e.getMessage());
        }
    }




    @RequestMapping(value = "/getImageByIdFile/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getImageByIdFile(@PathVariable("id") Long id) {
        try {
            // Récupérer l'utilisateur par ID
            FileUpload fileUpload = fileUploadService.getById(id);

            // Vérifier si la signature est null
            if (fileUpload == null || fileUpload.getName() == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Signature introuvable pour l'utilisateur avec ID : " + id);
            }

            // Construire le chemin du fichier
            String filePath = Params.DIRECTORYCNI + "/" + fileUpload.getName();
            String fileExtension = fileUpload.getName()
                    .substring(fileUpload.getName().lastIndexOf(".") + 1).toLowerCase();

            // Déterminer le type MIME
            MediaType mediaType;
            switch (fileExtension) {
                case "pdf":
                    mediaType = MediaType.APPLICATION_PDF;
                    break;
                case "png":
                    mediaType = MediaType.IMAGE_PNG;
                    break;
                case "jpeg":
                case "jpg":
                    mediaType = MediaType.IMAGE_JPEG;
                    break;
                default:
                    mediaType = MediaType.APPLICATION_OCTET_STREAM;
                    break;
            }

            byte[] fileContent = Files.readAllBytes(get(filePath));

            return ResponseEntity.ok()
                    .contentType(mediaType)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" +fileUpload.getName() + "\"")
                    .body(fileContent);

        } catch (IOException e) {
            // Gérer les erreurs de lecture de fichier
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de la lecture du fichier : " + e.getMessage());
        } catch (Exception e) {
            // Gérer d'autres exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur inattendue est survenue : " + e.getMessage());
        }
    }

//    @GetMapping("/getImagesByIdDemandeur/{idDemandeur}")
//    public ResponseEntity<List<FileUpload>> getImagesByIdDemandeur(@PathVariable Long idDemandeur) {
//        List<FileUpload> files = fileUploadServiceImpl.getImagesByDemandeurId(idDemandeur);
//        if (files.isEmpty()) {
//            return ResponseEntity.noContent().build();
//        }
//        return ResponseEntity.ok(files);
//    }

    @GetMapping("/getImagesByIdDemandeur/{idDemandeur}")
    public ResponseEntity<List<Map<String, Object>>> getImagesByIdDemandeur(@PathVariable Long idDemandeur) {
        List<FileUpload> files = fileUploadServiceImpl.getImagesByDemandeurId(idDemandeur);
        if (files.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        List<Map<String, Object>> response = files.stream().map(file -> {
            Map<String, Object> fileData = new HashMap<>();
            fileData.put("id", file.getId());
            fileData.put("name", file.getName());
            fileData.put("type", file.getType());
            fileData.put("taille", file.getTaille());
//            fileData.put("url", "/images/" + file.getName()); // URL de base de l'image
            return fileData;
        }).toList();

        return ResponseEntity.ok(response);
    }


}
