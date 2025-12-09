package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Utente;
import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;

public class GestioneUtente {
    private List<Utente> utenti;
    
    public GestioneUtente() {}
    public void inserisciUtente(Utente utente) {}
    public void modificaUtente(String matricola, Utente utenteAggiornato) {}
    public boolean cancellaUtente(String matricola) {return false;}
    public List<Utente> cercaPerCognome(String cognome) {return null;}
    public Utente cercaPerMatricols(String matricola) {return null;}
    public List<Utente> listaUtentiOrdinata() {return null;}
    public List<Utente> getUtenti() {return null;}
    
    /*
    @FXML
    private void switchToSecondary() throws IOException {
        Main.setRoot("secondary");
    }
    */
}
