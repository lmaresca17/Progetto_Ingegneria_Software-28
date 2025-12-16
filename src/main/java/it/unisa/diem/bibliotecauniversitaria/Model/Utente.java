package it.unisa.diem.bibliotecauniversitaria.model;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Utente implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String matricola;
    private String cognome;
    private String nome;
    private String email;
    private List<String> prestitiAttivi;
    
    public Utente(String matricola, String cognome, String nome, String email) {
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        prestitiAttivi = new ArrayList<>();
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
    
    public List<String> getPrestitiAttivi() {
        return this.prestitiAttivi;
    }
    
    public void setPrestitiAttivi(List<String> prestitiAttivi) {
        this.prestitiAttivi = prestitiAttivi;
    }
    
    public void aggiungiPrestito(String prestito) {
        this.prestitiAttivi.add(prestito);
    }

    // Metodo comodo per rimuovere un prestito
    public void rimuoviPrestito(String prestito) {
        this.prestitiAttivi.remove(prestito);
    }
}
