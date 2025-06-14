package org.example.dao;

import java.util.List;

public interface Gestion<T> {
    void ajouter(T objet);
    List<T> lister();
    void modifier(T objet);
    void supprimer(int id);
    T getById(int id);
}

