package model;

import org.junit.jupiter.api.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class StockTest {

    Stock testStock;
    Client testClient1;
    Client testClient2;
    Product testProduct1;
    Product testProduct2;
    Product testProduct3;

    @BeforeEach
    void runBefore() {
        testStock = new Stock("Test Stock");
        testClient1 = new Client("Test Client 1", new ArrayList<>());
        testClient2 = new Client("Test Client 2", new ArrayList<>());
        testProduct1 = new Product("Test Product 1", 1500, 2, testClient1);
        testProduct2 = new Product("Test Product 2", 2000, 3, testClient2);
        testProduct3 = new Product("Test Product 3", 2500, 4, testClient1);

    }

    @Test
    void testConstructor() {
        assertEquals(new ArrayList<>(), testStock.getProducts());
        assertEquals(new ArrayList<>(), testStock.getClients());
        assertEquals("", testStock.getClientsDataString());
        assertEquals("", testStock.getProductsDataString());
        assertEquals("Test Product:", testStock.getSpecificProduct("Test Product"));
        assertEquals("Test Client:", testStock.getSpecificClient("Test Client"));
    }

    @Test
    void testAddProductsDuplicateClients() {
        List<String> expectedProducts = Arrays.asList("Test Product 1", "Test Product 2");
        List<String> expectedClients = Arrays.asList("Test Client 1", "Test Client 2");

        testStock.addProductToStock("Test Product 1", 1500, 2, "Test Client 1");
        testStock.addProductToStock("Test Product 2", 2000, 3, "Test Client 2");
        assertEquals(expectedProducts, testStock.getAllProductsString());
        assertEquals(expectedClients, testStock.getAllClientsString());

        List<String> newExpectedProducts = Arrays.asList(
                "Test Product 1", "Test Product 2", "Test Product 3");
        testStock.addProductToStock("Test Product 3", 2500, 4, "Test Client 1");
        assertEquals(newExpectedProducts, testStock.getAllProductsString());
        assertEquals(expectedClients, testStock.getAllClientsString());
    }

    @Test
    void testAddProductsDuplicateProducts() {
        testStock.addProductToStock("Test Product 3", 2500, 4, "Test Client 2");
        assertEquals("Test Product 3: $2500 x4, Test Client 2\n",
                testStock.getProductsDataString());

        testStock.addProductToStock("Test Product 1", 1500, 2, "Test Client 1");
        assertEquals("Test Product 3: $2500 x4, Test Client 2\nTest Product 1: $1500 x2, Test Client 1\n",
                testStock.getProductsDataString());

        testStock.addProductToStock("Test Product 1", 1500, 3, "Test Client 1");
        assertEquals("Test Product 3: $2500 x4, Test Client 2\nTest Product 1: $1500 x5, Test Client 1\n",
                testStock.getProductsDataString());
    }

    @Test
    void testRemoveProductFromStockAboveZero() {
        testStock.addProductToStock("Test Product 3", 2500, 4, "Test Client 1");
        testStock.removeProductFromStock("Test Product 3",
                2500, "Test Client 1", 3);
        assertEquals("Test Product 3: $2500 x1, Test Client 1\n",
                testStock.getProductsDataString());

        testStock.removeProductFromStock("Test Product 3",
                2500, "Test Client 1", 1);
        assertEquals(new ArrayList<>(), testStock.getProducts());

        testStock.removeProductFromStock("Test Product 4",
                1, "Test Client 4", 1);
        assertEquals(new ArrayList<>(), testStock.getProducts());
    }

    @Test
    void testRemoveProductFromStockToZero() {
        testStock.addProductToStock("Test Product 3", 2500, 4, "Test Client 1");
        testStock.addProductToStock("Test Product 2", 2000, 3, "Test Client 2");
        assertEquals("Test Client 1: Test Product 3\n" +
                "Test Client 2: Test Product 2\n", testStock.getClientsDataString());

        testStock.removeProductFromStock("Test Product 2",
                2000, "Test Client 2", 3);
        assertEquals("Test Product 3: $2500 x4, Test Client 1\n",
                testStock.getProductsDataString());
        assertEquals("Test Client 1: Test Product 3\n", testStock.getClientsDataString());
    }

    @Test
    void testGetSpecificsOneClientOneProduct() {
        testStock.addProductToStock("Test Product 1", 1500, 2, "Test Client 1");
        testStock.addProductToStock("Test Product 2", 2000, 3, "Test Client 2");
        assertEquals("Test Product 1: Test Client 1",
                testStock.getSpecificProduct("Test Product 1"));
        assertEquals("Test Product 2: Test Client 2",
                testStock.getSpecificProduct("Test Product 2"));
    }

    @Test
    void testGetSpecificClient() {
        testStock.addProductToStock("Test Product 1", 1500, 2, "Test Client 1");
        assertEquals("Test Client 1: Test Product 1", testStock.getSpecificClient("Test Client 1"));
        testStock.addProductToStock("Test Product 3", 2500, 4, "Test Client 1");
        assertEquals("Test Client 1: Test Product 1 Test Product 3",
                testStock.getSpecificClient("Test Client 1"));

        testStock.addProductToStock("Test Product 2", 2000, 3, "Test Client 2");
        assertEquals("Test Client 2: Test Product 2", testStock.getSpecificClient("Test Client 2"));
    }

    @Test
    void testGetSpecificProduct() {
        testStock.addProductToStock("Test Product 1", 1500, 2, "Test Client 1");
        testStock.addProductToStock("Test Product 1", 1500, 2, "Test Client 2");
        assertEquals("Test Product 1: Test Client 1 Test Client 2",
                testStock.getSpecificProduct("Test Product 1"));

        testStock.addProductToStock("Test Product 2", 2000, 3, "Test Client 2");
        assertEquals("Test Product 2: Test Client 2",
                testStock.getSpecificProduct("Test Product 2"));
    }

    @Test
    void testGetTotalStockValue() {
        assertEquals(0, testStock.getTotalStockValue());
        testStock.addProductToStock("Test Product 1", 1500, 2, "Test Client 1");
        testStock.addProductToStock("Test Product 2", 2000, 3, "Test Client 2");
        testStock.addProductToStock("Test Product 3", 2500, 4, "Test Client 1");
        assertEquals(19000, testStock.getTotalStockValue());
    }

    @Test
    void testChangePrices() {
        testStock.addProductToStock("Test Product 1", 1500, 2, "Test Client 1");
        testStock.addProductToStock("Test Product 2", 2000, 3, "Test Client 2");
        testStock.addProductToStock("Test Product 3", 2500, 4, "Test Client 1");
        testStock.changePrices("Test Product 1", 3000);
        assertEquals(22000, testStock.getTotalStockValue());

        testStock.changePrices("Test Product 3", 1000);
        assertEquals(16000, testStock.getTotalStockValue());
    }
}
