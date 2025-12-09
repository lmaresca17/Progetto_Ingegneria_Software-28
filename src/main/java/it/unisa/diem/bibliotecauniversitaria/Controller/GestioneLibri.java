package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Libro;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;

public class GestioneLibri {
    private List<Libro> libri;
    
    public GestioneLibri() {}
    
    public void inserisciLibro(Libro libro) {}
    public void modificaLibro(String isbn, Libro nuoviDati) {}
    public boolean cancellaLibro(String isbn) {return false;}
    public List<Libro> cercaPerTitolo(String titolo) {return null;}
    public List<Libro> cercaPerAuthor(String nomeAuthor) {return null;}
    public Libro cercaPerISBN(String isbn) {return null;}
    public List<Libro> listaLibriOrdinati() {return null;}
    public List<Libro> getLibri() {return null;}
    
    /*
    @FXML
    private void switchToSecondary() throws IOException {
        Main.setRoot("secondary");
    }
    */
}
