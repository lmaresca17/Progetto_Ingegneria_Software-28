package it.unisa.diem.bibliotecauniversitaria.model;

import java.io.Serializable;

public class Author implements Serializable {
    private static final long serialVersionUID = 1L;
    
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
}