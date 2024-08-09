package com.fonctionpublique.repository;

import com.fonctionpublique.entities.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile,Integer> {


    Optional<Object> findAllById(int num);

    Optional<Profile> findByCode(String profileName);
}
