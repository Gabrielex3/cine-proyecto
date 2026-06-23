package cine.proyect.movieservice;

import cine.proyect.movieservice.DTO.MovieDTO;
import cine.proyect.movieservice.Model.Movie;
import cine.proyect.movieservice.Repository.movieRepository;
import cine.proyect.movieservice.Service.movieService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;


import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("local")
public class movieServiceTest {

    @Autowired
    private movieService service;
    @MockitoBean
    private movieRepository repo;

    @Test
    public void testFindAll() {
        when(repo.findAll()).thenReturn(List.of(new Movie(1L,"Toy story 5","Infantil",3)));

        List<Movie> peliculas = service.getAllMovies();

        assertNotNull(peliculas);
        assertEquals(1, peliculas.size());
    }

    @Test
    public void testGetMovieById() {
        Movie movieMock = new Movie(1L, "Interstellar", "Sci-Fi", 169);

        when(repo.findById(1L)).thenReturn(Optional.of(movieMock));

        Movie resultado = service.getMovieById(1L);

        assertNotNull(resultado);
        assertEquals("Interstellar", resultado.getTitulo());
    }


    @Test
    public void testSave() {
        MovieDTO dto = new MovieDTO("The Matrix","Sci-Fi",136);
        Movie peliculaGuardada = new Movie(10L, "The Matrix", "Sci-Fi", 136);

        when(repo.findById(peliculaGuardada.getId())).thenReturn(Optional.empty());

        when(repo.save(any(Movie.class))).thenReturn(peliculaGuardada);

        Movie saved = service.createMovie(dto);

        assertNotNull(saved);
        assertEquals(10L, saved.getId());
    }

    @Test
    public void testUpdateMovie() {
        Long id = 1L;
        MovieDTO dto = new MovieDTO("The Matrix Reloaded","Sci-Fi",136);

        Movie existente = new Movie(id, "The Matrix", "Sci-Fi", 136);
        Movie actualizada = new Movie(id, "The Matrix Reloaded", "Sci-Fi", 138);

        when(repo.findById(id)).thenReturn(Optional.of(existente));
        when(repo.save(any(Movie.class))).thenReturn(actualizada);

        Movie resultado = service.updateMovie(id, dto);

        assertNotNull(resultado);
        assertEquals("The Matrix Reloaded", resultado.getTitulo(), "El título debió actualizarse");
    }

    @Test
    public void testDeleteMovie() {
        Long id = 1L;
        Movie existente = new Movie(id,"Pelicula de prueba","Sci-Fi",136);

        when(repo.findById(id)).thenReturn(Optional.of(existente));

        doNothing().when(repo).delete(any(Movie.class));

        service.deleteMovie(id);

        verify(repo, times(1)).delete(existente);
    }

}
