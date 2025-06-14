package org.example.dao;

import org.example.model.Vente;
import org.example.util.DBConnection;
import org.example.exception.AppException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VenteDAO implements Gestion<Vente> {

    @Override
    public void ajouter(Vente v) {
        String insertSql = "INSERT INTO ventes (voiture_id, client_id, date_vente, prix) VALUES (?, ?, ?, ?)";
        String updateVoitureSql = "UPDATE voitures SET statut = 'Vendu', dispo_vente = 0 WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql);
             PreparedStatement updateStmt = conn.prepareStatement(updateVoitureSql)) {

            insertStmt.setInt(1, v.getVoitureId());
            insertStmt.setInt(2, v.getClientId());
            insertStmt.setDate(3, Date.valueOf(v.getDateVente()));
            insertStmt.setDouble(4, v.getPrix());
            insertStmt.executeUpdate();

            updateStmt.setInt(1, v.getVoitureId());
            updateStmt.executeUpdate();

            System.out.println("✅ Vente ajoutée et voiture mise à jour avec statut 'Vendu'.");

        } catch (SQLException e) {
            throw new AppException("Erreur lors de l'ajout de la vente : " + e.getMessage());
        }
    }

    @Override
    public List<Vente> lister() {
        List<Vente> liste = new ArrayList<>();
        String sql = "SELECT * FROM ventes";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Vente v = new Vente(
                        rs.getInt("id"),
                        rs.getInt("voiture_id"),
                        rs.getInt("client_id"),
                        rs.getDate("date_vente").toLocalDate(),
                        rs.getDouble("prix")
                );
                liste.add(v);
            }

        } catch (SQLException e) {
            throw new AppException("Erreur lors du chargement des ventes : " + e.getMessage());
        }
        return liste;
    }

    @Override
    public void modifier(Vente v) {
        String sql = "UPDATE ventes SET voiture_id=?, client_id=?, date_vente=?, prix=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, v.getVoitureId());
            stmt.setInt(2, v.getClientId());
            stmt.setDate(3, Date.valueOf(v.getDateVente()));
            stmt.setDouble(4, v.getPrix());
            stmt.setInt(5, v.getId());

            stmt.executeUpdate();
            System.out.println("✅ Vente modifiée.");

        } catch (SQLException e) {
            throw new AppException("Erreur lors de la modification de la vente : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM ventes WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("✅ Vente supprimée.");

        } catch (SQLException e) {
            throw new AppException("Erreur lors de la suppression de la vente : " + e.getMessage());
        }
    }

    @Override
    public Vente getById(int id) {
        String sql = "SELECT * FROM ventes WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Vente(
                        rs.getInt("id"),
                        rs.getInt("voiture_id"),
                        rs.getInt("client_id"),
                        rs.getDate("date_vente").toLocalDate(),
                        rs.getDouble("prix")
                );
            }

        } catch (SQLException e) {
            throw new AppException("Erreur lors de la récupération de la vente : " + e.getMessage());
        }
        return null;
    }
}
