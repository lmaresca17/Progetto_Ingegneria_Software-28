package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Utente;
import it.unisa.diem.bibliotecauniversitaria.controller.GestioneUtente;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class UserController {

    @FXML
    private TableView<Utente> tableViewUtenti;
    @FXML
    private TableColumn<Utente, String> colMatricola;
    @FXML
    private TableColumn<Utente, String> colCognome;
    @FXML
    private TableColumn<Utente, String> colNome;
    @FXML
    private TableColumn<Utente, String> colEmail;
    @FXML
    private TextField txtRicerca;
    @FXML
    private Button btnRicerca;
    @FXML
    private Button btnInserisciUtente;
    @FXML
    private Button btnModificaUtente;
    @FXML
    private Button btnEliminaUtente;
    @FXML
    private Button homeButton;

    private GestioneUtente gestioneUtente;
    private ObservableList<Utente> observableListaUtenti;

    @FXML
    public void initialize() throws IOException {
        gestioneUtente = new GestioneUtente();
        
        try {
            DataInitializer.initializeIfNeeded();
            
            List<Utente> u = GestioneFile.leggiUtenti();
            observableListaUtenti = FXCollections.observableArrayList(u);
            tableViewUtenti.setItems(observableListaUtenti);
        } catch (IOException e) {
            mostraAlert(Alert.AlertType.ERROR, "Errore", "Errore nel caricamento dei dati");
        }

        // Dati di esempio
        /*
        gestioneUtente.inserisciUtente(new Utente("M001", "Rossi", "Mario", "mario.rossi@unisa.it"));
        gestioneUtente.inserisciUtente(new Utente("M002", "Bianchi", "Anna", "anna.bianchi@unisa.it"));
        gestioneUtente.inserisciUtente(new Utente("M003", "Verdi", "Luigi", "luigi.verdi@unisa.it"));
        */
        
        // Imposta colonne
        colMatricola.setCellValueFactory(new PropertyValueFactory<>("Matricola"));
        colCognome.setCellValueFactory(new PropertyValueFactory<>("Cognome"));
        colNome.setCellValueFactory(new PropertyValueFactory<>("Nome"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));

        // Popola la TableView 
        /*
        observableListaUtenti.setAll(gestioneUtente.getUtenti());
        tableViewUtenti.setItems(observableListaUtenti);
        */
        
        // Listener selezione
        tableViewUtenti.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean isSelected = newSel != null;
            btnModificaUtente.setDisable(!isSelected);
            btnEliminaUtente.setDisable(!isSelected);
        });
    }

    @FXML
    private void handleRicerca() {
        String testo = txtRicerca.getText().trim();
        if (testo.isEmpty()) {
            tableViewUtenti.setItems(observableListaUtenti);
            return;
        }

        List<Utente> risultati = gestioneUtente.cercaPerCognome(testo);
        if (risultati.isEmpty()) {
            risultati = gestioneUtente.cercaPerMatricola(testo);
        }

        tableViewUtenti.setItems(FXCollections.observableArrayList(risultati));

        if (risultati.isEmpty()) {
            mostraAlert(AlertType.INFORMATION, "Ricerca", "Nessun utente trovato.");
        }
    }

    @FXML
    private void handleInserisciUtente() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuovo Utente");
        dialog.setHeaderText("Inserimento Nuovo Utente");
        dialog.setContentText("Inserisci dati (Matricola, Cognome, Nome, Email):");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            String[] dati = result.get().split(",");
            if (dati.length == 4) {
                try {
                    Utente u = new Utente(dati[0].trim(), dati[1].trim(), dati[2].trim(), dati[3].trim());
                    if (gestioneUtente.inserisciUtente(u)) {
                        observableListaUtenti.add(u);
                        mostraAlert(AlertType.INFORMATION, "Successo", "Utente inserito.");
                        try {
                            GestioneFile.salvaUtenti(new ArrayList<>(observableListaUtenti));
                        } catch (IOException e) {
                            mostraAlert(Alert.AlertType.ERROR, "Errore", "Errore nel salvataggio dei dati");
                        }
                    } else {
                        mostraAlert(AlertType.ERROR, "Errore", "Matricola già esistente.");
                    }
                }catch (Exception e) {
                    mostraAlert(AlertType.ERROR, "Errore", "Formato dati non corretto.");
                }
            } else {
                mostraAlert(AlertType.ERROR, "Errore", "Devi inserire esattamente 4 campi separati da virgola.");
            }
        }
    }

    @FXML
    private void handleModificaUtente() {
        Utente selezionato = tableViewUtenti.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            TextInputDialog dialog = new TextInputDialog(
                    String.join(",", selezionato.getMatricola(), selezionato.getCognome(),
                            selezionato.getNome(), selezionato.getEmail()));
            dialog.setTitle("Modifica Utente");
            dialog.setHeaderText("Aggiorna dati utente");

            Optional<String> result = dialog.showAndWait();
            if (result.isPresent()) {
                String[] dati = result.get().split(",");
                if (dati.length == 4) {
                    Utente aggiornato = new Utente(dati[0].trim(), dati[1].trim(), dati[2].trim(), dati[3].trim());
                    gestioneUtente.modificaUtente(selezionato.getMatricola(), aggiornato);
                    observableListaUtenti.setAll(gestioneUtente.getUtenti());
                    tableViewUtenti.refresh();
                    try {
                        GestioneFile.salvaUtenti(new ArrayList<>(observableListaUtenti));
                    } catch (IOException e) {
                        mostraAlert(Alert.AlertType.ERROR, "Errore", "Errore nel salvataggio dei dati");
                    }
                }
            }
        }
    }

    @FXML
    private void handleEliminaUtente() {
        Utente selezionato = tableViewUtenti.getSelectionModel().getSelectedItem();
        if (selezionato != null) {
            Alert confirm = new Alert(AlertType.CONFIRMATION);
            confirm.setTitle("Elimina Utente");
            confirm.setHeaderText(null);
            confirm.setContentText("Sei sicuro?");
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                gestioneUtente.cancellaUtente(selezionato.getMatricola());
                observableListaUtenti.setAll(gestioneUtente.getUtenti());
                tableViewUtenti.refresh();
                try {
                    GestioneFile.salvaUtenti(new ArrayList<>(observableListaUtenti));
                } catch (IOException e) {
                    mostraAlert(Alert.AlertType.ERROR, "Errore", "Errore nel salvataggio dei dati");
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

    private void mostraAlert(AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
