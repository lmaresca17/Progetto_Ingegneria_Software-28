package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Libro;
import it.unisa.diem.bibliotecauniversitaria.model.Prestiti;
import it.unisa.diem.bibliotecauniversitaria.model.Utente;
import java.io.*;
import java.util.List;
import java.util.ArrayList;

public class GestioneFile {
    private static final String FILE_BOOK = "data/books.dat";
    private static final String FILE_USER = "data/users.dat";
    private static final String FILE_LOAN = "data/loans.dat";
    
    public static void salvaUtenti(List<Utente> utenti) throws IOException {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdir();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_USER))) {
            oos.writeObject(utenti);
            System.out.println("Utenti salvati con successo!");
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio utenti: " + e.getMessage());
            throw e;
        }
    }
    
    public static void salvaLibri(List<Libro> libri) throws IOException {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdir();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_BOOK))) {
            oos.writeObject(libri);
            System.out.println("Libri salvati con successo!");
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio libri: " + e.getMessage());
            throw e;
        }
    }
    
    public static void salvaPrestiti(List<Prestiti> prestiti) throws IOException {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdir();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_LOAN))) {
            oos.writeObject(prestiti);
            System.out.println("Prestiti salvati con successo!");
        } catch (IOException e) {
            System.err.println("Errore nel salvataggio prestiti: " + e.getMessage());
            throw e;
        }
    }
    
    public static List<Utente> leggiUtenti() throws IOException {
        File file = new File(FILE_USER);
        if (!file.exists()) return new ArrayList<>();
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Utente> list = (List<Utente>) ois.readObject();
            System.out.println("Utenti caricati: " + list.size());
            return list;
        } catch (ClassNotFoundException e) {
            System.err.println("Formato file utenti non valido: " + e.getMessage());
            return new ArrayList<>();
        } catch (EOFException e) {
            // File vuoto
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Errore nella lettura utenti: " + e.getMessage());
            throw e;
        }
    }
    
    public static List<Libro> leggiLibri() throws IOException {
        File file = new File(FILE_BOOK);
        if (!file.exists()) return new ArrayList<>();
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Libro> list = (List<Libro>) ois.readObject();
            System.out.println("Libri caricati: " + list.size());
            return list;
        } catch (ClassNotFoundException e) {
            System.err.println("Formato file libri non valido: " + e.getMessage());
            return new ArrayList<>();
        } catch (EOFException e) {
            // File vuoto
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Errore nella lettura libri: " + e.getMessage());
            throw e;
        }
    }
    
    public static List<Prestiti> leggiPrestiti() throws IOException {
        File file = new File(FILE_LOAN);
        if (!file.exists()) return new ArrayList<>();
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            List<Prestiti> list = (List<Prestiti>) ois.readObject();
            System.out.println("Prestiti caricati: " + list.size());
            return list;
        } catch (ClassNotFoundException e) {
            System.err.println("Formato file prestiti non valido: " + e.getMessage());
            return new ArrayList<>();
        } catch (EOFException e) {
            // File vuoto
            return new ArrayList<>();
        } catch (IOException e) {
            System.err.println("Errore nella lettura prestiti: " + e.getMessage());
            throw e;
        }
    }
}