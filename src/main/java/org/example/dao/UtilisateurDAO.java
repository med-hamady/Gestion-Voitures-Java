package org.example.dao;

import org.example.model.Utilisateur;
import org.example.util.DBConnection;
import org.example.exception.AppException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UtilisateurDAO {

    // 🔐 Authentification d’un utilisateur
    public Utilisateur authentifier(String identifiant, String motDePasse) {
        String sql = "SELECT * FROM utilisateurs WHERE identifiant = ? AND mot_de_passe = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, identifiant);
            stmt.setString(2, motDePasse);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Utilisateur(
                        rs.getInt("id"),
                        rs.getString("identifiant"),
                        rs.getString("mot_de_passe"),
                        rs.getString("role")
                );
            }

        } catch (SQLException e) {
            throw new AppException("Erreur d'authentification : " + e.getMessage());
        }

        return null; // Utilisateur non trouvé
    }

    // ✅ Connexion d'un utilisateur (ancienne version nommée différemment, peut être supprimée si doublon)
    public Utilisateur seConnecter(String identifiant, String motDePasse) {
        return authentifier(identifiant, motDePasse);
    }

    // ✅ Ajouter un nouvel utilisateur
    public void ajouterUtilisateur(Utilisateur utilisateur) {
        String sql = "INSERT INTO utilisateurs (identifiant, mot_de_passe, role) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, utilisateur.getIdentifiant());
            stmt.setString(2, utilisateur.getMotDePasse());
            stmt.setString(3, utilisateur.getRole());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new AppException("Erreur lors de l'ajout de l'utilisateur : " + e.getMessage());
        }
    }

    // ✅ Vérifier si un identifiant existe déjà
    public boolean identifiantExiste(String identifiant) {
        String sql = "SELECT COUNT(*) FROM utilisateurs WHERE identifiant = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, identifiant);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            throw new AppException("Erreur lors de la vérification de l'identifiant : " + e.getMessage());
        }
        return false;
    }
}
