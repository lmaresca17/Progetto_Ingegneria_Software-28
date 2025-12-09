package it.unisa.diem.bibliotecauniversitaria.model;

import java.io.IOException;
import java.time.LocalDate;
import javafx.fxml.FXML;

public class Prestiti {
    private static int id = 0;
    private String isbn;
    private String matricola;
    private LocalDate dataPrestito;
    private LocalDate dataRestituzionePrevista;
    private LocalDate dataRestituzioneEffettiva;
    private boolean sanzioneApplicata;
    
    public Prestiti(String isbn, String matricola, LocalDate dataPrestito) {
        this.isbn = isbn;
        this.id = this.id++;
        this.matricola = matricola;
        this.dataPrestito = dataPrestito;
        this.sanzioneApplicata = false;
    }
    
    public boolean isInLate() {
        return this.dataRestituzioneEffettiva.isAfter(this.dataRestituzionePrevista);
    }
    
    public String getISBN() { 
        return this.isbn;
    }
    
    public String getMatricola() { 
        return this.matricola; 
    }
    
    public LocalDate getDataPrestito() { 
        return this.dataPrestito; 
    }
    
    public LocalDate getDataRestituzioneEffettiva() { 
        return this.dataRestituzioneEffettiva; 
    }
    
    public LocalDate getDataRestituzionePrevista() { 
        return this.dataRestituzionePrevista;
    }
    
    public void setISBN(String isbn) {
        this.isbn = isbn;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }
    
    public void setDataPrestito(LocalDate dataPrestito) {
        this.dataPrestito = dataPrestito;
    }
    
    public void setDataRestituzionePrevista(LocalDate dataRestituzionePrevista) { 
        this.dataRestituzionePrevista = dataRestituzionePrevista;
    }

    public void setDataRestituzioneEffettiva(LocalDate dataRestituzioneEffettiva) {
        this.dataRestituzioneEffettiva = dataRestituzioneEffettiva;
    }
    public boolean isSanzioneApplicata() {
        return this.sanzioneApplicata;
    }
    
    public void setSanzioneApplicata() {
        if (this.isInLate()){
            this.sanzioneApplicata = true;
        }
    }
    
    /*
    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
    */
}
