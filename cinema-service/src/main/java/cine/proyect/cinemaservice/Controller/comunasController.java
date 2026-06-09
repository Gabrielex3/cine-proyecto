package cine.proyect.cinemaservice.Controller;


import cine.proyect.cinemaservice.DTO.comunasDTO;
import cine.proyect.cinemaservice.Model.comunas;
import cine.proyect.cinemaservice.Service.comunaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cine/comunas")
@Tag(name = "COMUNAS", description = "API RELACIONADA A LA CREACION DE COMUNAS")
public class comunasController {

    @Autowired
    private comunaService service;

    @GetMapping
    @Operation(summary = "METODO GET FIND ALL COMUNAS", description = "Lista todos las comunas")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = comunas.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<List<comunas>> listarComunas() {
        return ResponseEntity.ok(service.obtenerComunas());
    }

    @PostMapping
    @Operation(summary = "METODO POST CREATE COMUNA", description = "Crea una nueva Comuna.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Comuna creada exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = comunas.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición no valida. Los datos enviados no cumplen con las validaciones requeridas (ej. Nombre inválido, campos obligatorios vacíos).",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto. Ya existe una comuna registrada con el mismo Nombre.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No  se pudo crear la comuna",
                    content = @Content
            )
    })
    public ResponseEntity<comunas> crearComuna(@Valid @RequestBody comunasDTO dto) {
        comunas nuevaComuna = service.crearComuna(dto);
        return new ResponseEntity<>(nuevaComuna, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "METODO PUT UPDATE COMUNA", description = "Actualiza los datos de una comuna existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. La comuna fue actualizada correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = comunas.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. Los datos enviados en el formulario no son válidos.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Comuna no encontrada. El ID proporcionado no coincide con ningún registro.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto. Los nuevos datos (Nombre) ya están en uso por otra comuna.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo actualizar el registro.",
                    content = @Content
            )
    })
    public ResponseEntity<comunas> actualizarComuna(@PathVariable Long id, @Valid @RequestBody comunasDTO dto) {
        return ResponseEntity.ok(service.actualizarComuna(id, dto));
    }

    @DeleteMapping("/{id}")
@Operation(summary = "METODO DELETE COMUNA", description = "Elimina una comuna existente con ID")
@ApiResponses(value = {
        @ApiResponse(
                responseCode = "204",
                description = "Operación exitosa. La comuna fue eliminada correctamente. No se devuelve contenido.",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "400",
                description = "Petición inválida. El formato del ID proporcionado no es correcto.",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "404",
                description = "Comuna no encontrada. El ID solicitado no existe en el sistema.",
                content = @Content
        ),
        @ApiResponse(
                responseCode = "500",
                description = "Error interno del servidor. No se pudo eliminar la comuna.",
                content = @Content
        )
})
    public ResponseEntity<Void> eliminarComuna(@PathVariable Long id) {
        service.eliminarComuna(id);
        return ResponseEntity.noContent().build();
    }
}
