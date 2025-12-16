package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Prestiti;
import it.unisa.diem.bibliotecauniversitaria.model.Utente;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

public class LoanController {
    @FXML
    private TableView<Prestiti> tableViewPrestiti;
    @FXML 
    private TableColumn<Prestiti, String> idColumn; 
    @FXML 
    private TableColumn<Prestiti, String> isbnColumn;
    @FXML 
    private TableColumn<Prestiti, String> matricolaColumn;
    @FXML 
    private TableColumn<Prestiti, LocalDate> dataPrestitoColumn;
    @FXML 
    private TableColumn<Prestiti, LocalDate> dataScadenzaColumn;
    @FXML 
    private TableColumn<Prestiti, String> ritardoColumn;
    @FXML
    private Button returnLoanButton;
    @FXML
    private Button homeButton;

    private GestionePrestiti gestionePrestiti;
    private ObservableList<Prestiti> observableListaPrestiti;

    @FXML
    public void initialize() {
        this.gestionePrestiti = new GestionePrestiti();
        
        
        try {
            List<Prestiti> prestiti = GestioneFile.leggiPrestiti();
            observableListaPrestiti = FXCollections.observableArrayList(prestiti);
            tableViewPrestiti.setItems(observableListaPrestiti);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Errore nel caricamento dei dati");
        }
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        matricolaColumn.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        
        dataPrestitoColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Prestiti, LocalDate>, ObservableValue<LocalDate>>() {
            @Override
            public ObservableValue<LocalDate> call(TableColumn.CellDataFeatures<Prestiti, LocalDate> param) {
                return new SimpleObjectProperty<>(param.getValue().getDataPrestito());
            }
        });

        dataScadenzaColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Prestiti, LocalDate>, ObservableValue<LocalDate>>() {
            @Override
            public ObservableValue<LocalDate> call(TableColumn.CellDataFeatures<Prestiti, LocalDate> param) {
                return new SimpleObjectProperty<>(param.getValue().getDataRestituzionePrevista());
            }
        });

        ritardoColumn.setCellValueFactory(cellData -> {
            Prestiti prestito = cellData.getValue();
            if (prestito.getDataRestituzioneEffettiva() == null && prestito.getDataRestituzionePrevista() != null) {
                 return new SimpleStringProperty(prestito.isInLate() ? "Sì" : "No");
            }
            return new SimpleStringProperty(""); 
        });

        // Popola la TableView
        observableListaPrestiti.setAll(gestionePrestiti.getPrestiti());
        tableViewPrestiti.setItems(observableListaPrestiti);
        
        // Listener selezione
        tableViewPrestiti.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            boolean isSelected = newSel != null;
            returnLoanButton.setDisable(!isSelected);
        });
    }
   
    @FXML
    private void openInsertLoan(ActionEvent event) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuovo Prestito");
        dialog.setHeaderText("Registrazione Nuovo Prestito");

        dialog.setContentText("Inserisci Dati (Matricola,ISBN,DataScadenza YYYY-MM-DD) separati da virgola:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String[] dati = result.get().split(",");
            
            if (dati.length == 3) {
                try {
                    String matricola = dati[0].trim();
                    String isbn = dati[1].trim();
                    String dataScadenzaStr = dati[2].trim();
                    
                    LocalDate dataFinePrevista = LocalDate.parse(dataScadenzaStr);
                    Prestiti nuovo = new Prestiti(isbn, matricola, LocalDate.now());
                    nuovo.setDataRestituzionePrevista(dataFinePrevista);
   
                    if (gestionePrestiti != null) { 
                        observableListaPrestiti.add(nuovo);
                        observableListaPrestiti.setAll(gestionePrestiti.getPrestiti());
                        tableViewPrestiti.refresh();    
                        showAlert(Alert.AlertType.INFORMATION, "Successo", "Prestito registrato con successo.");
                        try {
                            GestioneFile.salvaPrestiti(new ArrayList<>(observableListaPrestiti));
                        } catch (IOException e) {
                            showAlert(Alert.AlertType.ERROR, "Errore", "Errore nel salvataggio dei dati");
                        }
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile registrare il prestito. Libro già in prestito o dati non validi.");
                    }
                } catch (java.time.format.DateTimeParseException e) {
                    showAlert(Alert.AlertType.ERROR, "Errore", "Formato Data Scadenza non corretto (usa YYYY-MM-DD).");
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Errore", "Formato dati non corretto.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Errore", "Devi inserire esattamente 3 campi separati da virgola.");
            }
        }
    }


    @FXML
    private void openReturnLoan(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Restituzione Libro");
        dialog.setHeaderText("Registrazione Restituzione Prestito");
        dialog.setContentText("Inserisci Dati (Matricola Utente, ISBN Libro) separati da virgola:");


        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String[] dati = result.get().split(",");
            
            
            if (dati.length == 2) {
                try {
                    String matricola = dati[0].trim();
                    String isbn = dati[1].trim();
                    showAlert(Alert.AlertType.INFORMATION, "Restituzione Registrata", "Restituzione del libro " + isbn + " per l'utente " + matricola + " registrata.");
                    observableListaPrestiti.setAll(gestionePrestiti.getPrestiti());
                    tableViewPrestiti.refresh();
                    try {
                        GestioneFile.salvaPrestiti(new ArrayList<>(observableListaPrestiti));
                    } catch (IOException e) {
                        showAlert(Alert.AlertType.ERROR, "Errore", "Errore nel salvataggio dei dati");
                    }
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore durante l'elaborazione.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Errore", "Devi inserire esattamente 2 campi separati da virgola.");
            }
        }
    }


    @FXML
    private void showUserLoans(ActionEvent event) {

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Filtra Prestiti");
        dialog.setHeaderText("Visualizza Prestiti per Utente");
        dialog.setContentText("Inserisci la Matricola dell'utente da ricercare:");

        Optional<String> result = dialog.showAndWait();

        if (result.isPresent()) {
            String matricolaRicerca = result.get().trim();
            
            if (!matricolaRicerca.isEmpty()) {
                try {
                    ObservableList<Prestiti> prestitiFiltrati = FXCollections.observableArrayList();

                    if (prestitiFiltrati.isEmpty()) {
                        showAlert(Alert.AlertType.INFORMATION, "Nessun Risultato", "Nessun prestito attivo trovato per la matricola: " + matricolaRicerca);
                        observableListaPrestiti.setAll(gestionePrestiti.getPrestiti());
                        tableViewPrestiti.refresh();  
                    } else {
                        tableViewPrestiti.setItems(prestitiFiltrati);
                        showAlert(Alert.AlertType.INFORMATION, "Filtro Applicato", "Visualizzazione dei prestiti attivi per l'utente " + matricolaRicerca + ".");
                        observableListaPrestiti.setAll(gestionePrestiti.getPrestiti());
                        tableViewPrestiti.refresh();  
                    }
                    
                } catch (Exception e) {
                    showAlert(Alert.AlertType.ERROR, "Errore", "Si è verificato un errore durante la ricerca.");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Attenzione", "La matricola non può essere vuota.");
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
    
    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}