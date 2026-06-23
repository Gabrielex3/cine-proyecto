package cine.proyect.cinemaservice.Controller;


import cine.proyect.cinemaservice.DTO.CinemaDTO;
import cine.proyect.cinemaservice.Model.Cinema;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/v1/cine/cinemas")
@Tag(name = "CINEMAS", description = "API RELACIONADA A LA CREACION DE CINEMAS")
public class ControllerCinema {

    @Autowired
    private cine.proyect.cinemaservice.Service.ServiceCinema service;

    @GetMapping
    @Operation(summary = "METODO GET FIND ALL CINEMAS", description = "Lista todos los cines")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Cinema.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<List<Cinema>> getAllCinemas() {
        log.info("REST request: Solicitando lista completa de sucursales");
        return ResponseEntity.ok(service.getAllCinemas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "METODO GET FIND CINEMA BY ID", description = "Busca un cine por id especifico")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. Se encontró el Cine.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Cinema.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<Cinema> getCinemaById(@PathVariable Long id) {
        log.info("REST request: Solicitando sucursal con ID: {}", id);
        return ResponseEntity.ok(service.getCinemaById(id));
    }

    @PostMapping
    @Operation(summary = "METODO POST CREATE CINEMA", description = "Crea un nuevo cine.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Cine creado exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Cinema.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición no valida. Los datos enviados no cumplen con las validaciones requeridas (ej. Nombre inválido, campos obligatorios vacíos).",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No  se pudo crear el Cine",
                    content = @Content
            )
    })
    public ResponseEntity<Cinema> crearCinema(@Valid @RequestBody CinemaDTO dto) {
        log.info("REST request: Solicitud para crear sucursal: {}", dto.getCine());
        Cinema crearCinema = service.crearCinema(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(crearCinema);
    }

    @PutMapping("/{id}")
    @Operation(summary = "METODO PUT UPDATE CINEMA", description = "Actualiza los datos de un cine existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. El cine fue actualizado correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Cinema.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. Los datos enviados en el formulario no son válidos.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cine no encontrado. El ID proporcionado no coincide con ningún registro.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo actualizar el registro.",
                    content = @Content
            )
    })
    public ResponseEntity<Cinema> actualizarCinema(@PathVariable Long id, @Valid @RequestBody CinemaDTO dto) {
        log.info("REST request: Solicitud para actualizar la sucursal con ID: {}", id);
        Cinema actualizarCinema = service.actualizarCinema(id, dto);
        return ResponseEntity.ok(actualizarCinema);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "METODO DELETE CINEMA", description = "Elimina un Cine existente con ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Operación exitosa. El cine fue eliminado correctamente. No se devuelve contenido.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. El formato del ID proporcionado no es correcto.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Cine no encontrado. El ID solicitado no existe en el sistema.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo eliminar el Cine.",
                    content = @Content
            )
    })
    public ResponseEntity<Void> eliminarCinema(@PathVariable Long id) {
        log.info("REST request: Solicitud para eliminar la sucursal con ID: {}", id);
        service.eliminarCinema(id);
        return ResponseEntity.noContent().build();
    }




}
