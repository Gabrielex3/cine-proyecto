package cine.proyect.cinemaservice;

import cine.proyect.cinemaservice.DTO.CinemaDTO;
import cine.proyect.cinemaservice.Model.Cinema;
import cine.proyect.cinemaservice.Model.comunas;
import cine.proyect.cinemaservice.Repository.RepositoryCinema;
import cine.proyect.cinemaservice.Repository.comunaRepository;
import cine.proyect.cinemaservice.Service.ServiceCinema;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("local")
public class cinemaServiceTest {
    @Autowired
    private ServiceCinema service;

    @MockitoBean
    private RepositoryCinema repo;

    @MockitoBean
    private comunaRepository crepo;

    @Test
    public void testGetAllCinemas() {
        Cinema cine = new Cinema(1L,"Cinepolis Costanera","AV AMERICO VESPUCIO 255",new comunas(1L,"La florida",List.of()));

        when(repo.findAll()).thenReturn(List.of(cine));

        List<Cinema> cinemas = service.getAllCinemas();

        assertFalse(cinemas.isEmpty());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Cinema cine = new Cinema(1L,"Cinepolis Costanera","AV AMERICO VESPUCIO 255",new comunas(1L,"La florida",List.of()));

        when(repo.findById(id)).thenReturn(Optional.of(cine));

        Cinema found = service.getCinemaById(id);

        assertNotNull(found);
        assertEquals(id, found.getId(), "El ID del usuario devuelto no coincide");
    }

    @Test
    public void testSave() {
        CinemaDTO dtoEntrada = new CinemaDTO("Cinepolis Costanera", "AV AMERICO VESPUCIO 255", 1L);

        comunas comuna = new comunas(1L, "La florida", new java.util.ArrayList<>());

        Cinema cineNuevo = new Cinema(1L, "Cinepolis Costanera", "AV AMERICO VESPUCIO 255", comuna);

        comuna.getCinemas().add(cineNuevo);

        when(crepo.findById(1L)).thenReturn(Optional.of(comuna));
        when(repo.save(any(Cinema.class))).thenReturn(cineNuevo);

        Cinema saved = service.crearCinema(dtoEntrada);

        assertNotNull(saved);
        assertEquals("La florida", saved.getComuna().getNombre());
        assertEquals(1L, saved.getComuna().getId());
    }

    @Test
    public void testUpdateCinema() {
        Long idCine = 1L;

        comunas nuevaComuna = new comunas(2L, "Las Condes", new java.util.ArrayList<>());

        CinemaDTO dtoActualizar = new CinemaDTO("Cinepolis Costanera (Editado)", "Nueva Direccion 123", 2L);

        Cinema existente = new Cinema(idCine, "Cinepolis Costanera", "AV AMERICO VESPUCIO 255", new comunas(1L, "La Florida", new java.util.ArrayList<>()));
        Cinema actualizado = new Cinema(idCine, "Cinepolis Costanera (Editado)", "Nueva Direccion 123", nuevaComuna);

        when(crepo.findById(2L)).thenReturn(Optional.of(nuevaComuna));
        when(repo.findById(idCine)).thenReturn(Optional.of(existente));
        when(repo.save(any(Cinema.class))).thenReturn(actualizado);

        Cinema resultado = service.actualizarCinema(idCine, dtoActualizar);

        assertNotNull(resultado);
        assertEquals("Cinepolis Costanera (Editado)", resultado.getCine(), "El nombre del cine no se actualizó");
        assertEquals("Nueva Direccion 123", resultado.getDireccion(), "La dirección no se actualizó");
        assertEquals("Las Condes", resultado.getComuna().getNombre(), "La comuna no cambió a la del DTO");
    }

    @Test
    public void testEliminarCinema() {
        Long idCine = 1L;
        comunas comuna = new comunas(1L, "La Florida", new java.util.ArrayList<>());
        Cinema existente = new Cinema(idCine, "Cinepolis Costanera", "AV AMERICO VESPUCIO 255", comuna);

        when(repo.findById(idCine)).thenReturn(Optional.of(existente));
        doNothing().when(repo).delete(any(Cinema.class));

        service.eliminarCinema(idCine);

        verify(repo, times(1)).delete(existente);
    }
}
