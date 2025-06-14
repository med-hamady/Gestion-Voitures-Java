package org.example.dao;

import org.example.model.Location;
import org.example.util.DBConnection;
import org.example.exception.AppException;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LocationDAO implements Gestion<Location> {

    @Override
    public void ajouter(Location l) {
        String insertSql = "INSERT INTO locations (voiture_id, client_id, date_debut, date_fin, prix_location_totale) VALUES (?, ?, ?, ?, ?)";
        String updateVoitureStatut = "UPDATE voitures SET statut = 'En location' WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateVoitureStatut)) {

            insertStmt.setInt(1, l.getVoitureId());
            insertStmt.setInt(2, l.getClientId());
            insertStmt.setDate(3, Date.valueOf(l.getDateDebut()));
            insertStmt.setDate(4, Date.valueOf(l.getDateFin()));
            insertStmt.setDouble(5, l.getPrixTotal());
            insertStmt.executeUpdate();

            updateStmt.setInt(1, l.getVoitureId());
            updateStmt.executeUpdate();

        } catch (SQLException e) {
            throw new AppException("Erreur lors de l'ajout de la location : " + e.getMessage());
        }
    }

    @Override
    public void modifier(Location l) {
        String sql = "UPDATE locations SET voiture_id=?, client_id=?, date_debut=?, date_fin=?, prix_location_totale=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, l.getVoitureId());
            stmt.setInt(2, l.getClientId());
            stmt.setDate(3, Date.valueOf(l.getDateDebut()));
            stmt.setDate(4, Date.valueOf(l.getDateFin()));
            stmt.setDouble(5, l.getPrixTotal());
            stmt.setInt(6, l.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new AppException("Erreur lors de la modification de la location : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM locations WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new AppException("Erreur lors de la suppression de la location : " + e.getMessage());
        }
    }

    @Override
    public List<Location> lister() {
        List<Location> liste = new ArrayList<>();
        String sql = "SELECT * FROM locations";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Location l = new Location(
                        rs.getInt("id"),
                        rs.getInt("voiture_id"),
                        rs.getInt("client_id"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        rs.getDouble("prix_location_totale")
                );
                liste.add(l);
            }

            // 🔁 Mise à jour des voitures dont la location est terminée
            libererVoituresTerminees(conn);

        } catch (SQLException e) {
            throw new AppException("Erreur lors du chargement des locations : " + e.getMessage());
        }
        return liste;
    }

    @Override
    public Location getById(int id) {
        String sql = "SELECT * FROM locations WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Location(
                        rs.getInt("id"),
                        rs.getInt("voiture_id"),
                        rs.getInt("client_id"),
                        rs.getDate("date_debut").toLocalDate(),
                        rs.getDate("date_fin").toLocalDate(),
                        rs.getDouble("prix_location_totale")
                );
            }

        } catch (SQLException e) {
            throw new AppException("Erreur lors de la récupération de la location : " + e.getMessage());
        }
        return null;
    }

    private void libererVoituresTerminees(Connection conn) {
        String sql = "UPDATE voitures SET statut = 'Disponible' WHERE id IN ( " +
                "SELECT voiture_id FROM locations WHERE date_fin < CURDATE()) " +
                "AND statut = 'En location'";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new AppException("Erreur lors de la libération des voitures : " + e.getMessage());
        }
    }
}
