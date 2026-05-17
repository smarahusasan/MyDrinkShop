package drinkshop.it;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.repository.file.FileRetetaRepository;
import drinkshop.service.RetetaService;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RetetaServiceMockValidatorTest {
    private RetetaService retetaService;
    private final String testFileName = "test_mock_validator.txt";

    @Mock
    private Validator<Reteta> validatorMock;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        File file = new File(testFileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        FileRetetaRepository repoReal = new FileRetetaRepository(testFileName);

        retetaService = new RetetaService(repoReal, validatorMock);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(new File(testFileName).toPath());
    }

    @Test
    void testAddReteta_RealRepo_MockValidator_SavesDirectly() {
        Reteta reteta = new Reteta(77, Arrays.asList(new IngredientReteta("Test", 1.0)));

        retetaService.addReteta(reteta);

        verify(validatorMock, times(1)).validate(reteta);

        Reteta salvata = retetaService.findById(77);
        assertNotNull(salvata);
        assertEquals(77, salvata.getId());
    }

    @Test
    void testAddReteta_ValidatorMockSimulatesError() {
        Reteta reteta = new Reteta(88, Arrays.asList(new IngredientReteta("Eroare", 1.0)));
        doThrow(new RuntimeException("Validare esuata artificial")).when(validatorMock).validate(reteta);

        assertThrows(RuntimeException.class, () -> {
            retetaService.addReteta(reteta);
        });

        assertNull(retetaService.findById(88));
    }
}