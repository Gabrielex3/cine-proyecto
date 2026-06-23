package cine.proyect.notificationservice.Controller;

import cine.proyect.notificationservice.DTO.NotificationRequestDTO;
import cine.proyect.notificationservice.Model.Notification;
import cine.proyect.notificationservice.Service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v2/cine/notification")
@Tag(name = "NOTIFICATION", description = "API RELACIONADA A LA CREACION DE NOTIFICACIONES")

@Slf4j
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    @Operation(summary = "METODO POST CREATE NOTIFICATION", description = "Crea una notificacion a un usuario.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Notificacion creada exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Notification.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición no valida. Los datos enviados no cumplen con las validaciones requeridas (ej. Campos obligatorios vacíos).",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No  se pudo crear la notificacion",
                    content = @Content
            )
    })
    public ResponseEntity<String> enviar(@RequestBody NotificationRequestDTO dto) {
        notificationService.enviarNotificacion(dto);
        return ResponseEntity.ok("Notificación enviada");
    }

    @GetMapping("/historial")
    @Operation(summary = "METODO GET FIND ALL NOTIFICATIONS", description = "Lista todas las notificaciones")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Notification.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<List<Notification>>verHistorial(){
        return ResponseEntity.ok(notificationService.obtenerTodas());
    }

    @GetMapping("/{id}")
    @Operation(summary = "METODO GET FIND NOTIFICATION BY ID", description = "Busca una notificacion por id especifica")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. Se encontró la notificacion.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Notification.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<Notification> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.obtenerPorId(id));
    }


    @GetMapping("/user/{id}")
    @Operation(summary = "METODO GET FIND NOTIFICATION BY USER ID", description = "Busca una notificacion por id especifica de un usuario")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. Se encontraron las notificaciones del usuario.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Notification.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notificacion no encontrada. El ID proporcionado no coincide con ningún usuario registrado.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<List<Notification>> buscarPorIdUser(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.obtenerPorUsuario(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "METODO PUT UPDATE NOTIFICATION", description = "Actualiza los datos de una notificacion existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. La notificacion fue actualizada correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Notification.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. Los datos enviados en el formulario no son válidos.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notificacion no encontrada. El ID proporcionado no coincide con ningún registro.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo actualizar el registro.",
                    content = @Content
            )
    })
    public ResponseEntity<Notification> actualizar(@PathVariable Long id, @RequestBody NotificationRequestDTO dto) {
        return ResponseEntity.ok(notificationService.actualizarNotificacion(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "METODO DELETE NOTIFICATION", description = "Elimina una notificacion existente con ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Operación exitosa. La notificacion fue eliminado correctamente. No se devuelve contenido.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. El formato del ID proporcionado no es correcto.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Notificacion no encontrada. El ID solicitado no existe en el sistema.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo eliminar la Notificacion.",
                    content = @Content
            )
    })
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        notificationService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
