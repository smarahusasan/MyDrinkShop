package drinkshop.domain;

import drinkshop.repository.file.FileProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductTest {

    Product product;
    ProductService service;
    FileProductRepository repo;
    ProductValidator validator;

    @BeforeEach
    void setUp() {
        product =new Product(100, "Limonada", 10.0, CategorieBautura.JUICE, TipBautura.WATER_BASED);
        repo = new FileProductRepository("mock.csv");
        service = new ProductService(repo);
        validator = new ProductValidator();
    }

    @AfterEach
    void tearDown() {
        product = null;
        repo.findAll().forEach(p -> repo.delete(p.getId()));
        repo = null;
        service = null;
        validator = null;
    }

    @Test
    @Order(1)
    @DisplayName("Product: getId returns constructor ID")
    void getId() {
        assertEquals(100, product.getId(), "The product ID should be 100.");
    }

    @Test
    @Order(2)
    @DisplayName("Product: getNume returns constructor name")
    void getNume() {
        assertEquals("Limonada",product.getNume());
    }

    @Test
    @Order(3)
    @DisplayName("Product: getPret returns constructor price")
    void getPret() {
        assertEquals(10.0,product.getPret());
    }

    @Test
    @Order(4)
    @DisplayName("Product: getCategorie returns constructor category")
    void getCategorie() {
        assertEquals( CategorieBautura.JUICE,product.getCategorie());
    }

    @Test
    @Order(5)
    @DisplayName("Product: setCategorie updates category")
    void setCategorie() {
        product.setCategorie(CategorieBautura.SMOOTHIE);
        assertEquals( CategorieBautura.SMOOTHIE,product.getCategorie());
    }

    @Test
    @Order(6)
    @DisplayName("Product: getTip returns constructor type")
    void getTip() {
        assertEquals( TipBautura.WATER_BASED,product.getTip());
    }

    @Test
    @Order(7)
    @DisplayName("Product: setTip updates type")
    void setTip() {
        product.setTip(TipBautura.BASIC);
        assertEquals( TipBautura.BASIC,product.getTip());
    }

    @Test
    @Order(8)
    @DisplayName("Product: setNume updates name")
    void setNume() {
        product.setNume("newLimonada");
        assertEquals( "newLimonada",product.getNume());
    }

    @Test
    @Order(9)
    @DisplayName("Product: setPret updates price")
    void setPret() {
        product.setPret(10.05);
        assertEquals( 10.05,product.getPret());
    }

    @Test
    @Order(10)
    @DisplayName("Product: toString formats correctly")
    void testToString() {
        assertEquals( "Limonada (JUICE, WATER_BASED) - 10.0 lei",product.toString());
    }

    @Test
    @Order(11)
    @DisplayName("Service: addProduct saves a valid product")
    void addProduct_shouldSave() {

        Product p = new Product(10, "Produs1", 10, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

        service.addProduct(p);

        assertNotNull(service.findById(10));
        assertEquals("Produs1", service.findById(10).getNume());
        assertEquals(1, service.getAllProducts().size());
    }

    @Test
    @Order(12)
    @DisplayName("Service: addProduct rejects empty name")
    void addProduct_InvalidName_shouldThrowException() {

        Product p = new Product(10, "", 10, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

        assertThrows(ValidationException.class, () -> service.addProduct(p));
        assertNull(service.findById(10));
    }

//    @Test
//    void addProduct_InvalidID_shouldThrowException() {
//
//        Product p = new Product("id", "Produs1", 10, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);
//
//        assertThrows(ValidationException.class, () -> service.addProduct(p));
//        assertNull(service.findById(10));
//    }

//    @Test
//    void addProduct_InvalidCategorie_shouldThrowException() {
//
//        Product p = new Product(10, "", 10, TipBautura.BASIC, TipBautura.BASIC);
//
//        assertThrows(ValidationException.class, () -> service.addProduct(p));
//        assertNull(service.findById(10));
//    }

    @Test
    @Order(13)
    @DisplayName("Service: addProduct accepts ID at upper boundary")
    void addProduct_atIDBoundary_shouldSave() {

        Product p = new Product(999, "Nume1", 15, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

        service.addProduct(p);

        assertNotNull(service.findById(999));
        assertEquals("Nume1", service.findById(999).getNume());
        assertEquals(1, service.getAllProducts().size());
    }



    @Order(14)
    @DisplayName("Service: addProduct rejects name at lower boundary")
    @ParameterizedTest
    @CsvSource({"10, DD, 15, CLASSIC_COFFEE, BASIC"})
    void addProduct_atNumeBoundary_shouldThrowException(int id, String nume, double pret, CategorieBautura classic, TipBautura tip) {

        Product p = new Product(10, "DD",  15, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

        assertThrows(ValidationException.class, () -> service.addProduct(p));
        assertNull(service.findById(10));
        assertTrue(service.getAllProducts().isEmpty());
    }

    @Test
    @Order(15)
    @DisplayName("Service: addProduct accepts price at upper boundary")
    void addProduct_atPriceBoundary_shouldSave() {

        Product p = new Product(10, "Nume1", 20, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

        service.addProduct(p);
        assertNotNull(service.findById(10));
        assertEquals("Nume1", service.findById(10).getNume());
        assertEquals(1, service.getAllProducts().size());

    }

    @Test
    @Order(16)
    @DisplayName("Service: addProduct rejects price above upper boundary")
    void addProduct_atPriceBoundary_shouldThrowException() {

        Product p = new Product(10, "Nume1", 21, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

        assertThrows(ValidationException.class, () -> service.addProduct(p));
        assertNull(service.findById(10));
        assertTrue(service.getAllProducts().isEmpty());
    }


}