package it.unisa.diem.bibliotecauniversitaria.controller;

import java.io.*;
import java.nio.file.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BackupManager {
    
    public static void createBackup() throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File backupDir = new File("backup/" + timestamp);
        backupDir.mkdirs();
        
        // Copia i file di dati
        Files.copy(Paths.get("data/books.dat"), 
                  Paths.get(backupDir.getPath() + "/books.dat"),
                  StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("data/users.dat"), 
                  Paths.get(backupDir.getPath() + "/users.dat"),
                  StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get("data/loans.dat"), 
                  Paths.get(backupDir.getPath() + "/loans.dat"),
                  StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("Backup creato in: " + backupDir.getPath());
    }
    
    public static void restoreBackup(String backupPath) throws IOException {
        File backupDir = new File(backupPath);
        if (!backupDir.exists()) {
            throw new FileNotFoundException("Directory di backup non trovata: " + backupPath);
        }
        
        // Copia i file di backup nella directory data
        Files.copy(Paths.get(backupPath + "/books.dat"), 
                  Paths.get("data/books.dat"),
                  StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get(backupPath + "/users.dat"), 
                  Paths.get("data/users.dat"),
                  StandardCopyOption.REPLACE_EXISTING);
        Files.copy(Paths.get(backupPath + "/loans.dat"), 
                  Paths.get("data/loans.dat"),
                  StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println("Backup ripristinato da: " + backupPath);
    }
}
