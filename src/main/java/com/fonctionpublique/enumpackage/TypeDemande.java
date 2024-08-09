package com.fonctionpublique.enumpackage;

public enum TypeDemande {

    DEMANDE_NON_APP("Demande de non-appartenance á la Fonction Publique");

    private final String statut;
    TypeDemande(String statut) {
        this.statut = statut;
    }
    public String getStatut() {
        return statut;
    }

}
