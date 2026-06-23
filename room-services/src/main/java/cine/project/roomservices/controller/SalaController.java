package cine.project.roomservices.controller;

import cine.project.roomservices.DTO.salaDTO;
import cine.project.roomservices.model.Sala;
import cine.project.roomservices.service.SalaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/cine/room")
@Tag(name = "ROOM", description = "API RELACIONADA A LA CREACION DE SALAS")
@Slf4j
@RequiredArgsConstructor
public class SalaController {
    @Autowired
    private SalaService salaService;

    @PostMapping
    @Operation(summary = "METODO POST CREATE ROOM", description = "Crea una nueva sala.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Sala creado exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Sala.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición no valida. Los datos enviados no cumplen con las validaciones requeridas (ej. Campos obligatorios vacíos).",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No  se pudo crear la sala",
                    content = @Content
            )
    })
    public ResponseEntity<Sala> crear(@Valid @RequestBody salaDTO dto){
        log.info("Iniciando crear sala : {}", dto.getNombre());
        Sala nuevaSala = salaService.crearSala(dto);
        return new ResponseEntity<>(nuevaSala, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "METODO GET FIND ALL ROOMS", description = "Busca todas las salas existentes")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Sala.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<List<Sala>> listarSalas(){
        log.info("Iniciando lista de salas");
        return ResponseEntity.ok(salaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "METODO GET FIND ROOM BY ID", description = "Busca una sala por id especifica")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. Se encontró la sala.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Sala.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<Sala>  buscarPorId(@PathVariable Long id){
        log.info("Iniciando lista de sala : {}", id);
        return ResponseEntity.ok(salaService.obtenerPorId(id));
    }


    @PutMapping("/desactivar/{id}")
    @Operation(summary = "METODO PUT UPDATE ROOM STATUS  BY ID", description = "Actualiza el estado de la sala")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. El estado de la sala fue actualizado correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Sala.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado. El ID proporcionado no coincide con ningún registro.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo actualizar el registro.",
                    content = @Content
            )
    })
    public ResponseEntity<String> desactivarSala(@PathVariable Long id) {
        salaService.desactivarSala(id);
        return ResponseEntity.ok("La sala con ID " + id + " ha sido desactivada correctamente.");
    }

}
