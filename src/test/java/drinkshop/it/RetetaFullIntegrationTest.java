package drinkshop.it;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.repository.file.FileRetetaRepository;
import drinkshop.service.RetetaService;
import drinkshop.service.validator.RetetaValidator;
import drinkshop.service.validator.ValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

//toate comp reale
class RetetaFullIntegrationTest {

    private RetetaService service;
    private final String fileName = "retete_integrare.txt";

    @BeforeEach
    void setUp() throws IOException {
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileRetetaRepository repository = new FileRetetaRepository(fileName);
        RetetaValidator validator = new RetetaValidator();

        service = new RetetaService(repository, validator);
    }

    @AfterEach
    void tearDown() {
        File file = new File(fileName);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testAddReteta_Success_Integrat() {
        IngredientReteta ing1 = new IngredientReteta("Apa", 100.0);
        IngredientReteta ing2 = new IngredientReteta("Sirop", 50.0);
        Reteta retetaValida = new Reteta(10, Arrays.asList(ing1, ing2));

        service.addReteta(retetaValida);

        Reteta gasita = service.findById(10);

        assertNotNull(gasita);
        assertEquals(10, gasita.getId());
        assertEquals(2, gasita.getIngrediente().size());
        assertEquals("Apa", gasita.getIngrediente().get(0).getDenumire());
    }

    @Test
    void testAddReteta_ValidationFails_Integrat() {
        Reteta retetaInvalida = new Reteta(-5, Arrays.asList(new IngredientReteta("Test", 10.0)));

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            service.addReteta(retetaInvalida);
        });

        assertTrue(ex.getMessage().contains("Product ID invalid"));

        assertNull(service.findById(-5));
    }

    @Test
    void testDeleteReteta_Integrat() {
        Reteta r = new Reteta(1, Arrays.asList(new IngredientReteta("Cafea", 10.0)));
        service.addReteta(r);
        assertNotNull(service.findById(1));

        service.deleteReteta(1);

        assertNull(service.findById(1));
    }
}