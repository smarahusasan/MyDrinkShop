package drinkshop.domain;

import drinkshop.repository.file.FileProductRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import drinkshop.service.ProductService;
import drinkshop.service.validator.ProductValidator;
import drinkshop.service.validator.ValidationException;

import static org.junit.jupiter.api.Assertions.*;

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
    void getId() {
        assertEquals(100, product.getId(), "The product ID should be 100.");
    }

    @Test
    void getNume() {
        assertEquals("Limonada",product.getNume());
    }

    @Test
    void getPret() {
        assertEquals(10.0,product.getPret());
    }

    @Test
    void getCategorie() {
        assertEquals( CategorieBautura.JUICE,product.getCategorie());
    }

    @Test
    void setCategorie() {
        product.setCategorie(CategorieBautura.SMOOTHIE);
        assertEquals( CategorieBautura.SMOOTHIE,product.getCategorie());
    }

    @Test
    void getTip() {
        assertEquals( TipBautura.WATER_BASED,product.getTip());
    }

    @Test
    void setTip() {
        product.setTip(TipBautura.BASIC);
        assertEquals( TipBautura.BASIC,product.getTip());
    }

    @Test
    void setNume() {
        product.setNume("newLimonada");
        assertEquals( "newLimonada",product.getNume());
    }

    @Test
    void setPret() {
        product.setPret(10.05);
        assertEquals( 10.05,product.getPret());
    }

    @Test
    void testToString() {
        assertEquals( "Limonada (JUICE, WATER_BASED) - 10.0 lei",product.toString());
    }

    @Test
    void addProduct_shouldSave() {

        Product p = new Product(10, "Produs1", 10, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

        service.addProduct(p);

        assertNotNull(service.findById(10));
        assertEquals("Produs1", service.findById(10).getNume());
        assertEquals(1, service.getAllProducts().size());
    }

    @Test
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
//        service.addProduct(p);
//        assertThrows(ValidationException.class, () -> service.addProduct(p));
//        assertNull(service.findById(10));
//    }

//    @Test
//    void addProduct_InvalidCategorie_shouldThrowException() {
//
//        Product p = new Product(10, "", 10, TipBautura.BASIC, TipBautura.BASIC);
//
//        service.addProduct(p);
//        assertThrows(ValidationException.class, () -> service.addProduct(p));
//        assertNull(service.findById(10));
//    }

    @Test
    void addProduct_atIDBoundary_shouldSave() {

        Product p = new Product(999, "Nume1", 15, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

        service.addProduct(p);

        assertNotNull(service.findById(999));
        assertEquals("Nume1", service.findById(999).getNume());
        assertEquals(1, service.getAllProducts().size());
    }



    @Test
    void addProduct_atNumeBoundary_shouldThrowException() {

        Product p = new Product(10, "DD", 15, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

        assertThrows(ValidationException.class, () -> service.addProduct(p));
        assertNull(service.findById(10));
        assertTrue(service.getAllProducts().isEmpty());
    }

    @Test
    void addProduct_atPriceBoundary_shouldSave() {

        Product p = new Product(10, "Nume1", 20, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

        service.addProduct(p);
        assertNotNull(service.findById(10));
        assertEquals("Nume1", service.findById(10).getNume());
        assertEquals(1, service.getAllProducts().size());

    }

    @Test
    void addProduct_atPriceBoundary_shouldThrowException() {

        Product p = new Product(10, "Nume1", 21, CategorieBautura.CLASSIC_COFFEE, TipBautura.BASIC);

        assertThrows(ValidationException.class, () -> service.addProduct(p));
        assertNull(service.findById(10));
        assertTrue(service.getAllProducts().isEmpty());
    }


}