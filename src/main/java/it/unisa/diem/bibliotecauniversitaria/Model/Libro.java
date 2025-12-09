package it.unisa.diem.bibliotecauniversitaria.model;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;

public class Libro {
    private String titolo;
    private String isbn;
    private List<Author> author;
    private int annoPubblicazione;
    private int numeroCopieTotali;
    private int numeroCopieDisponibili;
    
    public Libro(String titolo, String isbn, int annoPubblicazione, int numeroCopieTotali) {
        this.isbn = isbn;
        this.titolo = titolo;
        this.numeroCopieTotali = numeroCopieTotali;
        this.annoPubblicazione = annoPubblicazione;
    }
    
    public boolean isAvailable() { 
        return this.numeroCopieDisponibili > 0; 
    }
   
    public String getTitolo() { 
        return this.titolo; 
    }
    
    public String getISBN() { 
        return this.isbn; 
    }
    
    public int getAnnoPubblicazione() { 
        return this.annoPubblicazione;
    }
    
    public int getnumeroCopieTotali() {
        return this.numeroCopieTotali;
    }
    
    public int getnumeroCopieDisponibili() { 
        return this.numeroCopieDisponibili; 
    }
    
    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }
    
    public void setISBN(String isbn) {
        this.isbn = isbn;
    }

    public void setAnnoPubblicazione(int annoPubblicazione) {
        this.annoPubblicazione = annoPubblicazione;
    }

    public void setNumeroCopieTotali(int numeroCopieTotali) {
        this.numeroCopieTotali = numeroCopieTotali;
    }
    

    public void setNumeroCopieDisponibili(int numeroCopieDisponibili) {
        this.numeroCopieDisponibili = numeroCopieDisponibili;
    }
    
    public void setAuthor(Author element) {
        this.author.add(element);
    }
    
    /*
    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
    */
}