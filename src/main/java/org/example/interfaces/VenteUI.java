package org.example.interfaces;

import org.example.dao.ClientDAO;
import org.example.dao.VenteDAO;
import org.example.dao.VoitureDAO;
import org.example.model.Client;
import org.example.model.Vente;
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

public class VenteUI extends JFrame {

    private JComboBox<Client> cbClient;
    private JComboBox<Voiture> cbVoiture;
    private JTextField tfDateVente, tfPrix;
    private JTable table;
    private DefaultTableModel tableModel;

    private final ClientDAO clientDAO = new ClientDAO();
    private final VoitureDAO voitureDAO = new VoitureDAO();
    private final VenteDAO venteDAO = new VenteDAO();

    public VenteUI() {
        setTitle("🟨 Vente de Voitures");
        setSize(900, 600);
        setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        mainPanel.setBackground(new Color(250, 250, 250));
        setContentPane(mainPanel);

        // ==== Formulaire ====
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setOpaque(false);
        formPanel.setBorder(BorderFactory.createTitledBorder("Nouvelle Vente"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        cbClient = new JComboBox<>();
        cbVoiture = new JComboBox<>();
        tfDateVente = new JTextField(LocalDate.now().toString());
        tfPrix = new JTextField();

        addField(formPanel, gbc, 0, "Client :", cbClient);
        addField(formPanel, gbc, 1, "Voiture :", cbVoiture);
        addField(formPanel, gbc, 2, "Date Vente (AAAA-MM-JJ) :", tfDateVente);
        addField(formPanel, gbc, 3, "Prix (€) :", tfPrix);

        // ==== Boutons ====
        JButton btnAjouter = createStyledButton("Ajouter", new Color(39, 174, 96));
        JButton btnModifier = createStyledButton("Modifier", new Color(41, 128, 185));
        JButton btnSupprimer = createStyledButton("Supprimer", new Color(192, 57, 43));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // ==== Tableau ====
        tableModel = new DefaultTableModel(new String[]{"ID", "Client", "Voiture", "Date", "Prix (€)"}, 0);
        table = new JTable(tableModel);
        table.setRowHeight(22);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Historique des Ventes"));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // ==== Actions ====
        btnAjouter.addActionListener(this::ajouterVente);
        btnModifier.addActionListener(this::modifierVente);
        btnSupprimer.addActionListener(this::supprimerVente);
        table.getSelectionModel().addListSelectionListener(this::remplirFormulaire);

        chargerClients();
        chargerVoitures();
        chargerVentes();

        cbVoiture.addActionListener(e -> {
            Voiture voiture = (Voiture) cbVoiture.getSelectedItem();
            if (voiture != null) {
                tfPrix.setText(String.valueOf(voiture.getPrixVente()));
            }
        });


        setVisible(true);
    }

    private void addField(JPanel panel, GridBagConstraints gbc, int y, String label, JComponent comp) {
        gbc.gridy = y;
        gbc.gridx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        panel.add(comp, gbc);
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

    private void ajouterVente(ActionEvent e) {
        try {
            LocalDate date = parseDate(tfDateVente.getText().trim());
            Client client = (Client) cbClient.getSelectedItem();
            Voiture voiture = (Voiture) cbVoiture.getSelectedItem();
            double prix = Double.parseDouble(tfPrix.getText().trim());

            if (client == null || voiture == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client et une voiture.");
                return;
            }

            Vente vente = new Vente(0, voiture.getId(), client.getId(), date, prix);
            venteDAO.ajouter(vente);
            chargerVentes();
            chargerVoitures();
            JOptionPane.showMessageDialog(this, "✅ Vente enregistrée !");
            clearForm();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void modifierVente(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            try {
                int id = (int) tableModel.getValueAt(row, 0);
                LocalDate date = parseDate(tfDateVente.getText().trim());
                Client client = (Client) cbClient.getSelectedItem();
                Voiture voiture = (Voiture) cbVoiture.getSelectedItem();
                double prix = Double.parseDouble(tfPrix.getText().trim());

                if (client == null || voiture == null) {
                    JOptionPane.showMessageDialog(this, "Sélectionnez un client et une voiture.");
                    return;
                }

                Vente vente = new Vente(id, voiture.getId(), client.getId(), date, prix);
                venteDAO.modifier(vente);
                chargerVentes();
                chargerVoitures();
                JOptionPane.showMessageDialog(this, "✅ Vente modifiée !");
                clearForm();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            }
        }
    }

    private void supprimerVente(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Confirmer la suppression ?", "Suppression", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                venteDAO.supprimer(id);
                chargerVentes();
                chargerVoitures();
                clearForm();
            }
        }
    }

    private void remplirFormulaire(ListSelectionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            tfDateVente.setText(tableModel.getValueAt(row, 3).toString());
            tfPrix.setText(tableModel.getValueAt(row, 4).toString());
        }
    }

    private LocalDate parseDate(String dateText) throws DateTimeParseException {
        return LocalDate.parse(dateText, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    private void chargerClients() {
        cbClient.removeAllItems();
        for (Client c : clientDAO.lister()) cbClient.addItem(c);
    }

    private void chargerVoitures() {
        cbVoiture.removeAllItems();
        for (Voiture v : voitureDAO.lister()) {
            if (v.isDispoVente() && !"Vendu".equalsIgnoreCase(v.getStatut())) {
                cbVoiture.addItem(v);
            }
        }
    }

    private void chargerVentes() {
        tableModel.setRowCount(0);
        for (Vente v : venteDAO.lister()) {
            Client c = clientDAO.getById(v.getClientId());
            Voiture voiture = voitureDAO.getById(v.getVoitureId());
            tableModel.addRow(new Object[]{
                    v.getId(),
                    c != null ? c.getNom() + " " + c.getPrenom() : v.getClientId(),
                    voiture != null ? voiture.getMatricule() + " - " + voiture.getModele() : v.getVoitureId(),
                    v.getDateVente(),
                    v.getPrix()
            });
        }
    }

    private void clearForm() {
        tfDateVente.setText(LocalDate.now().toString());
        tfPrix.setText("");
        cbVoiture.setSelectedIndex(-1);
        cbClient.setSelectedIndex(-1);
        table.clearSelection();
    }
}
