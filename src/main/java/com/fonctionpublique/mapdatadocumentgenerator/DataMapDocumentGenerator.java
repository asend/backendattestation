package com.fonctionpublique.mapdatadocumentgenerator;

import com.fonctionpublique.dto.DemandeurDTO;
import com.fonctionpublique.dto.UtilisateurDTO;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DataMapDocumentGenerator {
    public Context setData(List<UtilisateurDTO> empolyeeList) {

        Context context = new Context();

        Map<String, Object> data = new HashMap<>();

        data.put("employees", empolyeeList);

        context.setVariables(data);

        return context;
    }
}
