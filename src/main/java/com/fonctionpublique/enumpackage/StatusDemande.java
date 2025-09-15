package com.fonctionpublique.enumpackage;

public enum StatusDemande {
     DEMANDE_EN_COURS("cours"),
     DEMANDE_TRAITEE("approuvée"),
     DEMANDE_REFUSEE("rejetée"),
    DEMANDE_SUPPRIME("supprimer");
    private final String statut;
    StatusDemande(String statut) {
        this.statut = statut;
    }
    public String getStatut() {
        return statut;
    }
}
