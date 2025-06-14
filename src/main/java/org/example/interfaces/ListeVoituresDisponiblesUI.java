package org.example.interfaces;

import org.example.dao.VoitureDAO;
import org.example.model.Voiture;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class ListeVoituresDisponiblesUI extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;
    private final VoitureDAO voitureDAO = new VoitureDAO();

    public ListeVoituresDisponiblesUI() {
        setTitle("Voitures Disponibles");
        setSize(900, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("🚗 Liste des Voitures Disponibles", JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        headerLabel.setForeground(new Color(30, 60, 120));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 10, 0));
        add(headerLabel, BorderLayout.NORTH);

        String[] colonnes = {
                "ID", "Matricule", "Marque", "Modèle", "Année",
                "Prix Vente (€)", "Prix Location (€)", "Carburant", "Statut"
        };
        tableModel = new DefaultTableModel(colonnes, 0);
        table = new JTable(tableModel);
        table.setRowHeight(24);
        table.setFont(new Font("SansSerif", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 14));
        table.getTableHeader().setBackground(new Color(200, 220, 255));
        table.setGridColor(Color.LIGHT_GRAY);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Voitures disponibles à la vente ou à la location"));
        add(scrollPane, BorderLayout.CENTER);

        chargerVoituresDisponibles();
        setVisible(true);
    }

    private void chargerVoituresDisponibles() {
        tableModel.setRowCount(0);
        List<Voiture> liste = voitureDAO.lister();
        for (Voiture v : liste) {
            if ("Disponible".equalsIgnoreCase(v.getStatut())) {
                tableModel.addRow(new Object[]{
                        v.getId(), v.getMatricule(), v.getMarque(), v.getModele(),
                        v.getAnnee(), v.getPrixVente(), v.getPrixLocation(),
                        v.getCarburant(), v.getStatut()
                });
            }
        }
    }
}
