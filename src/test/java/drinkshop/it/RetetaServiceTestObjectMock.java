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
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RetetaServiceTestObjectMock {

    private RetetaService retetaService;
    private final String testFileName = "test_retete.txt";

    @BeforeEach
    void setUp() throws IOException {
        File file = new File(testFileName);

        if (!file.exists()) {
            file.createNewFile();
        }

        FileRetetaRepository repoReal = new FileRetetaRepository(testFileName);
        RetetaValidator validatorReal = new RetetaValidator();

        retetaService = new RetetaService(repoReal, validatorReal);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(new File(testFileName).toPath());
    }

    @Test
    void testAddReteta_FullFlow_Success() {
        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Cafea", 15.0),
                new IngredientReteta("Lapte", 100.0)
        );
        Reteta retetaNoua = new Reteta(1, ingrediente);

        retetaService.addReteta(retetaNoua);

        Reteta salvata = retetaService.findById(1);

        assertNotNull(salvata, "Reteta ar trebui să fie salvată în fișier");
        assertEquals(1, salvata.getId());
        assertEquals(2, salvata.getIngrediente().size());
        assertEquals("Cafea", salvata.getIngrediente().get(0).getDenumire());
    }

    @Test
    void testAddReteta_FullFlow_ValidationFails_NoPersistence() {
        List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Zahar", -5.0)
        );
        Reteta retetaInvalida = new Reteta(2, ingrediente);

        assertThrows(ValidationException.class, () -> {
            retetaService.addReteta(retetaInvalida);
        });

        assertNull(retetaService.findById(2), "Reteta invalidă nu trebuia să fie salvată");
    }
}