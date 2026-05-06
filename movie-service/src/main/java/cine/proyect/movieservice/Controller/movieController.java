package cine.proyect.movieservice.Controller;

import cine.proyect.movieservice.DTO.MovieDTO;
import cine.proyect.movieservice.Service.movieService;
import cine.proyect.movieservice.Model.Movie;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Provider;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v1/cine/movie")
public class movieController {

    @Autowired
    private movieService service;

    @GetMapping
    public ResponseEntity<List<Movie>> getAllMovies(){
        log.info("REST request: Solicitando la lista completa de peliculas");
        return ResponseEntity.ok(service.getAllMovies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieId(@PathVariable Long id) {
        log.info("REST request: Solicitando pelicula con ID: {}", id);
        return ResponseEntity.ok(service.getMovieById(id));
    }

    @PostMapping
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        log.info("REST request: Solicitud para crear pelicula: {}", movieDTO.getTitulo());
        Movie createdMovie = service.createMovie(movieDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieDTO movieDTO){
        log.info("REST request: Solicitud para actualizar la pelicula con ID: {}", id);
        Movie updateMovie = service.updateMovie(id, movieDTO);
        return ResponseEntity.ok(updateMovie);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.info("REST request: Solicitud para eliminar la pelicula con ID: {}", id);
        service.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
