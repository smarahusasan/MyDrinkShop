package drinkshop.it;

import drinkshop.domain.IngredientReteta;
import drinkshop.domain.Reteta;
import drinkshop.repository.Repository;
import drinkshop.service.RetetaService;
import drinkshop.service.validator.RetetaValidator;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

//doar repo-ul mock
class RetetaServiceTestMockRepo {
    private RetetaService retetaService;

    @Mock
    private Repository<Integer, Reteta> retetaRepoMock;

    private Validator<Reteta> retetaValidatorReal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        retetaValidatorReal = new RetetaValidator();
        retetaService = new RetetaService(retetaRepoMock, retetaValidatorReal);
    }

    @Test
    void testIntegration_S_V_RealValidatorTriggers() {
        Reteta retetaInvalida = new Reteta(1, Collections.emptyList());

        ValidationException ex = assertThrows(ValidationException.class, () -> {
            retetaService.addReteta(retetaInvalida);
        });

        assertTrue(ex.getMessage().contains("Ingrediente empty!"));

        verify(retetaRepoMock, never()).save(any());
    }

    @Test
    void testIntegration_S_V_ValidDataFlowsToRepo() {
       List<IngredientReteta> ingrediente = Arrays.asList(
                new IngredientReteta("Apa", 200.0)
        );
        Reteta retetaValida = new Reteta(10, ingrediente);

        retetaService.addReteta(retetaValida);

        verify(retetaRepoMock, times(1)).save(retetaValida);
    }
}
