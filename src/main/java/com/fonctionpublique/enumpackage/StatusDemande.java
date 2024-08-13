package com.fonctionpublique.enumpackage;

public enum StatusDemande {
     DEMANDE_EN_COURS("cours"),
     DEMANDE_TRAITEE("approuvee"),
     DEMANDE_REFUSEE("rejetee"),
    DEMANDE_SUPPRIME("supprimer");
    private final String statut;
    StatusDemande(String statut) {
        this.statut = statut;
    }
    public String getStatut() {
        return statut;
    }
}
