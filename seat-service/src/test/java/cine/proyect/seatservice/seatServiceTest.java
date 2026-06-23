package cine.proyect.seatservice;

import cine.proyect.seatservice.Client.SalaClient;
import cine.proyect.seatservice.Dto.AsientoRequestDTO;
import cine.proyect.seatservice.Dto.roomDTO;
import cine.proyect.seatservice.Model.Asiento;
import cine.proyect.seatservice.Repository.AsientoRepository;
import cine.proyect.seatservice.Service.AsientoService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
public class seatServiceTest {
    @Autowired
    private AsientoService service;

    @MockitoBean
    private AsientoRepository repo;

    @MockitoBean
    private SalaClient salaclient;

    @Test
    public void testFindAll() {
        when(repo.findAll()).thenReturn(List.of(new Asiento(1L,"1-A",true,1L)));

        List<Asiento> funciones = service.listarAsientos();

        assertNotNull(funciones);
        assertEquals(1, funciones.size());


    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Asiento asiento = new Asiento(id,"1-A",true,1L);

        when(repo.findById(id)).thenReturn(Optional.of(asiento));

        Asiento found = service.findAsientoById(id);

        assertNotNull(found);
        assertEquals(id, found.getId(), "El ID de el asiento no coincide");
    }

    @Test
    public void testFindByRoomId() {
        Long salaId = 1L;
        Asiento asiento = new Asiento(1L, "1-A", true, salaId);

        when(salaclient.obtenerSalaPorId(salaId)).thenReturn(new roomDTO("Imax","2D",true));

        when(repo.findBySalaId(salaId)).thenReturn(List.of(asiento));

        List<Asiento> founds = service.listarPorSala(salaId);

        assertNotNull(founds, "La lista no debería ser nula");
        assertFalse(founds.isEmpty(), "La lista debería tener al menos un asiento");
        assertEquals(1, founds.size(), "La lista debería contener exactamente 1 asiento simulado");

        assertEquals(salaId, founds.get(0).getSalaId(), "El ID de la sala no coincide");
    }

    @Test
    public void testSave() {
        AsientoRequestDTO dtoEntrada = new AsientoRequestDTO("1-A",true, 1L);
        Asiento asientoNuevo = new Asiento(1L,"1-A",true, 1L);

        when(repo.save(any(Asiento.class))).thenReturn(asientoNuevo);


        Asiento saved = service.crearAsiento(dtoEntrada);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        when(repo.existsById(id)).thenReturn(true);

        doNothing().when(repo).deleteById(id);

        service.eliminarAsiento(id);

        verify(repo, times(1)).deleteById(id);
    }

    @Test
    public void testUpdateStatus() {
        Long id = 1L;
        boolean nuevoEstado = false;

        Asiento asientoExistente = new Asiento(id, "1-A", true, 1L);

        Asiento asientoModificado = new Asiento(id, "1-A", nuevoEstado, 1L);

        when(repo.findById(id)).thenReturn(Optional.of(asientoExistente));
        when(repo.save(any(Asiento.class))).thenReturn(asientoModificado);

        Asiento resultado = service.actualizarEstado(id, nuevoEstado);

        assertNotNull(resultado, "El asiento devuelto no debería ser nulo");

        assertEquals(nuevoEstado, resultado.isDisponible(), "El estado de disponibilidad no se actualizó");
    }
}
