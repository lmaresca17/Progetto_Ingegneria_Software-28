package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Author;
import it.unisa.diem.bibliotecauniversitaria.model.Libro;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class BookController {

    // --- Riferimenti FXML (elementi definiti nel file .fxml) ---
    @FXML
    private TableView<Libro> tableViewLibri;
    @FXML
    private TableColumn<Libro, String> colTitolo;
    @FXML
    private TableColumn<Libro, String> colAutori;
    @FXML
    private TableColumn<Libro, String> colAnnoPub;
    @FXML
    private TableColumn<Libro, String> colISBN;
    @FXML
    private TableColumn<Libro, String> colDisponib;
    @FXML
    private TextField txtRicerca;
    @FXML
    private Button btnRicerca;
    @FXML
    private Button btnInserisciLibro;
    @FXML
    private Button btnModificaLibro;
    @FXML
    private Button btnEliminaLibro;
    @FXML
    private Button homeButton;
    
    private GestioneLibri gestioneLibro;
    private ObservableList<Libro> observableListaLibri;

    @FXML
    public void initialize() throws IOException {
        gestioneLibro = new GestioneLibri();
        
        try {
            DataInitializer.initializeIfNeeded();
            
            List<Libro> libri = GestioneFile.leggiLibri();
            observableListaLibri = FXCollections.observableArrayList(libri);
            tableViewLibri.setItems(observableListaLibri);
            
            // Popola gestioneLibri
            for (Libro libro : libri) {
                gestioneLibro.inserisciLibro(libro);
            }
        } catch (IOException e) {
            mostraAlert(Alert.AlertType.ERROR, "Errore", "Errore nel caricamento dei dati");
        }
        
        colTitolo.setCellValueFactory(new PropertyValueFactory<>("titolo"));
        colISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        colAutori.setCellValueFactory(cellData ->
            new javafx.beans.property.SimpleStringProperty(
                cellData.getValue()
                        .getAuthor()
                        .stream()
                        .map(a -> a.getNome() + " " + a.getCognome())
                        .collect(Collectors.joining("; "))
            )
        );
        colAnnoPub.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                String.valueOf(cellData.getValue().getAnnoPubblicazione())
            )
        );

        colDisponib.setCellValueFactory(cellData ->
            new SimpleStringProperty(
                String.valueOf(cellData.getValue().getnumeroCopieDisponibili())
            )
        );

        tableViewLibri.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Libro>() {
            @Override
            public void changed(ObservableValue<? extends Libro> observable, Libro oldValue, Libro newValue) {
                boolean isSelected = newValue != null;
                btnModificaLibro.setDisable(!isSelected);
                btnEliminaLibro.setDisable(!isSelected);
            }
        });
    }

    @FXML
    private void handleRicerca() {
        String testoRicerca = txtRicerca.getText().trim();

        if (testoRicerca.isEmpty()) {
            // Usa observableListaLibri invece di gestioneLibro.getLibri()
            tableViewLibri.setItems(observableListaLibri);
            return;
        }

        List<Libro> risultati = gestioneLibro.cercaPerISBN(testoRicerca);

        if (risultati.isEmpty()) {
            risultati = gestioneLibro.cercaPerTitolo(testoRicerca);
            if (risultati.isEmpty()) {
                risultati = gestioneLibro.cercaPerAuthor(testoRicerca);
            }
        }

        tableViewLibri.setItems(FXCollections.observableArrayList(risultati));

        if (risultati.isEmpty()) {
             mostraAlert(AlertType.INFORMATION, "Ricerca", "Nessun libro trovato con il criterio specificato.");
        }
    }

    @FXML
    private void handleInserisciLibro() {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuovo Libro");
        dialog.setHeaderText("Inserimento Nuovo Libro");
        dialog.setContentText("Inserisci Dati separati da virgola e autori separati da punto e virgola\n (Titolo, Autori, AnnoPubblicazione, ISBN, Disponibilità):");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String[] dati = result.get().split(",");
            if (dati.length == 5) {
                try {
                    String titolo = dati[0].trim();
                    String autori = dati[1].trim();
                    String annopub = dati[2].trim();
                    String isbn = dati[3].trim();
                    String disponib = dati[4].trim();
                    
                    //conversione dati in formati appropriati
                    int anno;
                    int disp;
                    anno = Integer.parseInt(annopub);
                    disp = Integer.parseInt(disponib);
                    
                    String[] parts = dati[1].split(";");
                    List<Author> authors = new ArrayList<>();
                    for (String part : parts) {
                       String[] nc = part.trim().split("\\s+", 2);
                       if (nc.length == 2) {
                           authors.add(new Author(nc[0], nc[1]));
                       }
                    }
                    
                    Libro nuovoLibro = new Libro(titolo, isbn, anno, disp, authors);

                    if (gestioneLibro.inserisciLibro(nuovoLibro)) {
                        mostraAlert(AlertType.INFORMATION, "Successo", "Libro inserito con successo.");
                        observableListaLibri.add(nuovoLibro); // Aggiornamento diretto della lista
                        try {
                            GestioneFile.salvaLibri(new ArrayList<>(observableListaLibri));
                        } catch (IOException e) {
                            mostraAlert(Alert.AlertType.ERROR, "Errore", "Errore nel salvataggio dei dati");
                        }
                    } else {
                        mostraAlert(AlertType.ERROR, "Errore", "Impossibile inserire il libro. Codice ISBN già esistente o dati non validi.");
                    }
                } catch (Exception e) {
                    mostraAlert(AlertType.ERROR, "Errore", "Formato dati non corretto.");
                }
            } else {
                mostraAlert(AlertType.ERROR, "Errore", "Devi inserire esattamente 5 campi separati da virgola.");
            }
        }
    }


   @FXML
    private void handleModificaLibro() {
        Libro libroSelezionato = tableViewLibri.getSelectionModel().getSelectedItem();
        
        if (libroSelezionato != null) {
            final String vecchioISBN = libroSelezionato.getISBN();
            String autore;
            List<Author> authors = new ArrayList<>();
            
            authors = libroSelezionato.getAuthor();
            autore = authors.stream().map(a -> a.getNome() + " " + a.getCognome()).collect(Collectors.joining("; "));
        
            String defaultInput = String.join(",", libroSelezionato.getTitolo(), autore, String.valueOf(libroSelezionato.getAnnoPubblicazione()), vecchioISBN, String.valueOf(libroSelezionato.getnumeroCopieDisponibili()));
            
            TextInputDialog dialog = new TextInputDialog(defaultInput);
            dialog.setTitle("Modifica Libro");
            dialog.setHeaderText("Modifica Dati per " + libroSelezionato.getTitolo() + " " + libroSelezionato.getISBN());
            dialog.setContentText("Inserisci Dati separati da virgola e autori separati da punto e virgola\n (Titolo, Autori, AnnoPubblicazione, ISBN, Disponibilità):");

            Optional<String> result = dialog.showAndWait();
            
            /*if (result.isPresent()) {
                String[] dati = result.get().split(",");
                if (dati.length == 4) {
                    String nuovoTitolo = dati[0].trim();
                    String nuovoAutore = dati[1].trim();
                    String nuovoAnnoPub = dati[2].trim();
                    String nuovoISBN = dati[3].trim();
                    String nuovoDisp = dati[4].trim();

                    //conversione dati in formati appropriati
                    int a;
                    int d;
                    a = Integer.parseInt(nuovoAnnoPub);
                    d = Integer.parseInt(nuovoDisp);

                    String[] parts = dati[1].split(";");
                    List<Author> nuovoA = new ArrayList<>();
                    for (String part : parts) {
                        String nome = part.trim();
                        String cognome = part.trim();
                        nuovoA.add(new Author(nome, cognome));
                    }

                    Libro libroAggiornato = new Libro(nuovoTitolo, nuovoISBN, a, d, nuovoA);
                        
                    gestioneLibro.modificaLibro(libroSelezionato.getISBN(), libroAggiornato);
                    observableListaLibri.setAll(gestioneLibro.getLibri());
                    tableViewLibri.refresh();
                }
            }*/
            
            if (result.isPresent()) {
                String[] dati = result.get().split(",");
                if (dati.length == 5) {
                    try {
                        String nuovoTitolo = dati[0].trim();
                        String nuovoAutore = dati[1].trim();
                        String nuovoAnnoPub = dati[2].trim();
                        String nuovoISBN = dati[3].trim();
                        String nuovoDisp = dati[4].trim();

                        //conversione dati in formati appropriati
                        int a;
                        int d;
                        a = Integer.parseInt(nuovoAnnoPub);
                        d = Integer.parseInt(nuovoDisp);

                        String[] parts = dati[1].split(";");
                        List<Author> nuovoA = new ArrayList<>();
                        for (String part : parts) {
                            String nome = part.trim();
                            String cognome = part.trim();
                            nuovoA.add(new Author(nome, cognome));
                        }

                        Libro libroAggiornato = new Libro(nuovoTitolo, nuovoISBN, a, d, nuovoA);

                        boolean successo = false;
                        
                        if (nuovoISBN.equals(vecchioISBN)) {
                            gestioneLibro.modificaLibro(vecchioISBN, libroAggiornato);
                            libroSelezionato.setTitolo(nuovoTitolo);
                            libroSelezionato.setAnnoPubblicazione(a);
                            libroSelezionato.setNumeroCopieDisponibili(d);
                            libroSelezionato.setAuthor(nuovoA);
                            tableViewLibri.refresh();
                        } else {                          
                            if (gestioneLibro.cercaPerISBN(nuovoISBN) != null) {
                                mostraAlert(AlertType.ERROR, "Errore", "Impossibile modificare. Il nuovo ISBN (" + nuovoISBN + ") esiste già.");
                                successo = false;
                                return;
                            }
                        }
                        
                        if (successo) {
                            mostraAlert(AlertType.INFORMATION, "Successo", "Libro modificato con successo.");
                            observableListaLibri.setAll(gestioneLibro.getLibri());
                            tableViewLibri.refresh();
                            try {
                                GestioneFile.salvaLibri(new ArrayList<>(observableListaLibri));
                            } catch (IOException e) {
                                mostraAlert(Alert.AlertType.ERROR, "Errore", "Errore nel salvataggio dei dati");
                            }
                        } else {
                            mostraAlert(AlertType.ERROR, "Errore", "Modifica fallita. Verifica i dati di input.");
                        }
                    } catch (Exception e) {
                        mostraAlert(AlertType.ERROR, "Errore", "Formato dati non corretto. Dettagli: " + e.getMessage());
                    }
                } else {
                    mostraAlert(AlertType.ERROR, "Errore", "Devi inserire esattamente 5 campi separati da virgola.");
                }
            }
        }
    }

    @FXML
    private void handleEliminaLibro() {
        Libro libroSelezionato = tableViewLibri.getSelectionModel().getSelectedItem();
        
        if (libroSelezionato != null) {
            Alert confirmAlert = new Alert(AlertType.CONFIRMATION);
            confirmAlert.setTitle("Conferma Eliminazione");
            confirmAlert.setHeaderText("Eliminazione Libro");
            confirmAlert.setContentText("Sei sicuro di voler eliminare il libro: " + libroSelezionato.getTitolo() + " " + libroSelezionato.getISBN() + "?");
            
            Optional<ButtonType> result = confirmAlert.showAndWait();
            
            if (result.isPresent() && result.get() == ButtonType.OK) {

                boolean successo = gestioneLibro.cancellaLibro(libroSelezionato.getISBN());
                
                if (successo) {
                    observableListaLibri.remove(libroSelezionato);
                    mostraAlert(AlertType.INFORMATION, "Eliminazione", "Libro eliminato con successo.");
                    try {
                        GestioneFile.salvaLibri(new ArrayList<>(observableListaLibri));
                    } catch (IOException e) {
                        mostraAlert(Alert.AlertType.ERROR, "Errore", "Errore nel salvataggio dei dati");
                    }
                } else {
                    mostraAlert(AlertType.ERROR, "Errore", "Impossibile eliminare il libro dal sistema.");
                }
            }
        }
    }    
 
    @FXML
    private void handleHome() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/it/unisa/diem/bibliotecauniversitaria/view/HomeView.fxml"));
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento.");
        }
    }
    
    private void mostraAlert(AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}