package org.example.interfaces;

import org.example.dao.ClientDAO;
import org.example.dao.LocationDAO;
import org.example.dao.VoitureDAO;
import org.example.exception.InvalidDateRangeException;
import org.example.model.Client;
import org.example.model.Location;
import org.example.model.Voiture;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class LocationUI extends JFrame {

    private JComboBox<Client> cbClient;
    private JComboBox<Voiture> cbVoiture;
    private JTextField tfDateDebut, tfDateFin, tfPermisClient, tfPrixTotal;
    private JTable table;
    private DefaultTableModel tableModel;

    private final ClientDAO clientDAO = new ClientDAO();
    private final VoitureDAO voitureDAO = new VoitureDAO();
    private final LocationDAO locationDAO = new LocationDAO();

    public LocationUI() {
        setTitle("🟦 Location de Voitures");
        setSize(1000, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(245, 248, 255));
        add(mainPanel);

        // ==== Formulaire ====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder("Nouvelle Location"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbClient = new JComboBox<>();
        cbVoiture = new JComboBox<>();
        tfPermisClient = new JTextField(); tfPermisClient.setEditable(false);
        tfDateDebut = new JTextField(LocalDate.now().toString());
        tfDateFin = new JTextField();
        tfPrixTotal = new JTextField(); tfPrixTotal.setEditable(false);

        addField(formPanel, gbc, 0, "Client :", cbClient);
        addField(formPanel, gbc, 1, "Permis du client :", tfPermisClient);
        addField(formPanel, gbc, 2, "Voiture :", cbVoiture);
        addField(formPanel, gbc, 3, "Date Début :", tfDateDebut);
        addField(formPanel, gbc, 4, "Date Fin :", tfDateFin);
        addField(formPanel, gbc, 5, "Prix total estimé (€) :", tfPrixTotal);

        // ==== Boutons ====
        JButton btnAjouter = createStyledButton("Ajouter", new Color(46, 204, 113));
        JButton btnModifier = createStyledButton("Modifier", new Color(52, 152, 219));
        JButton btnSupprimer = createStyledButton("Supprimer", new Color(231, 76, 60));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);

        JPanel topSection = new JPanel(new BorderLayout());
        topSection.setOpaque(false);
        topSection.add(formPanel, BorderLayout.CENTER);
        topSection.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(topSection, BorderLayout.NORTH);

        // ==== Tableau ====
        tableModel = new DefaultTableModel(
                new String[]{"ID", "Client", "Voiture", "Date Début", "Date Fin", "Prix Total (€)", "Statut Voiture"}, 0
        );
        table = new JTable(tableModel);
        table.setRowHeight(22);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Historique des Locations"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ==== Listeners ====
        btnAjouter.addActionListener(this::ajouterLocation);
        btnModifier.addActionListener(this::modifierLocation);
        btnSupprimer.addActionListener(this::supprimerLocation);
        table.getSelectionModel().addListSelectionListener(this::remplirFormulaire);
        cbClient.addActionListener(e -> {
            Client c = (Client) cbClient.getSelectedItem();
            tfPermisClient.setText(c != null ? c.getPermisId() : "");
        });
        cbVoiture.addActionListener(e -> calculerPrixTotal());
        tfDateDebut.addActionListener(e -> calculerPrixTotal());
        tfDateFin.addActionListener(e -> calculerPrixTotal());

        chargerClients();
        chargerVoitures();
        chargerLocations();
        setVisible(true);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent field) {
        gbc.gridy = y;
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(field, gbc);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(120, 35));
        return btn;
    }

    private void ajouterLocation(ActionEvent e) {
        try {
            LocalDate debut = parseDate(tfDateDebut.getText().trim(), "début", false);
            LocalDate fin = parseDate(tfDateFin.getText().trim(), "fin", false);
            Client client = (Client) cbClient.getSelectedItem();
            Voiture voiture = (Voiture) cbVoiture.getSelectedItem();
            if (client == null || voiture == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client et une voiture.");
                return;
            }
            double total = calculerPrixTotalPourEnregistrement();
            Location location = new Location(0, voiture.getId(), client.getId(), debut, fin, total);
            location.valider();
            locationDAO.ajouter(location);
            chargerLocations();
            chargerVoitures();
            JOptionPane.showMessageDialog(this, "✅ Location ajoutée !");
            clearForm();
        } catch (InvalidDateRangeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierLocation(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            try {
                int id = (int) tableModel.getValueAt(row, 0);
                LocalDate debut = parseDate(tfDateDebut.getText().trim(), "début", false);
                LocalDate fin = parseDate(tfDateFin.getText().trim(), "fin", false);
                Client client = (Client) cbClient.getSelectedItem();
                Voiture voiture = (Voiture) cbVoiture.getSelectedItem();
                if (client == null || voiture == null) return;
                double total = calculerPrixTotalPourEnregistrement();
                Location location = new Location(id, voiture.getId(), client.getId(), debut, fin, total);
                location.valider();
                locationDAO.modifier(location);
                chargerLocations();
                chargerVoitures();
                JOptionPane.showMessageDialog(this, "✅ Location modifiée !");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void supprimerLocation(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Suppression", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                locationDAO.supprimer(id);
                chargerLocations();
                chargerVoitures();
                clearForm();
            }
        }
    }

    private LocalDate parseDate(String text, String label, boolean silent) {
        try {
            return LocalDate.parse(text, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException ex) {
            if (!silent) {
                JOptionPane.showMessageDialog(this,
                        "❌ Erreur : la date " + label + " doit être au format AAAA-MM-JJ\n(ex : 2025-05-07)");
            }
            throw ex;
        }
    }

    private void calculerPrixTotal() {
        try {
            Voiture voiture = (Voiture) cbVoiture.getSelectedItem();
            LocalDate debut = parseDate(tfDateDebut.getText().trim(), "début", true);
            LocalDate fin = parseDate(tfDateFin.getText().trim(), "fin", true);
            if (voiture != null && !fin.isBefore(debut)) {
                long jours = java.time.temporal.ChronoUnit.DAYS.between(debut, fin) + 1;
                double total = jours * voiture.getPrixLocation();
                tfPrixTotal.setText(String.format("%.2f", total));
            } else {
                tfPrixTotal.setText("");
            }
        } catch (Exception ignored) {
            tfPrixTotal.setText("");
        }
    }

    private double calculerPrixTotalPourEnregistrement() {
        try {
            Voiture voiture = (Voiture) cbVoiture.getSelectedItem();
            LocalDate debut = parseDate(tfDateDebut.getText().trim(), "début", true);
            LocalDate fin = parseDate(tfDateFin.getText().trim(), "fin", true);
            if (voiture != null && !fin.isBefore(debut)) {
                long jours = java.time.temporal.ChronoUnit.DAYS.between(debut, fin) + 1;
                return jours * voiture.getPrixLocation();
            }
        } catch (Exception ignored) {}
        return 0;
    }

    private void chargerClients() {
        cbClient.removeAllItems();
        for (Client c : clientDAO.lister()) cbClient.addItem(c);
    }

    private void chargerVoitures() {
        cbVoiture.removeAllItems();
        for (Voiture v : voitureDAO.lister()) {
            if ("Disponible".equalsIgnoreCase(v.getStatut())) cbVoiture.addItem(v);
        }
    }

    private void chargerLocations() {
        tableModel.setRowCount(0);
        for (Location loc : locationDAO.lister()) {
            Voiture v = voitureDAO.getById(loc.getVoitureId());
            Client c = clientDAO.getById(loc.getClientId());
            tableModel.addRow(new Object[]{
                    loc.getId(),
                    c != null ? c.toString() : loc.getClientId(),
                    v != null ? v.toString() : loc.getVoitureId(),
                    loc.getDateDebut(),
                    loc.getDateFin(),
                    loc.getPrixTotal(),
                    v != null ? v.getStatut() : "Inconnu"
            });
        }
    }

    private void remplirFormulaire(ListSelectionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            tfDateDebut.setText(tableModel.getValueAt(row, 3).toString());
            tfDateFin.setText(tableModel.getValueAt(row, 4).toString());
            tfPrixTotal.setText(tableModel.getValueAt(row, 5).toString());
        }
    }

    private void clearForm() {
        tfDateDebut.setText(LocalDate.now().toString());
        tfDateFin.setText("");
        tfPermisClient.setText("");
        tfPrixTotal.setText("");
        table.clearSelection();
    }
}
