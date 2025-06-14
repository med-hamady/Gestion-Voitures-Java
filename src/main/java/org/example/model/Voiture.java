package org.example.model;

public abstract class Voiture {
    protected int id;
    protected String matricule;
    protected String marque;
    protected String modele;
    protected int annee;
    protected double prixVente;
    protected double prixLocation;
    protected boolean dispoVente;
    protected boolean dispoLocation;
    protected String carburant;
    protected String statut; // ✅ nouveau champ

    public Voiture(int id, String matricule, String marque, String modele, int annee,
                   double prixVente, double prixLocation,
                   boolean dispoVente, boolean dispoLocation,
                   String carburant, String statut) {
        this.id = id;
        this.matricule = matricule;
        this.marque = marque;
        this.modele = modele;
        this.annee = annee;
        this.prixVente = prixVente;
        this.prixLocation = prixLocation;
        this.dispoVente = dispoVente;
        this.dispoLocation = dispoLocation;
        this.carburant = carburant;
        this.statut = statut;
    }

    // Getters
    public int getId() { return id; }
    public String getMatricule() { return matricule; }
    public String getMarque() { return marque; }
    public String getModele() { return modele; }
    public int getAnnee() { return annee; }
    public double getPrixVente() { return prixVente; }
    public double getPrixLocation() { return prixLocation; }
    public boolean isDispoVente() { return dispoVente; }
    public boolean isDispoLocation() { return dispoLocation; }
    public String getCarburant() { return carburant; }
    public String getStatut() { return statut; }

    // Setters
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public void setDispoVente(boolean dispoVente) { this.dispoVente = dispoVente; }
    public void setDispoLocation(boolean dispoLocation) { this.dispoLocation = dispoLocation; }
    public void setCarburant(String carburant) { this.carburant = carburant; }
    public void setStatut(String statut) { this.statut = statut; }

    // Méthode abstraite
    public abstract void afficherInfos();
}
