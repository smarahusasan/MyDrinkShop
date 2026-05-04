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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

//comp reale, obiect mock-uit
class RetetaServiceTestObjectMock {

    private RetetaService retetaService;
    private final String testFileName = "test_mock_entity.txt";

    @Mock
    private Reteta retetaMock;

    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);

        File file = new File(testFileName);
        if (!file.exists()) file.createNewFile();

        FileRetetaRepository repoReal = new FileRetetaRepository(testFileName);
        RetetaValidator validatorReal = new RetetaValidator();

        retetaService = new RetetaService(repoReal, validatorReal);
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(new File(testFileName).toPath());
    }

    @Test
    void testAddReteta_RealRepo_MockEntity_Success() {
        when(retetaMock.getId()).thenReturn(55);
        when(retetaMock.getIngrediente()).thenReturn(Collections.singletonList(
                new IngredientReteta("MockIngredient", 10.0)
        ));

        retetaService.addReteta(retetaMock);

        Reteta salvata = retetaService.findById(55);

        assertNotNull(salvata);
        assertEquals(55, salvata.getId());
        assertEquals("MockIngredient", salvata.getIngrediente().get(0).getDenumire());
    }

    @Test
    void testAddReteta_RealRepo_MockEntity_FailsValidation() {
        when(retetaMock.getId()).thenReturn(-99); // ID invalid
        when(retetaMock.getIngrediente()).thenReturn(Collections.emptyList());

        assertThrows(ValidationException.class, () -> {
            retetaService.addReteta(retetaMock);
        });

        assertNull(retetaService.findById(-99));
    }
}