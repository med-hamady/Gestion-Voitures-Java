package org.example.interfaces;

import org.example.dao.UtilisateurDAO;
import org.example.model.Utilisateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginUI extends JFrame {

    private final JTextField tfIdentifiant;
    private final JPasswordField pfMotDePasse;
    private final UtilisateurDAO utilisateurDAO = new UtilisateurDAO();

    public LoginUI() {
        setTitle("Connexion");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridLayout(3, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 10, 30));

        tfIdentifiant = new JTextField();
        pfMotDePasse = new JPasswordField();

        panel.add(new JLabel("Identifiant :"));
        panel.add(tfIdentifiant);
        panel.add(new JLabel("Mot de passe :"));
        panel.add(pfMotDePasse);

        JButton btnConnexion = new JButton("Connexion");
        btnConnexion.setBackground(new Color(30, 144, 255));
        btnConnexion.setForeground(Color.WHITE);
        btnConnexion.setFocusPainted(false);
        btnConnexion.setFont(new Font("Arial", Font.BOLD, 14));
        btnConnexion.addActionListener(this::seConnecter);

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnConnexion);

        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void seConnecter(ActionEvent e) {
        String identifiant = tfIdentifiant.getText().trim();
        String motDePasse = new String(pfMotDePasse.getPassword());

        if (identifiant.isEmpty() || motDePasse.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.");
            return;
        }

        Utilisateur utilisateur = utilisateurDAO.seConnecter(identifiant, motDePasse);
        if (utilisateur != null) {
            JOptionPane.showMessageDialog(this, "Connexion réussie !");
            dispose();

            // Redirection selon le rôle
            if ("admin".equalsIgnoreCase(utilisateur.getRole())) {
                new MenuPrincipal(); // Menu pour admin
            } else {
                new MenuUtilisateur(utilisateur); // ✅ Utilisateur simple
            }

        } else {
            JOptionPane.showMessageDialog(this, "Identifiants invalides.");
        }
    }
}
