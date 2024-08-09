package com.fonctionpublique.repository;

import com.fonctionpublique.entities.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileUploadRepositoy extends JpaRepository<FileUpload, Long> {
}