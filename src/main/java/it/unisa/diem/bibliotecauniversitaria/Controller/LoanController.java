package it.unisa.diem.bibliotecauniversitaria.controller;

import it.unisa.diem.bibliotecauniversitaria.model.Prestiti;
import it.unisa.diem.bibliotecauniversitaria.model.Libro;
import it.unisa.diem.bibliotecauniversitaria.model.Utente;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
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

public class LoanController {

    @FXML private TableView<Prestiti> tableViewPrestiti;
    @FXML private TableColumn<Prestiti, Integer> idColumn;
    @FXML private TableColumn<Prestiti, String> isbnColumn;
    @FXML private TableColumn<Prestiti, String> matricolaColumn;
    @FXML private TableColumn<Prestiti, LocalDate> dataPrestitoColumn;
    @FXML private TableColumn<Prestiti, LocalDate> dataScadenzaColumn;
    @FXML private TableColumn<Prestiti, String> ritardoColumn;
    @FXML private Button returnLoanButton;
    @FXML private Button homeButton;

    private GestionePrestiti gestionePrestiti;
    private ObservableList<Prestiti> observableListaPrestiti;

    @FXML
    public void initialize() throws IOException {
        try {
            DataInitializer.initializeIfNeeded();
                    
            List<Prestiti> prestiti = GestioneFile.leggiPrestiti();
            gestionePrestiti = new GestionePrestiti(prestiti);
            observableListaPrestiti = FXCollections.observableArrayList(prestiti);
            gestionePrestiti = new GestionePrestiti(prestiti);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Errore nel caricamento dei dati");
        }

        tableViewPrestiti.setItems(observableListaPrestiti);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        matricolaColumn.setCellValueFactory(new PropertyValueFactory<>("matricola"));
        dataPrestitoColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getDataPrestito()));
        dataScadenzaColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getDataRestituzionePrevista()));

        ritardoColumn.setCellValueFactory(c -> {
            Prestiti p = c.getValue();
            if (p.getDataRestituzioneEffettiva() == null && p.getDataRestituzionePrevista() != null) {
                return new SimpleStringProperty(p.isInLate() ? "Sì" : "No");
            }
            return new SimpleStringProperty("");
        });

        returnLoanButton.setDisable(true);
        tableViewPrestiti.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldSel, newSel) -> returnLoanButton.setDisable(newSel == null)
        );
    }

    @FXML
    private void openInsertLoan(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nuovo Prestito");
        dialog.setHeaderText("Matricola, ISBN, Data scadenza (YYYY-MM-DD)");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) return;

        try {
            String[] dati = result.get().split(",");
            if (dati.length != 3) throw new IllegalArgumentException();

            String matricola = dati[0].trim();
            String isbn = dati[1].trim();
            LocalDate scadenza = LocalDate.parse(dati[2].trim());

            Utente utente = cercaUtente(matricola);
            Libro libro = cercaLibro(isbn);

            if (utente == null || libro == null) {
                showAlert(Alert.AlertType.ERROR, "Errore", "Utente o libro non trovato");
                return;
            }

            Prestiti nuovo = gestionePrestiti.registraPrestito(utente, libro, scadenza);
            if (nuovo == null) {
                showAlert(Alert.AlertType.ERROR, "Errore", "Libro già in prestito");
                return;
            }

            observableListaPrestiti.setAll(gestionePrestiti.getPrestiti());
            try {
                GestioneFile.salvaPrestiti(new ArrayList<>(observableListaPrestiti));
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Errore", "Errore nel salvataggio dei dati");
            }
            tableViewPrestiti.refresh();

            showAlert(Alert.AlertType.INFORMATION, "Successo", "Prestito registrato");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Dati non validi");
        }
    }

    @FXML
    private void openReturnLoan(ActionEvent event) {
        Prestiti p = tableViewPrestiti.getSelectionModel().getSelectedItem();
        if (p == null) return;

        try {
            Libro libro = cercaLibro(p.getISBN());

            gestionePrestiti.restituisciLibro(p.getId(), libro);
            observableListaPrestiti.remove(p);
            // observableListaPrestiti.setAll(gestionePrestiti.getPrestiti());
            GestioneFile.salvaPrestiti(new ArrayList<>(observableListaPrestiti));
            tableViewPrestiti.refresh();

            showAlert(Alert.AlertType.INFORMATION, "Restituzione", "Libro restituito");
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Errore restituzione");
        }
    }

    @FXML
    private void showUserLoans(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Prestiti Utente");
        dialog.setHeaderText("Inserisci matricola");

        Optional<String> result = dialog.showAndWait();
        if (!result.isPresent()) return;

        String matricola = result.get().trim();
        ObservableList<Prestiti> filtrati = FXCollections.observableArrayList();

        for (Prestiti p : gestionePrestiti.getPrestiti()) {
            if (p.getMatricola().equals(matricola) && p.getDataRestituzioneEffettiva() == null) {
                filtrati.add(p);
            }
        }

        tableViewPrestiti.setItems(filtrati);
    }

    @FXML
    private void handleHome() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(
                    "/it/unisa/diem/bibliotecauniversitaria/view/HomeView.fxml"));
            Stage stage = (Stage) homeButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Errore", "Impossibile tornare alla Home");
        }
    }

    private Utente cercaUtente(String matricola) throws IOException {
        for (Utente u : GestioneFile.leggiUtenti()) {
            if (u.getMatricola().equals(matricola)) return u;
        }
        return null;
    }

    private Libro cercaLibro(String isbn) throws IOException {
        for (Libro l : GestioneFile.leggiLibri()) {
            if (l.getISBN().equals(isbn)) return l;
        }
        return null;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}