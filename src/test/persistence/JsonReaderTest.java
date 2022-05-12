package persistence;

import model.*;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

// Inspired by JSON Serialization Demo
public class JsonReaderTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Stock stock = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyStock() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyStock.json");
        try {
            Stock stock = reader.read();
            assertEquals("My Stock", stock.getName());
            assertTrue(stock.getProducts().isEmpty());
            assertTrue(stock.getClients().isEmpty());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralStock() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralStock.json");
        try {
            Stock stock = reader.read();
            assertEquals("My Stock", stock.getName());
            List<Product> products = stock.getProducts();
            List<Client> clients = stock.getClients();
            assertEquals(2, products.size());
            assertEquals(2, clients.size());
            assertEquals("Camera: $200 x2, Jack\nLaptop: $1500 x1, Bob\n",
                    stock.getProductsDataString());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}
