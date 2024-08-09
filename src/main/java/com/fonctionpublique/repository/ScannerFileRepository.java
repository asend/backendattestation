package com.fonctionpublique.repository;

import com.fonctionpublique.entities.ScannerFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface ScannerFileRepository extends JpaRepository<ScannerFile,String> {
}
