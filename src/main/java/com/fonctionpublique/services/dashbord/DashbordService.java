package com.fonctionpublique.services.dashbord;

import com.fonctionpublique.dto.DemandeDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface DashbordService {

    ResponseEntity<Integer> getCount();

    ResponseEntity<Integer> getApprouved();

    ResponseEntity<Integer> getRejected();


    ResponseEntity<Integer> getCours();


    ResponseEntity<List<DemandeDTO>> getDemandeCount();

    ResponseEntity<List<DemandeDTO>> getDemandeApprouved();

    ResponseEntity<List<DemandeDTO>> getDemandeRejected();


    ResponseEntity<List<DemandeDTO>> getDemandeCours();
}
