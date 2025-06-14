package org.example.interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        // Configuration de base de la fenêtre
        setTitle("Menu Principal - Gestion de Voitures");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal avec espacement
        setLayout(new BorderLayout(0, 20));
        ((JComponent)getContentPane()).setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Titre
        JLabel titre = new JLabel("Gestion de Voitures", SwingConstants.CENTER);
        titre.setFont(new Font("Tahoma", Font.BOLD, 22));
        titre.setForeground(new Color(0, 51, 102));
        add(titre, BorderLayout.NORTH);

        // Panel des boutons
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        // Création des boutons avec style Swing pur
        JButton btnVoitures = createStyledButton("📦 Gérer les voitures");
        JButton btnClients = createStyledButton("👤 Gérer les clients");
        JButton btnLocations = createStyledButton("🚗 Gérer les locations");
        JButton btnVentes = createStyledButton("🧾 Gérer les ventes");

        // Actions
        btnVoitures.addActionListener(e -> new VoitureUI());
        btnClients.addActionListener(e -> new ClientUI());
        btnLocations.addActionListener(e -> new LocationUI());
        btnVentes.addActionListener(e -> new VenteUI());

        buttonPanel.add(btnVoitures);
        buttonPanel.add(btnClients);
        buttonPanel.add(btnLocations);
        buttonPanel.add(btnVentes);

        add(buttonPanel, BorderLayout.CENTER);

        setVisible(true); // <-- Ligne ajoutée pour afficher la fenêtre
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);

        button.setFont(new Font("Tahoma", Font.PLAIN, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(240, 240, 240));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));

        // Effets hover
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(220, 240, 255));
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(100, 150, 200)),
                        BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(240, 240, 240));
                button.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        BorderFactory.createEmptyBorder(8, 15, 8, 15)
                ));
            }
        });

        return button;
    }
}