package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Libro;
import it.unisa.diem.bibliotecauniversitaria.model.Prestiti;
import it.unisa.diem.bibliotecauniversitaria.model.Utente;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import javafx.fxml.FXML;

public class GestionePrestiti {
    private List<Prestiti> prestiti;
    
    public GestionePrestiti() {}
    
    public Prestiti registraPrestito(Utente utente, Libro libro, LocalDate dataFine) {return null;}
    public void restituisciLibro(Utente utente, Libro libro) {}
    public List<Prestiti> prestitiUtente(Utente utente) {return null;}
    public List<Prestiti> prestitiAttiviOrdinati() {return null;}
    public List<Prestiti> getPrestiti() {return null;}
    
    /*
    @FXML
    private void switchToSecondary() throws IOException {
        Main.setRoot("secondary");
    }
    */
}
