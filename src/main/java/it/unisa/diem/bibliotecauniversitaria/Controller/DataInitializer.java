package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataInitializer {
    
    public static void initializeIfNeeded() throws IOException {
        // Controlla se i file esistono, altrimenti crea dati di esempio
        if (!new File("data/books.dat").exists()) {
            initializeBooks();
        }
        if (!new File("data/users.dat").exists()) {
            initializeUsers();
        }
        if (!new File("data/loans.dat").exists()) {
            initializeLoans();
        }
    }
    
    private static void initializeBooks() throws IOException {
        List<Author> autori1 = new ArrayList<>();
        autori1.add(new Author("Erich", "Gamma"));
        autori1.add(new Author("Richard", "Helm"));
        autori1.add(new Author("Ralph", "Johnson"));
        autori1.add(new Author("John", "Vlissides"));
        
        List<Author> autori2 = new ArrayList<>();
        autori2.add(new Author("Robert", "Martin"));
        
        List<Author> autori3 = new ArrayList<>();
        autori3.add(new Author("Umberto", "Eco"));
        
        List<Libro> libri = new ArrayList<>();
        libri.add(new Libro("Design Patterns", "978-0-201-63361-0", 1994, 5, autori1));
        libri.add(new Libro("Clean Code", "978-0-13-235088-4", 2008, 3, autori2));
        libri.add(new Libro("Il nome della rosa", "978-88-452-9002-0", 1980, 2, autori3));
        
        GestioneFile.salvaLibri(libri);
    }
    
    private static void initializeUsers() throws IOException {
        List<Utente> utenti = new ArrayList<>();
        utenti.add(new Utente("M001", "Rossi", "Mario", "mario.rossi@unisa.it"));
        utenti.add(new Utente("M002", "Bianchi", "Anna", "anna.bianchi@unisa.it"));
        utenti.add(new Utente("M003", "Verdi", "Luigi", "luigi.verdi@unisa.it"));
        
        GestioneFile.salvaUtenti(utenti);
    }
    
    private static void initializeLoans() throws IOException {
        // Inizializza con lista vuota
        GestioneFile.salvaPrestiti(new ArrayList<>());
    }
}
