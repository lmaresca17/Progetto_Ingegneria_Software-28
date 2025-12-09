package it.unisa.diem.bibliotecauniversitaria.model;

import java.io.IOException;
import javafx.fxml.FXML;

public class Author {
    private String nome;
    private String cognome;
    
    public Author(String nome, String cognome) {
        this.nome = nome;
        this.cognome = cognome;
    }
    
    public String getNome() {
        return this.nome;
    }
    
    public String getCognome() {
        return this.cognome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public void setCognome(String cognome) {
        this.cognome = cognome;
    }
    
    /*
    @FXML
    private void switchToSecondary() throws IOException {
        Main.setRoot("secondary");
    }
    */
}
