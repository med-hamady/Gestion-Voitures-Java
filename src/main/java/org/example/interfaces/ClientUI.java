package org.example.interfaces;

import org.example.dao.ClientDAO;
import org.example.model.Client;
import org.example.exception.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class ClientUI extends JFrame {

    private JTextField tfNom, tfPrenom, tfTelephone, tfEmail, tfPermisId;
    private JTable table;
    private DefaultTableModel tableModel;
    private ClientDAO clientDAO = new ClientDAO();

    public ClientUI() {
        // Configuration de la fenêtre
        setTitle("Gestion des Clients");
        setSize(900, 600);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(240, 240, 240));

        // Panel principal avec marges
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Panel du formulaire
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "Informations Client"));
        formPanel.setBackground(Color.WHITE);

        // Création des champs avec style uniforme
        tfNom = createStyledTextField();
        tfPrenom = createStyledTextField();
        tfTelephone = createStyledTextField();
        tfEmail = createStyledTextField();
        tfPermisId = createStyledTextField();

        // Ajout des composants au formulaire
        formPanel.add(createStyledLabel("Nom :"));
        formPanel.add(tfNom);
        formPanel.add(createStyledLabel("Prénom :"));
        formPanel.add(tfPrenom);
        formPanel.add(createStyledLabel("Téléphone :"));
        formPanel.add(tfTelephone);
        formPanel.add(createStyledLabel("Email :"));
        formPanel.add(tfEmail);
        formPanel.add(createStyledLabel("Permis ID :"));
        formPanel.add(tfPermisId);

        // Boutons avec style amélioré
        JButton btnAjouter = createActionButton("Ajouter", new Color(70, 130, 180));
        JButton btnModifier = createActionButton("Modifier", new Color(60, 179, 113));
        JButton btnSupprimer = createActionButton("Supprimer", new Color(220, 80, 80));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(btnAjouter);
        buttonPanel.add(btnModifier);
        buttonPanel.add(btnSupprimer);

        // Panel supérieur regroupant formulaire et boutons
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Configuration du tableau
        String[] colonnes = {"ID", "Nom", "Prénom", "Téléphone", "Email", "Permis ID"};
        tableModel = new DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tableau non modifiable
            }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Tahoma", Font.BOLD, 12));
        table.setRowHeight(25);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setShowGrid(false);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(150, 150, 150)),
                "Liste des Clients"));
        scrollPane.setBackground(Color.WHITE);

        // Assemblage des composants
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel);

        // Gestion des événements
        btnAjouter.addActionListener(this::ajouterClient);
        btnModifier.addActionListener(this::modifierClient);
        btnSupprimer.addActionListener(this::supprimerClient);
        table.getSelectionModel().addListSelectionListener(this::remplirFormulaire);

        // Chargement initial des données
        chargerClients();
        setVisible(true);
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Tahoma", Font.PLAIN, 14));
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        return textField;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Tahoma", Font.BOLD, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    private void ajouterClient(ActionEvent e) {
        try {
            String nom = tfNom.getText().trim();
            String prenom = tfPrenom.getText().trim();
            String telephone = tfTelephone.getText().trim();
            String email = tfEmail.getText().trim();
            String permisId = tfPermisId.getText().trim();

            Client c = new Client(0, nom, prenom, telephone, email, permisId);
            c.valider();
            clientDAO.ajouter(c);
            chargerClients();
            JOptionPane.showMessageDialog(this, "Client ajouté avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
        } catch (InvalidTelephoneException | InvalidEmailException | InvalidPermisException | AppException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierClient(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            try {
                int id = (int) tableModel.getValueAt(row, 0);
                String nom = tfNom.getText().trim();
                String prenom = tfPrenom.getText().trim();
                String telephone = tfTelephone.getText().trim();
                String email = tfEmail.getText().trim();
                String permisId = tfPermisId.getText().trim();

                Client c = new Client(id, nom, prenom, telephone, email, permisId);
                c.valider();
                clientDAO.modifier(c);
                chargerClients();
                JOptionPane.showMessageDialog(this, "Client modifié avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
            } catch (InvalidTelephoneException | InvalidEmailException | InvalidPermisException | AppException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client à modifier.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void supprimerClient(ActionEvent e) {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) tableModel.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Êtes-vous sûr de vouloir supprimer ce client?",
                    "Confirmation de suppression",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                clientDAO.supprimer(id);
                chargerClients();
                clearForm();
                JOptionPane.showMessageDialog(this, "Client supprimé avec succès!", "Succès", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un client à supprimer.", "Avertissement", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void remplirFormulaire(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int row = table.getSelectedRow();
            if (row != -1) {
                tfNom.setText(tableModel.getValueAt(row, 1).toString());
                tfPrenom.setText(tableModel.getValueAt(row, 2).toString());
                tfTelephone.setText(tableModel.getValueAt(row, 3).toString());
                tfEmail.setText(tableModel.getValueAt(row, 4).toString());
                tfPermisId.setText(tableModel.getValueAt(row, 5).toString());
            }
        }
    }

    private void chargerClients() {
        tableModel.setRowCount(0);
        List<Client> liste = clientDAO.lister();
        for (Client c : liste) {
            tableModel.addRow(new Object[]{
                    c.getId(), c.getNom(), c.getPrenom(), c.getTelephone(), c.getEmail(), c.getPermisId()
            });
        }
    }

    private void clearForm() {
        tfNom.setText("");
        tfPrenom.setText("");
        tfTelephone.setText("");
        tfEmail.setText("");
        tfPermisId.setText("");
        table.clearSelection();
    }
}