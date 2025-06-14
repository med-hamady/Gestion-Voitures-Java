package org.example.model;

import java.time.LocalDate;

public class Vente {
    private int id;
    private int voitureId;
    private int clientId;
    private LocalDate dateVente;
    private double prix;

    public Vente(int id, int voitureId, int clientId, LocalDate dateVente, double prix) {
        this.id = id;
        this.voitureId = voitureId;
        this.clientId = clientId;
        this.dateVente = dateVente;
        this.prix = prix;
    }

    // Getters
    public int getId() { return id; }
    public int getVoitureId() { return voitureId; }
    public int getClientId() { return clientId; }
    public LocalDate getDateVente() { return dateVente; }
    public double getPrix() { return prix; }

    // Setters
    public void setVoitureId(int voitureId) { this.voitureId = voitureId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    public void setDateVente(LocalDate dateVente) { this.dateVente = dateVente; }
    public void setPrix(double prix) { this.prix = prix; }
}
