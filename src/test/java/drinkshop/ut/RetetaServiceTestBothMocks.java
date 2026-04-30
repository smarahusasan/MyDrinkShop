package drinkshop.ut;

import drinkshop.domain.Reteta;
import drinkshop.repository.Repository;
import drinkshop.service.RetetaService;
import drinkshop.service.validator.ValidationException;
import drinkshop.service.validator.Validator;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class RetetaServiceTestBothMocks {
    @Mock
    private Validator<Reteta> retetaValidator;
    @Mock
    private Repository<Integer,Reteta> retetaRepository;
    @Mock
    private Reteta reteta;

    @InjectMocks
    private RetetaService retetaService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown(){
        retetaService = null;
        retetaRepository = null;
        retetaValidator = null;
    }

    @Test
    void testAddReteta_Valid(){

        retetaService.addReteta(reteta);

        verify(retetaValidator).validate(reteta);
        verify(retetaRepository).save(reteta);
    }

    @Test
    void testAddReteta_invalid() {

        doThrow(new ValidationException("invalid"))
                .when(retetaValidator).validate(reteta);

        assertThrows(ValidationException.class, () -> retetaService.addReteta(reteta));

        verify(retetaValidator).validate(reteta);
        verify(retetaRepository, never()).save(reteta);
    }


    @Test
    void testFindById() {

        when(retetaRepository.findOne(1)).thenReturn(reteta);

        Reteta result = retetaService.findById(1);

        assertEquals(reteta, result);
        verify(retetaRepository).findOne(1);
    }

    @Test
    void testGetAll() {
        Reteta reteta1=mock(Reteta.class);
        List<Reteta> list = List.of(reteta,reteta1);

        when(retetaRepository.findAll()).thenReturn(list);

        List<Reteta> result = retetaService.getAll();

        assertEquals(2, result.size());
        assertEquals(list, result);

        verify(retetaRepository).findAll();
    }
}
