package org.example.dao;

import org.example.model.Client;
import org.example.util.DBConnection;
import org.example.exception.AppException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientDAO implements Gestion<Client> {

    @Override
    public void ajouter(Client c) {
        String sql = "INSERT INTO clients (nom, prenom, email, telephone, permis_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getPrenom());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getTelephone());
            stmt.setString(5, c.getPermisId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new AppException("Erreur lors de l'ajout du client : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Client c) {
        String sql = "UPDATE clients SET nom=?, prenom=?, email=?, telephone=?, permis_id=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, c.getNom());
            stmt.setString(2, c.getPrenom());
            stmt.setString(3, c.getEmail());
            stmt.setString(4, c.getTelephone());
            stmt.setString(5, c.getPermisId());
            stmt.setInt(6, c.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new AppException("Erreur lors de la modification du client : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM clients WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new AppException("Erreur lors de la suppression du client : " + e.getMessage());
        }
    }

    @Override
    public List<Client> lister() {
        List<Client> liste = new ArrayList<>();
        String sql = "SELECT * FROM clients";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Client c = new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone"),
                        rs.getString("email"),
                        rs.getString("permis_id")
                );
                liste.add(c);
            }

        } catch (SQLException e) {
            throw new AppException("Erreur lors du chargement des clients : " + e.getMessage());
        }
        return liste;
    }

    @Override
    public Client getById(int id) {
        String sql = "SELECT * FROM clients WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Client(
                        rs.getInt("id"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("telephone"),  // ✅ corrigé ici
                        rs.getString("email"),      // ✅ corrigé ici
                        rs.getString("permis_id")
                );
            }

        } catch (SQLException e) {
            throw new AppException("Erreur lors de la récupération du client : " + e.getMessage());
        }
        return null;
    }
}
