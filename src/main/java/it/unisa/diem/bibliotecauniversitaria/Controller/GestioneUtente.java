package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Utente;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.fxml.FXML;

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
       if(!utenti.containsKey(matricola) || matricola==null || utenteAggiornato==null) return false;
       String nuovaMatricola=utenteAggiornato.getMatricola();
       if(utenti.containsKey(nuovaMatricola) || nuovaMatricola==null) return false;
       Utente utenteRegistrato=utenti.get(matricola);
       utenteRegistrato.setMatricola(nuovaMatricola);
       utenteRegistrato.setEmail(utenteAggiornato.getEmail());
       utenteRegistrato.setCognome(utenteAggiornato.getCognome());
       utenteRegistrato.setNome(utenteAggiornato.getNome());
       
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
            if(utente.getCognome().equals(cognome))
                risultati.add(utente);
        }
        return risultati;
    }
    public Utente cercaPerMatricola(String matricola) {
        if(matricola==null) return null;
        return utenti.get(matricola);
    }
    public List<Utente> listaUtentiOrdinata() {
        List<Utente> copia= (List<Utente>) utenti.values();
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
        List<Utente> copia= (List<Utente>) utenti.values();
        return copia;
    }
    
    /*
    @FXML
    private void switchToSecondary() throws IOException {
        Main.setRoot("secondary");
    }
    */
}
