package it.unisa.diem.bibliotecauniversitaria.model;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;

public class Utente {
    private String matricola;
    private String nome;
    private String cognome;
    private String email;
    private List<String> prestitiAttivi;
    
    public Utente(String matricola, String nome, String cognome, String email) {
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
    }
    
    public int NumeroPrestiti() { 
        return this.prestitiAttivi.size(); 
    }
    
    public String getMatricola() {
        return this.matricola;
    }
    
    public String getNome() {
        return this.nome;
    }
    
    public String getCognome() {
        return this.cognome;
    }
    
    public String getEmail() {
        return this.email;
    }
    
    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
 
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    /*public String[] getPrestitiAttivi(String matricola) {
        return null;
    }*/
    
    public void setPrestitiAttivi(String element) {
        this.prestitiAttivi.add(element);
    }
    
    /*
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
    */
}
