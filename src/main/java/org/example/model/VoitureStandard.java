package org.example.model;

import org.example.exception.*;

import java.time.LocalDate;

public class VoitureStandard extends Voiture {

    public VoitureStandard(int id, String matricule, String marque, String modele, int annee,
                           double prixVente, double prixLocation,
                           boolean dispoVente, boolean dispoLocation,
                           String carburant, String statut) {
        super(id, matricule, marque, modele, annee, prixVente, prixLocation, dispoVente, dispoLocation, carburant, statut);
    }

    @Override
    public void afficherInfos() {
        System.out.println("[" + matricule + "] " + marque + " " + modele + " (" + annee + ") - " + carburant + " - " + statut);
    }

    @Override
    public String toString() {
        return matricule + " - " + marque + " " + modele + " [" + statut + "]";
    }

    // ✅ Méthode de validation
    public void valider() {
        if (matricule == null || !matricule.matches("\\d{2}[A-Za-z]{2}\\d{2}")) {
            throw new InvalidMatriculeException("Le matricule doit contenir exactement 2 chiffres, 2 lettres, 2 chiffres (ex: 12AB34).");
        }
        if (marque == null || marque.trim().isEmpty()) {
            throw new InvalidMarqueException("La marque ne peut pas être vide.");
        }
        if (modele == null || modele.trim().isEmpty()) {
            throw new InvalidModeleException("Le modèle ne peut pas être vide.");
        }
        int currentYear = LocalDate.now().getYear();
        if (annee < 1900 || annee > currentYear) {
            throw new InvalidAnneeException("L'année doit être entre 1900 et " + currentYear + ".");
        }
        if (prixVente < 0) {
            throw new InvalidPrixVenteException("Le prix de vente ne peut pas être négatif.");
        }
        if (prixLocation < 0) {
            throw new InvalidPrixLocationException("Le prix de location ne peut pas être négatif.");
        }
        if (!(carburant.equalsIgnoreCase("essence") ||
                carburant.equalsIgnoreCase("diesel") ||
                carburant.equalsIgnoreCase("électrique"))) {
            throw new InvalidCarburantException("Type de carburant invalide. (essence, diesel, électrique)");
        }
    }
}
