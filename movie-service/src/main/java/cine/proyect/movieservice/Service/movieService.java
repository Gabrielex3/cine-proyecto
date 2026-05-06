package cine.proyect.movieservice.Service;

import cine.proyect.movieservice.DTO.MovieDTO;
import cine.proyect.movieservice.Model.Movie;
import lombok.extern.slf4j.Slf4j;
import cine.proyect.movieservice.Repository.movieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class movieService {

    @Autowired
    movieRepository repo;

    public List<Movie> getAllMovies(){
        log.info("Obteniendo todas las peliculas de la base de datos.");
        return repo.findAll();
    }

    public Movie getMovieById(long id){
        log.info("Buscando pelicula con ID: {}", id);
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Pelicula no encontrada con el ID: " + id));
    }

    public Movie createMovie(MovieDTO dto){
        try {
            log.info("Iniciando creacion de pelicula con titulo {}", dto.getTitulo());
            Movie movie = new Movie();
            movie.setTitulo(dto.getTitulo());
            movie.setGenero(dto.getGenero());
            movie.setDuracion(dto.getDuracion());

            Movie savedMovie = repo.save(movie);
            log.info("Pelicula creada exitosamente con ID: {}", savedMovie.getId());
            return savedMovie;
        } catch (Exception e){
            log.error("Error al guardar la pelicula en la BD: {}", e.getMessage());
            throw new RuntimeException("Error interno al intentar crear la pelicula");
        }
    }

    public Movie updateMovie(Long id, MovieDTO dto){
        try {
            log.info("Iniciando actualizacion de la pelicula con ID: {}", id);

            Movie existingMovie = getMovieById(id);

            existingMovie.setTitulo(dto.getTitulo());
            existingMovie.setGenero(dto.getGenero());
            existingMovie.setDuracion(dto.getDuracion());

            Movie updateMovie = repo.save(existingMovie);
            log.info("Pelicula con ID: {} actualizada exitosamente!", updateMovie.getId());
            return updateMovie;

        }   catch (RuntimeException e){
                throw e;
        }   catch (Exception e){
                log.error("Error al actualizar la pelicula {}: {}", id, e.getMessage());
                throw new RuntimeException("Error interno al intentar actualizar la pelicula");
        }
    }

    public void deleteMovie(Long id){
        try {
            log.info("Iniciando eliminacion de la pelicula con ID: {}", id);

            Movie existingMovie = getMovieById(id);
            repo.delete(existingMovie);
            log.info("Pelicula con ID: {} eliminada correctamente", id);

        }   catch (RuntimeException e) {
            throw e;
        }   catch (Exception e) {
            log.error("Error al Eliminar la pelicula {}: {}", id, e.getMessage());
            throw new RuntimeException("Error interno al intentar eliminar la pelicula");
        }
    }
}
