package cine.proyect.showtimeservice;

import cine.proyect.showtimeservice.Client.movieClient;
import cine.proyect.showtimeservice.Client.roomClient;
import cine.proyect.showtimeservice.dto.ShowtimeRequestDTO;
import cine.proyect.showtimeservice.dto.movieDTO;
import cine.proyect.showtimeservice.dto.roomDTO;
import cine.proyect.showtimeservice.model.Showtime;
import cine.proyect.showtimeservice.repository.ShowtimeRepository;
import cine.proyect.showtimeservice.service.ShowtimeService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
public class showtimeServiceTest {
    @Autowired
    private ShowtimeService service;

    @MockitoBean
    private ShowtimeRepository repo;

    @MockitoBean
    private  movieClient movieClient;
    @MockitoBean
    private roomClient roomClient;

    @Test
    public void testFindAll() {
        when(repo.findAll()).thenReturn(List.of(new Showtime(1L, 1L, 1L, LocalDateTime.parse("2026-03-26T21:30:00"), 10000.0)));

        List<Showtime> funciones = service.findAll();

        assertNotNull(funciones);
        assertEquals(1, funciones.size());


    }
    @Test
    public void testDeleteById() {
        Long id = 1L;

        when(repo.existsById(id)).thenReturn(true);

        doNothing().when(repo).deleteById(id);

        service.deleteShowtime(id);

        verify(repo, times(1)).deleteById(id);
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Showtime showtime = new Showtime(id, 1L, 1L,LocalDateTime.parse("2026-03-26T21:30:00"), 10000.0);

        when(repo.findById(id)).thenReturn(Optional.of(showtime));

        Showtime found = service.buscarPorId(id);

        assertNotNull(found);
        assertEquals(id, found.getId(), "El ID de la funcion devuelto no coincide");
    }

    @Test
    public void testSave() {
        ShowtimeRequestDTO dtoEntrada = new ShowtimeRequestDTO(1L, 1L, 10000.0, LocalDateTime.parse("2026-03-26T21:30:00"));
        Showtime showtimeNueva = new Showtime(1L, 1L, 1L, LocalDateTime.parse("2026-03-26T21:30:00"), 10000.0);

        movieDTO peliFalsa = new movieDTO("Toy Story 5","Ninos",3);
        roomDTO salaFalsa = new roomDTO("Sala imax 2","4DX",true);

        when(movieClient.obtenerPeliculaPorId(1L)).thenReturn(peliFalsa);
        when(roomClient.obtenerSalaPorId(1L)).thenReturn(salaFalsa);

        when(repo.save(any(Showtime.class))).thenReturn(showtimeNueva);

        // 2. ACT
        Showtime saved = service.createShowtime(dtoEntrada);

        // 3. ASSERT
        assertNotNull(saved);
        assertEquals(1L, saved.getId(), "El ID debería ser 1");
    }

    @Test
    public void testUpdate() {
        Long id = 1L;

        ShowtimeRequestDTO dtoActualizacion = new ShowtimeRequestDTO(2L, 1L, 15000.0, LocalDateTime.parse("2026-03-26T21:30:00"));

        Showtime funcionExistente = new Showtime(id, 1L, 1L, LocalDateTime.parse("2026-03-26T21:30:00"), 10000.0);

        Showtime funcionModificada = new Showtime(id, 2L, 1L, LocalDateTime.parse("2026-03-26T21:30:00"), 15000.0);

        when(repo.findById(id)).thenReturn(Optional.of(funcionExistente));
        when(repo.save(any(Showtime.class))).thenReturn(funcionModificada);

        Showtime resultado = service.actualizar(id, dtoActualizacion);

        assertNotNull(resultado, "La función actualizada no debería ser nula");

        assertEquals(2L, resultado.getMovieId(), "El ID de la película no se actualizó correctamente");
        assertEquals(15000.0, resultado.getPrecioTicket(), "El precio de la función no se actualizó correctamente");
    }
}
