package com.fonctionpublique.entities;

import com.fonctionpublique.repository.DepartementRepository;
import com.fonctionpublique.repository.RegionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PreEnregistreRegionsAndDepartement {


    private final RegionRepository regionRepository;
    private final DepartementRepository departementRepository;
    @PostConstruct
    public void init() {
        List<Region> regions = List.of(
                new Region(1, "Dakar"),
                new Region(2, "Diourbel"),
                new Region(3, "Fatick"),
                new Region(4, "Kaffrine"),
                new Region(5, "Kaolack"),
                new Region(6, "Kédougou"),
                new Region(7, "Kolda"),
                new Region(8, "Louga"),
                new Region(9, "Matam"),
                new Region(10, "Saint-Louis"),
                new Region(11, "Sédhiou"),
                new Region(12, "Tambacounda"),
                new Region(13, "Thiès"),
                new Region(14, "Ziguinchor")
        );
        regionRepository.deleteAll();
        regionRepository.saveAll(regions);

        List<Departement> departements = List.of(
                new Departement(47, "Dakar", 1),
                new Departement(48, "Pikine", 1),
                new Departement(49, "Rufisque", 1),
                new Departement(50, "Guédiawaye", 1),
                new Departement(51, "Keur Massar", 1),

                new Departement(52, "Bambey", 2),
                new Departement(53, "Diourbel", 2),
                new Departement(54, "Mbacké", 2),

                new Departement(55, "Fatick", 3),
                new Departement(56, "Foundiougne", 3),
                new Departement(57, "Gossas", 3),

                new Departement(58, "Kaffrine", 4),
                new Departement(59, "Birkelane", 4),
                new Departement(60, "Koungheul", 4),
                new Departement(61, "Malem-Hodar", 4),

                new Departement(62, "Kaolack", 5),
                new Departement(63, "Nioro du Rip", 5),
                new Departement(64, "Guinguinéo", 5),

                new Departement(65, "Kédougou", 6),
                new Departement(66, "Salemata", 6),
                new Departement(67, "Saraya", 6),

                new Departement(68, "Kolda", 7),
                new Departement(69, "Vélingara", 7),
                new Departement(70, "Médina Yoro Foulah", 7),

                new Departement(71, "Kébémer", 8),
                new Departement(72, "Linguère", 8),
                new Departement(73, "Louga", 8),

                new Departement(74, "Kanel", 9),
                new Departement(75, "Matam", 9),
                new Departement(76, "Ranérou", 9),

                new Departement(77, "Dagana", 10),
                new Departement(78, "Podor", 10),
                new Departement(79, "Saint-Louis", 10),

                new Departement(80, "Sédhiou", 11),
                new Departement(81, "Bounkiling", 11),
                new Departement(82, "Goudomp", 11),

                new Departement(83, "Bakel", 12),
                new Departement(84, "Tambacounda", 12),
                new Departement(85, "Goudiry", 12),
                new Departement(86, "Koumpentoum", 12),

                new Departement(87, "Mbour", 13),
                new Departement(88, "Thiès", 13),
                new Departement(89, "Tivaouane", 13),

                new Departement(90, "Bignona", 14),
                new Departement(91, "Oussouye", 14),
                new Departement(92, "Ziguinchor", 14)
        );

        departementRepository.deleteAll();
        departementRepository.saveAll(departements);
    }
}