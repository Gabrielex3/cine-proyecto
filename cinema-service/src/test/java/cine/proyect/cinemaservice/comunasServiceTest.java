package cine.proyect.cinemaservice;

import cine.proyect.cinemaservice.DTO.comunasDTO;
import cine.proyect.cinemaservice.Model.comunas;
import cine.proyect.cinemaservice.Repository.comunaRepository;
import cine.proyect.cinemaservice.Service.comunaService;
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
public class comunasServiceTest {

    @Autowired
    private comunaService service;

    @MockitoBean
    private comunaRepository repo;

    @Test
    public void testObtenerComunas() {
        comunas comuna = new comunas(1L, "Peñalolén", new java.util.ArrayList<>());

        when(repo.findAll()).thenReturn(List.of(comuna));

        List<comunas> resultados = service.obtenerComunas();

        assertFalse(resultados.isEmpty());
        assertEquals(1, resultados.size());
    }

    @Test
    public void testCrearComuna() {
        comunasDTO dto = new comunasDTO("Macul");

        comunas comunaGuardada = new comunas(1L,"Macul",new java.util.ArrayList<>());

        when(repo.save(any(comunas.class))).thenReturn(comunaGuardada);

        comunas resultado = service.crearComuna(dto);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Macul", resultado.getNombre());
    }

    @Test
    public void testObtenerComunaPorId() {
        comunas comuna = new comunas(1L,"Macul",new java.util.ArrayList<>());

        when(repo.findById(1L)).thenReturn(Optional.of(comuna));

        comunas resultado = service.obtenerComunaPorId(1L);

        assertNotNull(resultado);
        assertEquals("Macul", resultado.getNombre());
    }

    @Test
    public void testActualizarComuna() {
        Long id = 1L;

        comunasDTO dto = new comunasDTO("Providencia Editada");

        comunas existente = new comunas(1L,"Providencia",new java.util.ArrayList<>());

        comunas actualizada = new comunas(1L,"Providencia Editada",new java.util.ArrayList<>());

        when(repo.findById(id)).thenReturn(Optional.of(existente));
        when(repo.save(any(comunas.class))).thenReturn(actualizada);

        comunas resultado = service.actualizarComuna(id, dto);

        assertNotNull(resultado);
        assertEquals("Providencia Editada", resultado.getNombre());
    }

    @Test
    public void testEliminarComuna() {
        Long id = 1L;

        when(repo.existsById(id)).thenReturn(true);
        doNothing().when(repo).deleteById(id);

        service.eliminarComuna(id);
        verify(repo, times(1)).existsById(id);
        verify(repo, times(1)).deleteById(id);
    }
}
