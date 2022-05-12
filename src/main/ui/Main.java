package ui;

import javax.swing.*;
import java.io.FileNotFoundException;

// Main Function
public class Main {
    public static void main(String[] args) {
        try {
            new StockApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
