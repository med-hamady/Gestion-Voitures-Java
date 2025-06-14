# 🚗 Gestion de Voitures - Java Swing + MySQL

Ce projet est une application de bureau développée en **Java Swing** avec une base de données **MySQL**.  
Elle permet la gestion complète des opérations liées aux voitures ainsi qu’une interface pour les utilisateurs simples afin de visualiser les voitures disponibles à la vente ou à la location.

---

## 🛠️ Fonctionnalités principales

### 👤 Utilisateur simple (espace public)
- 🔎 Visualiser la liste des voitures disponibles
- 📋 Consulter les détails d’une voiture
- 🚪 Interface de connexion dédiée (email + mot de passe)

### 🔐 Administrateur
- 🚘 Ajouter / Modifier / Supprimer des voitures
- 🧾 Gérer les ventes et locations
- 👥 Gérer les clients
- 📊 Voir les statistiques (nombre de voitures, disponibles, vendues, louées)
- 🔒 Connexion sécurisée admin

---

## 🖥️ Technologies utilisées

- **Java SE**
- **Swing** (interfaces graphiques)
- **MySQL**
- **JDBC**
- **IntelliJ IDEA**

---

---

## ⚙️ Configuration de la base de données

1. Créer une base `voitures_db`
2. Importer `voitures_db.sql` via phpMyAdmin
3. Vérifier les informations de connexion JDBC :
   ```java
   String url = "jdbc:mysql://localhost:3306/voitures_db";
   String user = "root";
   String password = "";


📄 Auteur
med-hamady
GitHub : github.com/med-hamady

