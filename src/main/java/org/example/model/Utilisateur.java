package org.example.model;

public class Utilisateur {
    private int id;
    private String identifiant;
    private String motDePasse;
    private String role;

    public Utilisateur(int id, String identifiant, String motDePasse, String role) {
        this.id = id;
        this.identifiant = identifiant;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    public Utilisateur(String identifiant, String motDePasse, String role) {
        this.identifiant = identifiant;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // Getters
    public int getId() { return id; }
    public String getIdentifiant() { return identifiant; }
    public String getMotDePasse() { return motDePasse; }
    public String getRole() { return role; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setIdentifiant(String identifiant) { this.identifiant = identifiant; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return identifiant + " (" + role + ")";
    }
}
