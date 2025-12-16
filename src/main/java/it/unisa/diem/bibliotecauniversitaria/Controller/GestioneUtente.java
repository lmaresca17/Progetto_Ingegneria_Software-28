package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Utente;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestioneUtente {
    private Map<String,Utente> utenti;
    
    public GestioneUtente() {
        utenti=new HashMap<>();
    }
    public boolean inserisciUtente(Utente utente) {
        if(utente==null || utente.getMatricola()==null || utenti.containsKey(utente.getMatricola())) return false;
        utenti.put(utente.getMatricola(), utente);
        return true;
    }
    
    public boolean modificaUtente(String matricola, Utente utenteAggiornato) {
        if (matricola == null || utenteAggiornato == null || !utenti.containsKey(matricola)) return false;

        String nuovaMatricola = utenteAggiornato.getMatricola();
        if (nuovaMatricola == null) return false;

        // Se cambia matricola, verifica unicità
        if (!nuovaMatricola.equals(matricola) && utenti.containsKey(nuovaMatricola)) return false;

        Utente utente = utenti.get(matricola);
        utente.setNome(utenteAggiornato.getNome());
        utente.setCognome(utenteAggiornato.getCognome());
        utente.setEmail(utenteAggiornato.getEmail());

        if (!nuovaMatricola.equals(matricola)) {
            utenti.remove(matricola);
            utente.setMatricola(nuovaMatricola);
            utenti.put(nuovaMatricola, utente);
        }

        return true;
    }

    public boolean cancellaUtente(String matricola) {
        if(matricola!=null && utenti.containsKey(matricola)){
            utenti.remove(matricola);
            return true;
        }
        return false;
    }
    
    public List<Utente> cercaPerCognome(String cognome) {
        if(cognome==null) return null;
        List<Utente> risultati= new ArrayList<>();
        for(Utente utente:utenti.values()){
            if(utente.getCognome().equalsIgnoreCase(cognome))
                risultati.add(utente);
        }
        return risultati;
    }
    
    public List<Utente> cercaPerMatricola(String matricola) {
        if(matricola==null) return null;
        List<Utente> risultati= new ArrayList<>();
        for(Utente utente:utenti.values()){
            if(utente.getMatricola().equalsIgnoreCase(matricola))
                risultati.add(utente);
        }
        return risultati;
    }
    
    public List<Utente> listaUtentiOrdinata() {
        List<Utente> copia = new ArrayList<>(utenti.values());
        Collections.sort(copia, new Comparator<Utente>(){
            @Override
            public int compare(Utente u1, Utente u2) {
                String cognome1=u1.getCognome();
                String cognome2=u2.getCognome();
                if(cognome1==null && cognome2==null) return 0;
                if(cognome1==null) return 1;
                if(cognome2==null) return -1;
                
                return cognome1.compareToIgnoreCase(cognome2);
            }
            
        });
        return copia;
    }
    
    public List<Utente> getUtenti() {
        List<Utente> copia = new ArrayList<>(utenti.values());
        return copia;
    }
}