/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.unisa.diem.bibliotecauniversitaria.Controller;

import java.io.IOException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 *
 * @author Utente
 */
public class HomeController {
    @FXML
    private Button booksButton;
    @FXML
    private Button usersButton;
    @FXML
    private Button loansButton;

    @FXML
    private void openUsers() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/it/unisa/diem/bibliotecauniversitaria/view/UserView.fxml"));
            Stage stage = (Stage) usersButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void openBooks() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/it/unisa/diem/bibliotecauniversitaria/view/BookView.fxml"));
            Stage stage = (Stage) booksButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento.");
            e.printStackTrace();
        }
    }
    
    @FXML
    private void openLoans() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/it/unisa/diem/bibliotecauniversitaria/view/LoanView.fxml"));
            Stage stage = (Stage) loansButton.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            System.err.println("Errore durante il caricamento.");
            e.printStackTrace();
        }
    }

    @FXML
    private void close() {
        Platform.exit();
    }
}
