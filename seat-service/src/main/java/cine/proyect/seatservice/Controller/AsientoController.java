package cine.proyect.seatservice.Controller;

import cine.proyect.seatservice.Dto.AsientoRequestDTO;
import cine.proyect.seatservice.Model.Asiento;
import cine.proyect.seatservice.Service.AsientoService;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v2/cine/asiento")
@Tag(name = "ASIENTOS", description = "API RELACIONADA A LA CREACION DE ASIENTOS")
@RequiredArgsConstructor
public class AsientoController {
    @Autowired
    private AsientoService asientoService;

    @GetMapping
    @Operation(summary = "METODO GET FIND ALL ASIENTOS", description = "Lista todos los asientos")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Asiento.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<List<Asiento>> getAllAsientos(){
        List<Asiento> listaAsientos =asientoService.listarAsientos();
        return new ResponseEntity<>(listaAsientos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "METODO GET FIND SEAT BY ID", description = "Busca un usuario por id especifica")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. Se encontró el asiento.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Asiento.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Asiento no encontrado. El ID proporcionado no coincide con ningún registro.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<Asiento> getSeatById(@PathVariable Long id) {
        return ResponseEntity.ok(asientoService.findAsientoById(id));
    }

    @PostMapping
    @Operation(summary = "METODO POST CREATE SEAT", description = "Crea un nuevo asiento.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Asiento creado exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Asiento.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición no valida. Los datos enviados no cumplen con las validaciones requeridas (ej. roomId inválido, campos obligatorios vacíos).",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No  se pudo crear el asiento",
                    content = @Content
            )
    })
    public ResponseEntity<Asiento> crear(@Valid @RequestBody AsientoRequestDTO dto) {
        Asiento nuevo = asientoService.crearAsiento(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping("/sala/{salaId}")
    @Operation(summary = "METODO GET FIND USER BY SALA ID", description = "Busca un asiento por id especifica de room")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. Se encontró el asiento.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Asiento.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<List<Asiento>> listar(@PathVariable Long salaId) {
        return ResponseEntity.ok(asientoService.listarPorSala(salaId));
    }

    @PutMapping("/{id}/estado")
    @Operation(summary = "METODO PUT UPDATE SEAT STATUS ", description = "Actualiza el estado de un asiento mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. El asiento fue actualizado correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Asiento.class)
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
    public ResponseEntity<Asiento> actualizarEstado(@PathVariable Long id, @RequestParam boolean disponible) {
        return ResponseEntity.ok(asientoService.actualizarEstado(id, disponible));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "METODO DELETE SEAT", description = "Elimina un asiento existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Operación exitosa. El asiento fue eliminado correctamente. No se devuelve contenido.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. El formato del ID proporcionado no es correcto.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Asiento no encontrado. El ID solicitado no existe en el sistema.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo eliminar el usuario.",
                    content = @Content
            )
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asientoService.eliminarAsiento(id);
        return ResponseEntity.noContent().build();
    }
}