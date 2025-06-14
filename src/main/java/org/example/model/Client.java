package org.example.model;

import org.example.exception.InvalidTelephoneException;
import org.example.exception.InvalidEmailException;
import org.example.exception.InvalidPermisException;

public class Client {
    private int id;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String permisId;

    public Client(int id, String nom, String prenom, String telephone, String email, String permisId) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.telephone = telephone;
        this.email = email;
        this.permisId = permisId;
    }

    // Getters
    public int getId() { return id; }
    public String getNom() { return nom; }
    public String getPrenom() { return prenom; }
    public String getTelephone() { return telephone; }
    public String getEmail() { return email; }
    public String getPermisId() { return permisId; }

    // Setters
    public void setNom(String nom) { this.nom = nom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public void setTelephone(String telephone) { this.telephone = telephone; }
    public void setEmail(String email) { this.email = email; }
    public void setPermisId(String permisId) { this.permisId = permisId; }

    // Validation métier
    public void valider() {
        if (!telephone.matches("\\d{8}")) {
            throw new InvalidTelephoneException("Le numéro de téléphone doit contenir exactement 8 chiffres Positifs.");
        }
        if (!email.contains("@gmail.com")) {
            throw new InvalidEmailException("L'adresse email doit contenir un '@gmail.com'.");
        }
        if (!permisId.matches("\\d{15}")) {
            throw new InvalidPermisException("Le numéro de permis doit contenir exactement 15 chiffres.");
        }
    }

    @Override
    public String toString() {
        return nom + " " + prenom;
    }
}
