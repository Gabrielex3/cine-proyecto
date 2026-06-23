package cine.project.roomservices;
import cine.project.roomservices.DTO.salaDTO;
import cine.project.roomservices.model.Sala;
import cine.project.roomservices.repository.SalaRepository;
import cine.project.roomservices.service.SalaService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
public class salaServiceTest {
    @Autowired
    private SalaService service;

    @MockitoBean
    private SalaRepository repo;

    @Test
    public void testFindAll() {
        when(repo.findAll()).thenReturn(List.of(new Sala(1L,"1-A","4DX", true)));

        List<Sala> salas = service.obtenerTodas();

        assertNotNull(salas);
        assertEquals(1, salas.size());


    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Sala sala = new Sala(id,"1-A","4DX", true);

        when(repo.findById(id)).thenReturn(Optional.of(sala));

        Sala found = service.obtenerPorId(id);

        assertNotNull(found);
        assertEquals(id, found.getId(), "El ID de la sala no coincide");
    }


    @Test
    public void testSave() {
        salaDTO dtoEntrada = new salaDTO("1-A","4DX", true);
        Sala salaNueva = new Sala(1L,"1-A","4DX", true);

        when(repo.save(any(Sala.class))).thenReturn(salaNueva);


        Sala saved = service.crearSala(dtoEntrada);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
    }


    @Test
    public void testUpdateStatus() {
        Long id = 1L;
        boolean nuevoEstado = false;

        Sala salaExistente = new Sala(id, "1-A", "4DX", true);
        Sala salaModificada = new Sala(id, "1-A", "4DX", nuevoEstado);

        when(repo.findById(id)).thenReturn(Optional.of(salaExistente));
        when(repo.save(any(Sala.class))).thenReturn(salaModificada);

        Sala resultado = service.desactivarSala(id, nuevoEstado);

        assertNotNull(resultado, "La sala devuelta no debería ser nula");
        assertEquals(nuevoEstado, resultado.getActivo(), "El estado de la sala no se actualizó");
    }
}

