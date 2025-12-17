package it.unisa.diem.bibliotecauniversitaria.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Prestiti implements Serializable {
    private static final long serialVersionUID = 1L;

    private static int COUNTER = 0;

    private final int id;
    private String isbn;
    private String matricola;
    private LocalDate dataPrestito;
    private LocalDate dataRestituzionePrevista;
    private LocalDate dataRestituzioneEffettiva;
    private boolean sanzioneApplicata;

    public Prestiti(String isbn, String matricola, LocalDate dataPrestito) {
        this.id = ++COUNTER;
        this.isbn = isbn;
        this.matricola = matricola;
        this.dataPrestito = dataPrestito;
        this.sanzioneApplicata = false;
    }

    public int getId() {
        return id;
    }

    public String getISBN() {
        return isbn;
    }

    public String getMatricola() {
        return matricola;
    }

    public LocalDate getDataPrestito() {
        return dataPrestito;
    }

    public LocalDate getDataRestituzionePrevista() {
        return dataRestituzionePrevista;
    }

    public LocalDate getDataRestituzioneEffettiva() {
        return dataRestituzioneEffettiva;
    }

    public boolean isSanzioneApplicata() {
        return sanzioneApplicata;
    }

    public void setDataRestituzionePrevista(LocalDate dataRestituzionePrevista) {
        this.dataRestituzionePrevista = dataRestituzionePrevista;
    }

    public void setDataRestituzioneEffettiva(LocalDate dataRestituzioneEffettiva) {
        this.dataRestituzioneEffettiva = dataRestituzioneEffettiva;
    }

    public boolean isInLate() {
        return dataRestituzioneEffettiva != null &&
               dataRestituzionePrevista != null &&
               dataRestituzioneEffettiva.isAfter(dataRestituzionePrevista);
    }

    public void verificaSanzione() {
        if (isInLate()) {
            sanzioneApplicata = true;
        }
    }
}