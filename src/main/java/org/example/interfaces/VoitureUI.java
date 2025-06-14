package org.example.interfaces;

import org.example.dao.VoitureDAO;
import org.example.model.Voiture;
import org.example.model.VoitureStandard;
import org.example.exception.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;


public class VoitureUI extends JFrame {

    private final JTextField tfMatricule, tfMarque, tfModele, tfAnnee, tfPrixVente, tfPrixLocation;
    private final JComboBox<String> cbCarburant, cbStatut;
    private final JCheckBox cbDispoVente, cbDispoLocation;
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final VoitureDAO voitureDAO = new VoitureDAO();

    public VoitureUI() {
        setTitle("🚗 Gestion des Voitures");
        setSize(1000, 600);
        setLocationRelativeTo(null);


        // 🌟 Police moderne
        Font font = new Font("Segoe UI", Font.PLAIN, 14);
        UIManager.put("Label.font", font);
        UIManager.put("Button.font", font);
        UIManager.put("ComboBox.font", font);
        UIManager.put("TextField.font", font);
        UIManager.put("Table.font", font);
        UIManager.put("Table.rowHeight", 24);

        JPanel formPanel = new JPanel(new GridLayout(5, 4, 10, 10));
        formPanel.setBorder(new EmptyBorder(15, 15, 10, 15));

        tfMatricule = new JTextField();
        tfMarque = new JTextField();
        tfModele = new JTextField();
        tfAnnee = new JTextField();
        tfPrixVente = new JTextField();
        tfPrixLocation = new JTextField();
        cbCarburant = new JComboBox<>(new String[]{"Essence", "Diesel", "Électrique"});
        cbStatut = new JComboBox<>(new String[]{"Disponible"});
        cbDispoVente = new JCheckBox("Disponible à la vente");
        cbDispoLocation = new JCheckBox("Disponible à la location");

        formPanel.add(new JLabel("Matricule :")); formPanel.add(tfMatricule);
        formPanel.add(new JLabel("Marque :")); formPanel.add(tfMarque);
        formPanel.add(new JLabel("Modèle :")); formPanel.add(tfModele);
        formPanel.add(new JLabel("Année :")); formPanel.add(tfAnnee);
        formPanel.add(new JLabel("Prix Vente (€) :")); formPanel.add(tfPrixVente);
        formPanel.add(new JLabel("Prix Location (€) :")); formPanel.add(tfPrixLocation);
        formPanel.add(new JLabel("Carburant :")); formPanel.add(cbCarburant);
        formPanel.add(new JLabel("Statut :")); formPanel.add(cbStatut);
        formPanel.add(cbDispoVente); formPanel.add(cbDispoLocation);

        JButton btnAjouter = new JButton("➕ Ajouter");
        JButton btnModifier = new JButton("✏️ Modifier");
        JButton btnSupprimer = new JButton("🗑️ Supprimer");

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        String[] colonnes = {
                "ID", "Matricule", "Marque", "Modèle", "Année",
                "Prix Vente", "Prix Location", "Carburant", "Statut", "Vente", "Location"
        };

        tableModel = new DefaultTableModel(colonnes, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("📋 Liste des Voitures"));

        add(scrollPane, BorderLayout.CENTER);

        btnAjouter.addActionListener(this::ajouterVoiture);
        btnModifier.addActionListener(this::modifierVoiture);
        btnSupprimer.addActionListener(this::supprimerVoiture);
        table.getSelectionModel().addListSelectionListener(this::remplirFormulaire);

        chargerVoitures();
        setVisible(true);
    }

    private VoitureStandard lireVoitureDepuisFormulaire(int id) {
        String matricule = tfMatricule.getText().trim();
        String marque = tfMarque.getText().trim();
        String modele = tfModele.getText().trim();
        int annee = Integer.parseInt(tfAnnee.getText().trim());
        double prixVente = Double.parseDouble(tfPrixVente.getText().trim());
        double prixLocation = Double.parseDouble(tfPrixLocation.getText().trim());
        String carburant = (String) cbCarburant.getSelectedItem();
        String statut = (String) cbStatut.getSelectedItem();
        boolean vente = cbDispoVente.isSelected();
        boolean location = cbDispoLocation.isSelected();

        return new VoitureStandard(id, matricule, marque, modele, annee,
                prixVente, prixLocation, vente, location, carburant, statut);
    }

    private void ajouterVoiture(ActionEvent e) {
        try {
            VoitureStandard v = lireVoitureDepuisFormulaire(0);
            v.valider();
            voitureDAO.ajouter(v);
            chargerVoitures();
            JOptionPane.showMessageDialog(this, "✅ Voiture ajoutée !");
            clearForm();
        } catch (AppException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur inattendue : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierVoiture(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            try {
                int id = (int) tableModel.getValueAt(row, 0);
                VoitureStandard v = lireVoitureDepuisFormulaire(id);
                v.valider();
                voitureDAO.modifier(v);
                chargerVoitures();
                JOptionPane.showMessageDialog(this, "✅ Voiture modifiée !");
                clearForm();
            } catch (AppException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur inattendue : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sélectionnez une voiture à modifier.");
        }
    }

    private void supprimerVoiture(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Suppression", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                voitureDAO.supprimer(id);
                chargerVoitures();
                clearForm();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Sélectionnez une voiture à supprimer.");
        }
    }

    private void chargerVoitures() {
        tableModel.setRowCount(0);
        for (Voiture v : voitureDAO.lister()) {
            tableModel.addRow(new Object[]{
                    v.getId(), v.getMatricule(), v.getMarque(), v.getModele(),
                    v.getAnnee(), v.getPrixVente(), v.getPrixLocation(),
                    v.getCarburant(), v.getStatut(), v.isDispoVente(), v.isDispoLocation()
            });
        }
    }

    private void remplirFormulaire(ListSelectionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            tfMatricule.setText(tableModel.getValueAt(row, 1).toString());
            tfMarque.setText(tableModel.getValueAt(row, 2).toString());
            tfModele.setText(tableModel.getValueAt(row, 3).toString());
            tfAnnee.setText(tableModel.getValueAt(row, 4).toString());
            tfPrixVente.setText(tableModel.getValueAt(row, 5).toString());
            tfPrixLocation.setText(tableModel.getValueAt(row, 6).toString());
            cbCarburant.setSelectedItem(tableModel.getValueAt(row, 7).toString());
            cbStatut.setSelectedItem(tableModel.getValueAt(row, 8).toString());
            cbDispoVente.setSelected((boolean) tableModel.getValueAt(row, 9));
            cbDispoLocation.setSelected((boolean) tableModel.getValueAt(row, 10));
        }
    }

    private void clearForm() {
        tfMatricule.setText("");
        tfMarque.setText("");
        tfModele.setText("");
        tfAnnee.setText("");
        tfPrixVente.setText("");
        tfPrixLocation.setText("");
        cbCarburant.setSelectedIndex(0);
        cbStatut.setSelectedIndex(0);
        cbDispoVente.setSelected(false);
        cbDispoLocation.setSelected(false);
        table.clearSelection();
    }
}
