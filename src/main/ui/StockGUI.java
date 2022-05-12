package ui;

import model.Client;
import model.Event;
import model.EventLog;
import model.Product;
import model.Stock;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.imageio.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class StockGUI extends JPanel {
    private JList productList;
    private JList clientList;
    private DefaultListModel listModel1;
    private DefaultListModel listModel2;

    private static final String JSON_STORE = "./data/stockData.json";
    private Stock userStock;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static final String addString = "Add Product";
    private static final String removeString = "Remove Product";
    private static final String saveString = "Save";
    private static final String loadString = "Load";
    private BufferedImage centerPicture;

    private JButton addButton;
    private JButton removeButton;
    private JButton saveButton;
    private JButton loadButton;

    private JTextField productName;
    private JTextField productPrice;
    private JTextField productAmount;
    private JTextField productClientName;

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    // EFFECTS: Initializes GUI components and sets the layout of the screen
    public StockGUI() {
        super(new BorderLayout());

        userStock = new Stock("User's Stock");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        try {
            centerPicture = ImageIO.read(new File("./data/warehouse_img.jpg"));
        } catch (IOException e) {
            //
        }
        JLabel pictureLabel = new JLabel(new ImageIcon(centerPicture));

        listModel1 = new DefaultListModel<>();
        listModel2 = new DefaultListModel<>();
        productList = new JList<>(listModel1);
        clientList = new JList<>(listModel2);
        JScrollPane productListScrollPane = new JScrollPane(productList);
        JScrollPane clientListScrollPane = new JScrollPane(clientList);
        productListScrollPane.setPreferredSize(new Dimension(650,500));
        clientListScrollPane.setPreferredSize(new Dimension(650,500));
        productList.setFont(new Font("Times New Roman", 1, 25));
        clientList.setFont(new Font("Times New Roman", 1, 25));

        addButton = new JButton(addString);
        AddAndRemoveListener addListener = new AddListener(addButton);
        addButton.setActionCommand(addString);
        addButton.addActionListener(addListener);
        addButton.setEnabled(false);

        removeButton = new JButton(removeString);
        AddAndRemoveListener removeListener = new RemoveListener(removeButton);
        removeButton.setActionCommand(removeString);
        removeButton.addActionListener(removeListener);
        removeButton.setEnabled(false);

        productName = new JTextField(10);
        productName.addActionListener(addListener);
        productName.addActionListener(removeListener);
        productName.getDocument().addDocumentListener(addListener);
        productName.getDocument().addDocumentListener(removeListener);

        productPrice = new JTextField(10);
        productPrice.addActionListener(addListener);
        productPrice.addActionListener(removeListener);
        productPrice.getDocument().addDocumentListener(addListener);
        productPrice.getDocument().addDocumentListener(removeListener);

        productAmount = new JTextField(10);
        productAmount.addActionListener(addListener);
        productAmount.addActionListener(removeListener);
        productAmount.getDocument().addDocumentListener(addListener);
        productAmount.getDocument().addDocumentListener(removeListener);

        productClientName = new JTextField(10);
        productClientName.addActionListener(addListener);
        productClientName.addActionListener(removeListener);
        productClientName.getDocument().addDocumentListener(addListener);
        productClientName.getDocument().addDocumentListener(removeListener);

        saveButton = new JButton(saveString);
        saveButton.setActionCommand(saveString);
        saveButton.addActionListener(new SaveListener());

        loadButton = new JButton(loadString);
        loadButton.setActionCommand(loadString);
        loadButton.addActionListener(new LoadListener());

        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
        buttonPane.add(addButton);
        buttonPane.add(new JLabel("Product Name: "));
        buttonPane.add(productName);
        buttonPane.add(new JLabel("Product Price: "));
        buttonPane.add(productPrice);
        buttonPane.add(new JLabel("Product Amount: "));
        buttonPane.add(productAmount);
        buttonPane.add(new JLabel("Client Name: "));
        buttonPane.add(productClientName);
        buttonPane.add(removeButton);

        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));

        buttonPane.add(saveButton);
        buttonPane.add(loadButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

        add(productListScrollPane, BorderLayout.WEST);
        add(pictureLabel, BorderLayout.CENTER);
        add(clientListScrollPane, BorderLayout.EAST);
        add(buttonPane, BorderLayout.PAGE_END);
    }

    abstract class AddAndRemoveListener implements ActionListener, DocumentListener {
        private boolean alreadyEnabled = false;
        private JButton button;

        public AddAndRemoveListener(JButton button) {
            this.button = button;
        }

        // MODIFIES: this
        // EFFECTS: Adds or removes the product with the given specifications to the stock
        public void actionPerformed(ActionEvent e, String operation) {
            String name = productName.getText();
            int price = Integer.parseInt(productPrice.getText());
            int amount = Integer.parseInt(productAmount.getText());
            String clientName = productClientName.getText();

            if (operation.equals("add")) {
                userStock.addProductToStock(name, price, amount, clientName);
            } else if (operation.equals("remove")) {
                userStock.removeProductFromStock(name, price, clientName, amount);
            }

            updateLists();

            //Reset the text fields.
            productName.requestFocusInWindow();
            productName.setText("");
            productPrice.requestFocusInWindow();
            productPrice.setText("");
            productAmount.requestFocusInWindow();
            productAmount.setText("");
            productClientName.requestFocusInWindow();
            productClientName.setText("");
        }

        // MODIFIES: this
        // EFFECTS: enables the button
        @Override
        public void insertUpdate(DocumentEvent e) {
            enableButton();
        }

        // MODIFIES: this
        // EFFECTS: checks the text field after something has been removed
        @Override
        public void removeUpdate(DocumentEvent e) {
            handleEmptyTextField(e);
        }

        // MODIFIES: this
        // EFFECTS: enables the button if the text field is not empty
        @Override
        public void changedUpdate(DocumentEvent e) {
            if (!handleEmptyTextField(e)) {
                enableButton();
            }
        }

        // MODIFIES: this
        // EFFECTS: enables the button, unless it is already enabled
        private void enableButton() {
            if (!alreadyEnabled) {
                button.setEnabled(true);
            }
        }

        // MODIFIES: this
        // EFFECTS: disables the button if the text field
        //          has length <= 0 (empty)
        private boolean handleEmptyTextField(DocumentEvent e) {
            if (e.getDocument().getLength() <= 0) {
                button.setEnabled(false);
                alreadyEnabled = false;
                return true;
            }
            return false;
        }
    }

    class AddListener extends AddAndRemoveListener implements ActionListener, DocumentListener {
        public AddListener(JButton button) {
            super(button);
        }

        // MODIFIES: this
        // EFFECTS: Adds the product with the given specifications to the stock
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e, "add");
        }
    }

    class RemoveListener extends AddAndRemoveListener implements ActionListener, DocumentListener {
        public RemoveListener(JButton button) {
            super(button);
        }

        // MODIFIES: this
        // EFFECTS: Removes the product with the given specifications from the stock
        public void actionPerformed(ActionEvent e) {
            super.actionPerformed(e, "remove");
        }
    }

    class SaveListener implements ActionListener {
        @Override
        // EFFECTS: saves the stock to file
        public void actionPerformed(ActionEvent e) {
            try {
                jsonWriter.open();
                jsonWriter.write(userStock);
                jsonWriter.close();
            } catch (FileNotFoundException ex) {
                //
            }
        }
    }

    class LoadListener implements ActionListener {
        // EFFECTS: loads the stock from file
        public void actionPerformed(ActionEvent e) {
            try {
                userStock = jsonReader.read();
                updateLists();
            } catch (IOException ex) {
                //
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Updates the JLists to contain the correct objects
    private void updateLists() {
        listModel1.clear();
        listModel2.clear();
        for (Product product : userStock.getProducts()) {
            listModel1.addElement(product.getName() + ": $" + product.getPrice()
                    + " x" + product.getNumber() + ", " + product.getClient().getName());
        }
        for (Client client : userStock.getClients()) {
            listModel2.addElement(client.getName());
        }
    }

    // EFFECTS: Prints the log of events to console
    private static void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(next.toString() + "\n");
        }
    }

    // EFFECTS: Create the GUI and show it
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Manage Your Products");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                printLog(EventLog.getInstance());
                System.exit(0);
            }
        });

        //Create and set up the content pane.
        JComponent newContentPane = new StockGUI();
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
