package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Libro;
import it.unisa.diem.bibliotecauniversitaria.model.Author;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class GestioneLibri {
    private Map<String,Libro> libri;
    
    public GestioneLibri() {
        this.libri=new HashMap<>();
    }
    
    public boolean inserisciLibro(Libro libro) {
        if(libro.getISBN()==null || libro==null) return false;
        if(libri.containsKey(libro.getISBN())) return false;
        libri.put(libro.getISBN(), libro);
        return true;
    }
    
    public boolean modificaLibro(String isbn, Libro nuoviDati) {
        if(!libri.containsKey(isbn) || isbn==null || nuoviDati==null) return false;
        
        String nuovoISBN=nuoviDati.getISBN();
        if(libri.containsKey(nuovoISBN) || nuovoISBN==null) return false;
        
        Libro libroEsistente=libri.get(isbn);
        libroEsistente.setAnnoPubblicazione(nuoviDati.getAnnoPubblicazione());
        libroEsistente.setAuthor((List<Author>) (Author) nuoviDati.getAuthor());
        libroEsistente.setISBN(nuovoISBN);
        libroEsistente.setNumeroCopieDisponibili(nuoviDati.getnumeroCopieDisponibili());
        libroEsistente.setNumeroCopieTotali(nuoviDati.getnumeroCopieTotali());
        libroEsistente.setTitolo(nuoviDati.getTitolo());
        return true;
    }
    
    public boolean cancellaLibro(String isbn) {
        if(isbn!=null && libri.containsKey(isbn)){
            libri.remove(isbn);
            return true;
        }
        return false;
    }
    
    public List<Libro> cercaPerTitolo(String titolo) {
        if(titolo==null) return null;
        List<Libro> risultati = new ArrayList<>();
        for(Libro libro: libri.values()){
            if(libro.getTitolo().equals(titolo)){
                risultati.add(libro);
            }
        }
        return risultati;
    }
    
    public List<Libro> cercaPerAuthor(String nomeAuthor) {
        if(nomeAuthor==null) return null;
        List<Libro> risultati = new ArrayList<>();
        for(Libro libro:libri.values()){
            for(Author autore:libro.getAuthor()){
                if(autore.equals(nomeAuthor)){
                    risultati.add(libro);
                }
            }
        }
        return risultati;
    }
    
    public List<Libro> cercaPerISBN(String isbn) {
        if(isbn==null){
            return null;
        }
        List<Libro> risultati = new ArrayList<>();
        for(Libro libro:libri.values()){
            if(libro.getISBN().equals(isbn)){
                risultati.add(libro);
            }
        }
        return risultati;
    }
    
    public List<Libro> listaLibriOrdinati() {
        List<Libro> copia=(List<Libro>) libri.values();
        Collections.sort(copia, new Comparator<Libro>(){
            @Override
            public int compare(Libro l1, Libro l2) {
                String titolo1=l1.getTitolo();
                String titolo2=l2.getTitolo();
                if(titolo1==null && titolo2==null) return 0;
                if(titolo1==null) return 1;
                if(titolo2==null) return -1;
                
                return titolo1.compareToIgnoreCase(titolo2);
            }
            
        });
        return copia;
    }
    
    public List<Libro> getLibri() {
        List<Libro> copia = new ArrayList<>(libri.values());
        return copia;
    }
}