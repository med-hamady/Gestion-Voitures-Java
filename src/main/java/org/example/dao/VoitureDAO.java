package org.example.dao;

import org.example.model.Voiture;
import org.example.model.VoitureStandard;
import org.example.util.DBConnection;
import org.example.exception.AppException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VoitureDAO implements Gestion<Voiture> {

    @Override
    public void ajouter(Voiture v) {
        String sql = "INSERT INTO voitures (matricule, marque, modele, annee, prix_vente, prix_location, dispo_vente, dispo_location, carburant, statut) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, v.getMatricule());
            stmt.setString(2, v.getMarque());
            stmt.setString(3, v.getModele());
            stmt.setInt(4, v.getAnnee());
            stmt.setDouble(5, v.getPrixVente());
            stmt.setDouble(6, v.getPrixLocation());
            stmt.setBoolean(7, v.isDispoVente());
            stmt.setBoolean(8, v.isDispoLocation());
            stmt.setString(9, v.getCarburant());
            stmt.setString(10, v.getStatut());

            stmt.executeUpdate();
            System.out.println("Voiture ajoutée avec succès !");

        } catch (SQLException e) {
            throw new AppException("Erreur lors de l'ajout de la voiture : " + e.getMessage());
        }
    }

    @Override
    public List<Voiture> lister() {
        List<Voiture> liste = new ArrayList<>();
        String sql = "SELECT * FROM voitures";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Voiture v = new VoitureStandard(
                        rs.getInt("id"),
                        rs.getString("matricule"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("annee"),
                        rs.getDouble("prix_vente"),
                        rs.getDouble("prix_location"),
                        rs.getBoolean("dispo_vente"),
                        rs.getBoolean("dispo_location"),
                        rs.getString("carburant"),
                        rs.getString("statut")
                );
                liste.add(v);
            }

        } catch (SQLException e) {
            throw new AppException("Erreur lors du chargement des voitures : " + e.getMessage());
        }
        return liste;
    }

    @Override
    public void modifier(Voiture v) {
        String sql = "UPDATE voitures SET matricule=?, marque=?, modele=?, annee=?, prix_vente=?, prix_location=?, dispo_vente=?, dispo_location=?, carburant=?, statut=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, v.getMatricule());
            stmt.setString(2, v.getMarque());
            stmt.setString(3, v.getModele());
            stmt.setInt(4, v.getAnnee());
            stmt.setDouble(5, v.getPrixVente());
            stmt.setDouble(6, v.getPrixLocation());
            stmt.setBoolean(7, v.isDispoVente());
            stmt.setBoolean(8, v.isDispoLocation());
            stmt.setString(9, v.getCarburant());
            stmt.setString(10, v.getStatut());
            stmt.setInt(11, v.getId());

            stmt.executeUpdate();
            System.out.println("Voiture modifiée avec succès !");

        } catch (SQLException e) {
            throw new AppException("Erreur lors de la modification de la voiture : " + e.getMessage());
        }
    }

    @Override
    public void supprimer(int id) {
        String sql = "DELETE FROM voitures WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Voiture supprimée avec succès !");

        } catch (SQLException e) {
            throw new AppException("Erreur lors de la suppression de la voiture : " + e.getMessage());
        }
    }

    @Override
    public Voiture getById(int id) {
        String sql = "SELECT * FROM voitures WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new VoitureStandard(
                        rs.getInt("id"),
                        rs.getString("matricule"),
                        rs.getString("marque"),
                        rs.getString("modele"),
                        rs.getInt("annee"),
                        rs.getDouble("prix_vente"),
                        rs.getDouble("prix_location"),
                        rs.getBoolean("dispo_vente"),
                        rs.getBoolean("dispo_location"),
                        rs.getString("carburant"),
                        rs.getString("statut")
                );
            }

        } catch (SQLException e) {
            throw new AppException("Erreur lors de la récupération de la voiture : " + e.getMessage());
        }
        return null;
    }
}
