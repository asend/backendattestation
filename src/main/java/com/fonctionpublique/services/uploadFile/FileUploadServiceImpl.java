package com.fonctionpublique.services.uploadFile;

import com.fonctionpublique.entities.Demandeur;
import com.fonctionpublique.entities.FileUpload;
import com.fonctionpublique.entities.FileUploadResponse;
import com.fonctionpublique.entities.Params;
import com.fonctionpublique.repository.DemandeurRepository;
import com.fonctionpublique.repository.FileUploadRepositoy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;

import static java.nio.file.Paths.get;

@Service
@RequiredArgsConstructor
public class FileUploadServiceImpl implements  FileUploadService{

    private final FileUploadRepositoy fileUploadRepositoy;
    //private final Path uploadLocaton;
    private final DemandeurRepository demandeurRepository;

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
            //on set la valeur de scanncni de la table demandeur
            Demandeur d = demandeurRepository.findById(id).get();
//             d.setScannernin(originalFileName);
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
    public FileUpload download(long fileId) throws Exception {
        return fileUploadRepositoy.findById(fileId)
                .orElseThrow(() -> new Exception("A file with id: " + fileId + "could not found"));
    }









}
