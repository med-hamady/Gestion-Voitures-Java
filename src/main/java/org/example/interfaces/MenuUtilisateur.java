package org.example.interfaces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import org.example.model.Utilisateur;

public class MenuUtilisateur extends JFrame {

    private final Utilisateur utilisateur;

    public MenuUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;

        setTitle("Espace Utilisateur - " + utilisateur.getIdentifiant());
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Bienvenue, " + utilisateur.getIdentifiant(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        JButton btnListerVoitures = new JButton("Voir les voitures disponibles");
        btnListerVoitures.setFont(new Font("Arial", Font.PLAIN, 16));
        btnListerVoitures.setBackground(new Color(60, 179, 113));
        btnListerVoitures.setForeground(Color.WHITE);
        btnListerVoitures.setFocusPainted(false);
        btnListerVoitures.addActionListener(this::ouvrirListeVoitures);

        JButton btnQuitter = new JButton("Quitter");
        btnQuitter.setFont(new Font("Arial", Font.PLAIN, 16));
        btnQuitter.setBackground(new Color(220, 53, 69));
        btnQuitter.setForeground(Color.WHITE);
        btnQuitter.setFocusPainted(false);
        btnQuitter.addActionListener(e -> System.exit(0));

        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        centerPanel.add(btnListerVoitures);
        centerPanel.add(btnQuitter);

        add(centerPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    private void ouvrirListeVoitures(ActionEvent e) {
        new ListeVoituresDisponiblesUI();
    }
}
