package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Libro;
import it.unisa.diem.bibliotecauniversitaria.model.Prestiti;
import it.unisa.diem.bibliotecauniversitaria.model.Utente;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;

public class GestioneFile {
    public void salvaUtenti(List<Utente> utenti) {}
    public void salvaLibri(List<Libro> libri) {}
    public void salvaPrestiti(List<Prestiti> prestiti) {}
    
    public List<Utente> leggiUtenti() {return null;}
    public List<Libro> leggiLibri() {return null;}
    public List<Prestiti> leggiPrestiti() {return null;}
    
    /*
    @FXML
    private void switchToSecondary() throws IOException {
        Main.setRoot("secondary");
    }
    */
}
