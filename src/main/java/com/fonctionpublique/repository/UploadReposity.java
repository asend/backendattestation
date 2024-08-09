package com.fonctionpublique.repository;

import com.fonctionpublique.entities.Upload;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadReposity extends JpaRepository<Upload, Integer> {
}
