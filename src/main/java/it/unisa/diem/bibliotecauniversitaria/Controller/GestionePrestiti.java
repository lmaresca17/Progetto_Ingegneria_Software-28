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

    public GestionePrestiti(List<Prestiti> prestiti) {
        this.prestiti = prestiti;
    }
    
    public boolean inserisciPrestito(Prestiti prestito) {
        if(prestito==null) return false;
        if(prestito.getId() == 0) return false;
        if(prestiti.contains(prestito)) return false;
        prestiti.add(prestito);
        return true;
    }

    public Prestiti registraPrestito(Utente utente, Libro libro, LocalDate dataScadenza) {
        // Controllo libro già in prestito
        for (Prestiti p : prestiti) {
            if (p.getISBN().equals(libro.getISBN()) && p.getDataRestituzioneEffettiva() == null) {
                return null;
            }
        }

        if (libro.getnumeroCopieDisponibili() <= 0) return null;

        Prestiti nuovo = new Prestiti(libro.getISBN(), utente.getMatricola(), LocalDate.now());
        nuovo.setDataRestituzionePrevista(dataScadenza);

        prestiti.add(nuovo);
        libro.setNumeroCopieDisponibili(libro.getnumeroCopieDisponibili() - 1);
        utente.aggiungiPrestito(libro.getISBN());

        return nuovo;
    }
    
    public boolean restituisciLibro(int id, Libro libro) {
        for (int i = 0; i < prestiti.size(); i++) {
            Prestiti p = prestiti.get(i);
            if (p.getId() == id && p.getISBN().equals(libro.getISBN()) &&
                p.getDataRestituzioneEffettiva() == null) {

                p.setDataRestituzioneEffettiva(LocalDate.now());
                p.verificaSanzione();
                libro.setNumeroCopieDisponibili(libro.getnumeroCopieDisponibili() + 1);
                prestiti.remove(i);  // ✅ Rimozione sicura con indice
                return true;
            }
        }
        return false;
    }

    public List<Prestiti> prestitiUtente(String matricola) {
        List<Prestiti> result = new ArrayList<>();
        for (Prestiti p : prestiti) {
            if (p.getMatricola().equals(matricola)) result.add(p);
        }
        return result;
    }

    public List<Prestiti> prestitiAttiviOrdinati() {
        List<Prestiti> attivi = new ArrayList<>();
        for (Prestiti p : prestiti) {
            if (p.getDataRestituzioneEffettiva() == null) attivi.add(p);
        }
        attivi.sort(Comparator.comparing(Prestiti::getDataRestituzionePrevista));
        return attivi;
    }

    public List<Prestiti> getPrestiti() {
        return prestiti;
    }
}