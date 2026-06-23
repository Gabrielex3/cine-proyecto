package cine.proyect.movieservice.Controller;

import cine.proyect.movieservice.DTO.MovieDTO;
import cine.proyect.movieservice.Service.movieService;
import cine.proyect.movieservice.Model.Movie;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("api/v2/cine/movie")
@Tag(name = "MOVIES", description = "API RELACIONADA A LA CREACION DE PELICULAS")
public class movieController {

    @Autowired
    private movieService service;

    @GetMapping
    @Operation(summary = "METODO GET FIND ALL MOVIES", description = "Lista todos las peliculas")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Movie.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<List<Movie>> getAllMovies(){
        log.info("REST request: Solicitando la lista completa de peliculas");
        return ResponseEntity.ok(service.getAllMovies());
    }

    @GetMapping("/{id}")
    @Operation(summary = "METODO GET FIND MOVIE BY ID", description = "Busca una pelicula por id especifico")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. Se encontró la pelicula.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<Movie> getMovieId(@PathVariable Long id) {
        log.info("REST request: Solicitando pelicula con ID: {}", id);
        return ResponseEntity.ok(service.getMovieById(id));
    }

    @PostMapping
    @Operation(summary = "METODO POST CREATE MOVIE", description = "Crea una nueva pelicula.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pelicula creada exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición no valida. Los datos enviados no cumplen con las validaciones requeridas (ej. TITULO inválido, campos obligatorios vacíos).",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto. Ya existe una pelicula registrado con el mismo Titulo.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No  se pudo crear la pelicula",
                    content = @Content
            )
    })
    public ResponseEntity<Movie> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        log.info("REST request: Solicitud para crear pelicula: {}", movieDTO.getTitulo());
        Movie createdMovie = service.createMovie(movieDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMovie);
    }

    @PutMapping("/{id}")
    @Operation(summary = "METODO PUT UPDATE MOVIE", description = "Actualiza los datos de una pelicula existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. La pelicula fue actualizada correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Movie.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. Los datos enviados en el formulario no son válidos.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pelicula no encontrada. El ID proporcionado no coincide con ningún registro.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto. Los nuevos datos (Titulo) ya están en uso por otra pelicula.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo actualizar el registro.",
                    content = @Content
            )
    })
    public ResponseEntity<Movie> updateMovie(@PathVariable Long id, @Valid @RequestBody MovieDTO movieDTO){
        log.info("REST request: Solicitud para actualizar la pelicula con ID: {}", id);
        Movie updateMovie = service.updateMovie(id, movieDTO);
        return ResponseEntity.ok(updateMovie);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "METODO DELETE MOVIE", description = "Elimina una pelicula existente con ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Operación exitosa. La pelicula fue eliminada correctamente. No se devuelve contenido.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. El formato del ID proporcionado no es correcto.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Pelicula no encontrada. El ID solicitado no existe en el sistema.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo eliminar la Pelicula.",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteMovie(@PathVariable Long id) {
        log.info("REST request: Solicitud para eliminar la pelicula con ID: {}", id);
        service.deleteMovie(id);
        return ResponseEntity.noContent().build();
    }
}
