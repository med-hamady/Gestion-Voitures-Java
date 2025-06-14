package org.example;

import org.example.interfaces.LoginUI;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(LoginUI::new);
    }
}
