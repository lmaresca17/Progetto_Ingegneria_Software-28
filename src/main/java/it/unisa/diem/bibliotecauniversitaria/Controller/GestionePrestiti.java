package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Libro;
import it.unisa.diem.bibliotecauniversitaria.model.Prestiti;
import it.unisa.diem.bibliotecauniversitaria.model.Utente;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;


public class GestionePrestiti {
    private List<Prestiti> prestiti;
    
    public GestionePrestiti() {
        this.prestiti = new ArrayList<>();    
        //Registra un nuovo prestito se il libro non è già in prestito attivo
    }
    
    public Prestiti registraPrestito(Utente utente, Libro libro, LocalDate dataFine) {
        for (Prestiti p : prestiti) {
            if (p.getISBN().equals(libro.getISBN()) && p.getDataRestituzioneEffettiva() == null) {
                System.out.println("Il libro è già in prestito.");
                return null;
            }
        }
        Prestiti nuovo = new Prestiti(libro.getISBN(), utente.getMatricola(), LocalDate.now());
        LocalDate dataFinePrevista = null;
        nuovo.setDataRestituzionePrevista(dataFinePrevista);
        // aggiorna disponibilità del libro
        libro.setNumeroCopieDisponibili(libro.getnumeroCopieDisponibili() - 1);
        prestiti.add(nuovo);
        utente.aggiungiPrestito(libro.getISBN());
        return nuovo;
    }
    public void restituisciLibro(Utente utente, Libro libro) {
        for (Prestiti p : prestiti) {
            if (p.getMatricola().equals(utente.getMatricola()) &&
                p.getISBN().equals(libro.getISBN()) &&
                p.getDataRestituzioneEffettiva() == null) {

                p.setDataRestituzioneEffettiva(LocalDate.now());

                // Se è in ritardo, applica la sanzione
                p.setSanzioneApplicata();
               
                // aggiorna copie disponibili
                libro.setNumeroCopieDisponibili(libro.getnumeroCopieDisponibili() + 1);

                return;
            }
        }
    }
    public List<Prestiti> prestitiUtente(Utente utente) {
        List<Prestiti> lista = new ArrayList<>();
        for (Prestiti p: prestiti) {
            if (p.getMatricola().equals(utente.getMatricola())) {
                lista.add(p);
            }
        }
        return lista;
    }
    public List<Prestiti> prestitiAttiviOrdinati() {
        List<Prestiti> attivi = new ArrayList<>();
        for (Prestiti p : prestiti) {
            if (p.getDataRestituzioneEffettiva() == null) {
                attivi.add(p);
            }
        }
         attivi.sort(Comparator.comparing(Prestiti::getDataRestituzionePrevista));
        return attivi;
    }
    public List<Prestiti> getPrestiti() {
        return prestiti;
    }
}