package cine.proyect.showtimeservice.controller;

import cine.proyect.showtimeservice.dto.ShowtimeRequestDTO;
import cine.proyect.showtimeservice.model.Showtime;
import cine.proyect.showtimeservice.service.ShowtimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cine/showtime")
@Tag(name = "Showtime", description = "API RELACIONADA A LA CREACION DE FUNCIONES")

@RequiredArgsConstructor
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @PostMapping
    @Operation(summary = "METODO POST CREATE SHOWTIME", description = "Crea una nueva funcion.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Funcion creada exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Showtime.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición no valida. Los datos enviados no cumplen con las validaciones requeridas (ej. Movieid inválido, campos obligatorios vacíos).",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No  se pudo crear la funcion",
                    content = @Content
            )
    })

    public ResponseEntity<Showtime> crear(@Valid @RequestBody ShowtimeRequestDTO dto){
        return new ResponseEntity<>(showtimeService.crearFuncion(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "METODO GET FIND ALL SHOWTIMES", description = "Lista todas las funciones")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Showtime.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<List<Showtime>>listar(){
        return ResponseEntity.ok(showtimeService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "METODO GET FIND SHOWTIME BY ID", description = "Busca una funcion por id especifica")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. Se encontró la funcion.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Showtime.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<Showtime> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(showtimeService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "METODO PUT UPDATE SHOWTIME", description = "Actualiza los datos de una funcion existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. La funcion fue actualizado correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Showtime.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. Los datos enviados en el formulario no son válidos.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Funcion no encontrado. El ID proporcionado no coincide con ningún registro.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo actualizar la funcion.",
                    content = @Content
            )
    })
    public ResponseEntity<Showtime>actualizar(@PathVariable Long id, @Valid @RequestBody ShowtimeRequestDTO dto){
        return ResponseEntity.ok(showtimeService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "METODO DELETE SHOWTIME", description = "Elimina una funcion existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Operación exitosa. La funcion fue eliminada correctamente. No se devuelve contenido.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. El formato del ID proporcionado no es correcto.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado. El ID solicitado no existe en el sistema.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo eliminar el usuario.",
                    content = @Content
            )
    })

    public ResponseEntity<Void> delete(@PathVariable Long id) {
        showtimeService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
