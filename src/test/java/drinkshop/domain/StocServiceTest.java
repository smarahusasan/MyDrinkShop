package drinkshop.domain;

import drinkshop.repository.Repository;
import drinkshop.repository.file.FileStocRepository;
import drinkshop.service.StocService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StocServiceTest {

    StocService service;
    FileStocRepository repo;


    @BeforeEach
    void setUp() {
        repo = new FileStocRepository("mock.csv");
        service = new StocService(repo);
    }

    @AfterEach
    void setUpAfter() {
        List<Stoc> stocks = repo.findAll();
        stocks.forEach(s -> repo.delete(s.getId()));
        repo = null;
        service = null;
    }


    @Test
    void F02_TC01_insufficientStock_lapte100_reteta300() {

        Stoc s1 = new Stoc(1, "lapte", 100, 1);
        repo.save(s1);

        List<IngredientReteta> ingrediente = new ArrayList<>();
        ingrediente.add(new IngredientReteta("lapte", 300));
        Reteta r = new Reteta(1, ingrediente);

        assertThrows(IllegalStateException.class, () -> service.consuma(r));
        assertEquals(100, s1.getCantitate(), 0.001);
    }

    @Test
    void F02_TC02_emptyRecipe_stockUnchanged() {

        Stoc s1 = new Stoc(1, "lapte", 100, 1);
        repo.save(s1);

        Reteta r = new Reteta(2, new ArrayList<>());

        assertDoesNotThrow(() -> service.consuma(r));
        assertEquals(100, s1.getCantitate(), 0.001);
    }

    @Test
    void F02_TC03_zeroStock_zahar50_exception() {

        Stoc s1 = new Stoc(1, "zahar", 0, 1);
        repo.save(s1);

        List<IngredientReteta> ingrediente = new ArrayList<>();
        ingrediente.add(new IngredientReteta("zahar", 50));
        Reteta r = new Reteta(3, ingrediente);

        assertThrows(IllegalStateException.class, () -> service.consuma(r));
        assertEquals(0, s1.getCantitate(), 0.001);
    }

    @Test
    void F02_TC04_twoStockEntries_sameIngredient() {

        Stoc lapte1 = new Stoc(1, "lapte", 100, 1);
        Stoc lapte2 = new Stoc(2, "lapte", 50, 1);
        repo.save(lapte1);
        repo.save(lapte2);

        List<IngredientReteta> ingrediente = new ArrayList<>();
        ingrediente.add(new IngredientReteta("lapte", 100));
        Reteta r = new Reteta(4, ingrediente);

        assertDoesNotThrow(() -> service.consuma(r));
        assertEquals(0, lapte1.getCantitate(), 0.001);
        assertEquals(50, lapte2.getCantitate(), 0.001);
    }

    @Test
    void F02_TC05_singleStockEntry_normalConsume() {

        Stoc s1 = new Stoc(1, "lapte", 150, 1);
        repo.save(s1);

        List<IngredientReteta> ingrediente = new ArrayList<>();
        ingrediente.add(new IngredientReteta("lapte", 100));
        Reteta r = new Reteta(5, ingrediente);

        assertDoesNotThrow(() -> service.consuma(r));
        assertEquals(50, s1.getCantitate(), 0.001);
    }
}
