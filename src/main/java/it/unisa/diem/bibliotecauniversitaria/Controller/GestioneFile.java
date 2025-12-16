package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Libro;
import it.unisa.diem.bibliotecauniversitaria.model.Prestiti;
import it.unisa.diem.bibliotecauniversitaria.model.Utente;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class GestioneFile {
    private static final String FILE_BOOK = "data/books.dat";
    private static final String FILE_USER = "data/users.dat";
    private static final String FILE_LOAN = "data/loans.dat";
    
    public static void salvaUtenti(List<Utente> utenti) throws FileNotFoundException, IOException {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdir();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_USER))) {
            oos.writeObject(utenti);
            System.out.println("Oggetto salvato con successo!");
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void salvaLibri(List<Libro> libri) throws FileNotFoundException, IOException {
        File dir = new File("data");
        if (!dir.exists()) dir.mkdir();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_BOOK))) {
            oos.writeObject(libri);
            System.out.println("Oggetto salvato con successo!");
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static void salvaPrestiti(List<Prestiti> prestiti) throws FileNotFoundException, IOException{
        File dir = new File("data");
        if (!dir.exists()) dir.mkdir();
        
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_LOAN))) {
            oos.writeObject(prestiti);
            System.out.println("Oggetto salvato con successo!");
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
    
    public static List<Utente> leggiUtenti() throws FileNotFoundException, IOException {
        File file = new File(FILE_USER);
        if (!file.exists()) return new ArrayList<>();
        
        List<Utente> list = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                list = (List<Utente>) ois.readObject();
                return list;
        } catch (ClassNotFoundException e) {
                System.err.println(e.getMessage());
        }
        
        return list;
    }
    
    public static List<Libro> leggiLibri() throws FileNotFoundException, IOException {
        File file = new File(FILE_BOOK);
        if (!file.exists()) return new ArrayList<>();
        
        List<Libro> list = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                list = (List<Libro>) ois.readObject();
                return list;
        } catch (ClassNotFoundException e) {
                System.err.println(e.getMessage());
        }
        
        return list;
    }
    
    public static List<Prestiti> leggiPrestiti() throws FileNotFoundException, IOException {
        File file = new File(FILE_LOAN);
        if (!file.exists()) return new ArrayList<>();
        
        List<Prestiti> list = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            list = (List<Prestiti>) ois.readObject();
            return list;
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        
        return list;
    }
}