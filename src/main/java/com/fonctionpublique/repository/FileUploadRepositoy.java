package com.fonctionpublique.repository;

import com.fonctionpublique.entities.FileUpload;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileUploadRepositoy extends JpaRepository<FileUpload, Long> {

    List<FileUpload> findByDemandeurId(Long demandeurId);
    @Query("SELECT f FROM FileUpload f WHERE f.id = :id AND f.name = :name")
    Optional<FileUpload> findByName(@Param("id") Long id, @Param("name") String name);



}