package ui;

import model.Stock;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

// Product Stock App
// Inspired by tellerApp program and JSON Serialization Demo
public class StockApp {
    private static final String JSON_STORE = "./data/stockData.json";
    private Scanner userInput;
    private Stock userStock;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: Runs the application
    public StockApp() throws FileNotFoundException {
        userInput = new Scanner(System.in);
        userStock = new Stock("User's Stock");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runStockApp();
    }

    // MODIFIES: this
    // EFFECTS: Takes user input and executes depending on the input
    private void runStockApp() {
        boolean wantsToContinue = true;
        String answer;

        while (wantsToContinue) {
            showInterface();
            System.out.println("\nHow would you like to alter your stock?");
            answer = userInput.nextLine().toLowerCase();

            if (answer.equals("exit")) {
                System.out.println("Would you like to save the stock? (y/n)");
                String wantsToSave = userInput.nextLine();
                if (wantsToSave.equals("y")) {
                    saveStock();
                }
                wantsToContinue = false;
            } else {
                runAnswer(answer);
            }
        }
    }

    //MODIFIES: this
    //EFFECTS: Runs the application based on user input
    private void runAnswer(String answer) {
        switch (answer) {
            case "add": performAdd();
                break;
            case "remove": performRemove();
                break;
            case "vc": performViewClient();
                break;
            case "vp": performViewProduct();
                break;
            case "price": performChangePrice();
                break;
            case "value": performTotalValue();
                break;
            case "save": saveStock();
                break;
            case "load": loadStock();
                break;
        }
    }

    //EFFECTS: Displays the user interface in the console
    private void showInterface() {
        if (userStock.getProducts().isEmpty()) {
            System.out.println("\n\tadd -> Add Product");
            System.out.println("\tsave -> Save Stock");
            System.out.println("\tload -> Load Stock");
            System.out.println("\texit -> Exit");
        } else {
            System.out.println("\n\tadd -> Add Product");
            System.out.println("\tremove -> Remove Product");
            System.out.println("\tvc -> View Clients");
            System.out.println("\tvp -> View Products");
            System.out.println("\tprice -> Change Price");
            System.out.println("\tvalue -> Total Stock Value");
            System.out.println("\tsave -> Save Stock");
            System.out.println("\tload -> Load Stock");
            System.out.println("\texit -> Exit");
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds a product to the stock
    private void performAdd() {
        System.out.println("What product would you like to add?");
        String productName = userInput.nextLine();
        System.out.println("How much does this product cost?");
        int productPrice = Integer.parseInt(userInput.nextLine());
        System.out.println("How many units of this product would you like to add?");
        int amount = Integer.parseInt(userInput.nextLine());
        System.out.println("Who is buying this product?");
        String clientName = userInput.nextLine();
        userStock.addProductToStock(productName, productPrice, amount, clientName);
        System.out.println(amount + "x " + productName + " has been added.");
    }

    // MODIFIES: this
    // EFFECTS: Removes a product from the stock
    private void performRemove() {
        System.out.println("What product was bought?");
        String productName = userInput.nextLine();
        System.out.println("What is the cost of this product?");
        int productPrice = Integer.parseInt(userInput.nextLine());
        System.out.println("Who bought this product?");
        String clientName = userInput.nextLine();
        System.out.println("How many units of this product were sold?");
        int amount = Integer.parseInt(userInput.nextLine());
        userStock.removeProductFromStock(productName, productPrice, clientName, amount);
        System.out.println(amount + "x " + productName + " has been removed.");
    }

    // EFFECTS: Views the data of clients
    private void performViewClient() {
        System.out.println("Would you like to see all customers, or one particular customer?");
        System.out.println("\n\tall -> all");
        System.out.println("\tone -> one");
        String answer = userInput.nextLine();
        if (answer.equals("one")) {
            System.out.println("Which customer would you like to see?");
            String clientName = userInput.nextLine();
            System.out.println(userStock.getSpecificClient(clientName));
        } else if (answer.equals("all")) {
            System.out.println(userStock.getClientsDataString());
        }
    }

    // EFFECTS: Views the data of products
    private void performViewProduct() {
        System.out.println("Would you like to see all products, or one particular product?");
        System.out.println("\n\tall -> all");
        System.out.println("\tone -> one");
        String answer = userInput.nextLine();
        if (answer.equals("one")) {
            System.out.println("Which product would you like to see?");
            String productName = userInput.nextLine();
            System.out.println(userStock.getSpecificProduct(productName));
        } else if (answer.equals("all")) {
            System.out.println(userStock.getProductsDataString());
        }
    }

    // MODIFIES: this
    // EFFECTS: Changes the price of a product in the stock
    private void performChangePrice() {
        System.out.println("Which product would you like to change the price of?");
        String productName = userInput.nextLine();
        System.out.println("What would you like to change the price of this product to?");
        int newPrice = Integer.parseInt(userInput.nextLine());
        userStock.changePrices(productName, newPrice);
        System.out.println(productName + " has been set to $" + newPrice);
    }

    // EFFECTS: Displays the total value of the stock
    private void performTotalValue() {
        System.out.println("$" + userStock.getTotalStockValue());
    }

    // EFFECTS: saves the stock to file
    private void saveStock() {
        try {
            jsonWriter.open();
            jsonWriter.write(userStock);
            jsonWriter.close();
            System.out.println("Saved " + userStock.getName() + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads workroom from file
    private void loadStock() {
        try {
            userStock = jsonReader.read();
            System.out.println("Loaded " + userStock.getName() + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }
}
