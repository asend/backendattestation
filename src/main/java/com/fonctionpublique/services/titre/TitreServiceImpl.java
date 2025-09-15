package com.fonctionpublique.services.titre;

import com.fonctionpublique.dto.TitreDTO;
import com.fonctionpublique.dto.UtilisateurDTO;
import com.fonctionpublique.entities.Titre;
import com.fonctionpublique.entities.Utilisateur;
import com.fonctionpublique.repository.TitreRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TitreServiceImpl implements TitreService {

    @Autowired
    private TitreRepository titreRepository;


    public TitreDTO convertToDTO(Titre titre) {

        return TitreDTO.builder()
                .id(titre.getId())
                .titre(titre.getTitre())
                .build();
    }

    public Titre convertToEntity(TitreDTO titreDTO) {

        return Titre.builder()
                .id(titreDTO.getId())
                .titre(titreDTO.getTitre())
                .build();
    }


    public Integer createTitre(TitreDTO titreDTO) {
        Titre titre = convertToEntity(titreDTO);
        Titre savedTitre = titreRepository.save(titre);
        return savedTitre.getId();
    }

    public List<TitreDTO> getAllTitres() {
        return titreRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public void deleteTitre(Integer titreId) {
        if (titreRepository.existsById(titreId)) {
            titreRepository.deleteById(titreId);
        } else {
            throw new EntityNotFoundException("Titre with ID " + titreId + " not found");
        }
    }
}
