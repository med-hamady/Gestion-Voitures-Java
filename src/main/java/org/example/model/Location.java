package org.example.model;

import org.example.exception.InvalidDateRangeException;

import java.time.LocalDate;

public class Location {
    private int id;
    private int voitureId;
    private int clientId;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private double prixTotal;

    public Location(int id, int voitureId, int clientId, LocalDate dateDebut, LocalDate dateFin, double prixTotal) {
        this.id = id;
        this.voitureId = voitureId;
        this.clientId = clientId;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.prixTotal = prixTotal;
    }

    // Getters
    public int getId() { return id; }
    public int getVoitureId() { return voitureId; }
    public int getClientId() { return clientId; }
    public LocalDate getDateDebut() { return dateDebut; }
    public LocalDate getDateFin() { return dateFin; }
    public double getPrixTotal() { return prixTotal; }

    // Setters
    public void setVoitureId(int voitureId) { this.voitureId = voitureId; }
    public void setClientId(int clientId) { this.clientId = clientId; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }
    public void setPrixTotal(double prixTotal) { this.prixTotal = prixTotal; }

    // Validation
    public void valider() {
        if (dateDebut != null && dateFin != null && dateFin.isBefore(dateDebut)) {
            throw new InvalidDateRangeException("❌ La date de fin doit être postérieure ou égale à la date de début.");
        }
    }
}
