package cine.proyect.bookingservice.Controller;

import cine.proyect.bookingservice.DTO.bookingDTO;
import cine.proyect.bookingservice.Model.booking;
import cine.proyect.bookingservice.Service.bookingService;
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
@RequestMapping("/api/v1/cine/bookings")
@Tag(name = "BOOKINGS", description = "API RELACIONADA A LA CREACION DE RESERVAS")
public class bookingController {

    @Autowired
    private bookingService bookingService;

    @GetMapping
    @Operation(summary = "METODO GET FIND ALL BOOKINGS", description = "Lista todas las reservas")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = booking.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<List<booking>> findAllBookings() {
        log.info("Listar reservas : Buscando todas las reservas");
        return ResponseEntity.ok(bookingService.findAllBookings());
    }

    @GetMapping("/{id}")
    @Operation(summary = "METODO GET FIND BOOKING BY ID", description = "Busca una reserva por ID especifico")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. Se encontró el usuario.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = booking.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<booking> getBookingById(@PathVariable Long id) {
        log.info("BuscarPorId: Obteniendo reserva con ID {}", id);
        return ResponseEntity.ok(bookingService.findBookingById(id));
    }

    @PostMapping
    @Operation(summary = "METODO POST CREATE BOOKING", description = "Crea una nueva reserva.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Reserva creada exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = booking.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición no valida. Los datos enviados no cumplen con las validaciones requeridas (ej. ID Showtime inválido, campos obligatorios vacíos).",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto. Ya existe una reserva registrada con el mismo ShowtimeID o SeatID.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No  se pudo crear la reserva",
                    content = @Content
            )
    })
    public ResponseEntity<booking> create(@Valid @RequestBody bookingDTO dto) {
        log.info("Crear: Creando nueva reserva con RUT {}", dto.getUserId());
        booking nuevoBooking = bookingService.createBooking(dto);
        return new ResponseEntity<booking>(nuevoBooking, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "METODO PUT UPDATE BOOKING", description = "Actualiza los datos de una reserva existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. La reserva fue actualizada correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = booking.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. Los datos enviados en el formulario no son válidos.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada. El ID proporcionado no coincide con ningún registro.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo actualizar el registro.",
                    content = @Content
            )
    })
    public ResponseEntity<booking> updateBooking(@PathVariable Long id, @Valid @RequestBody bookingDTO dto) {
        booking reservaActualizada = bookingService.updateBooking(id, dto);

        return ResponseEntity.ok(reservaActualizada);
    }

    @PutMapping("/cambiarestado/{id}")
    @Operation(summary = "METODO PUT UPDATE BOOKING STATUS", description = "Actualiza el estado de una reserva existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. El estado de la reserva fue actualizada correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = booking.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. Los datos enviados en el formulario no son válidos.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Estado de reserva no encontrada. El ID proporcionado no coincide con ningún registro.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo actualizar el registro.",
                    content = @Content
            )
    })
    public ResponseEntity<booking> updateStatusBooking(@PathVariable Long id, @RequestBody bookingDTO dto) {
        booking statusActualizado = bookingService.actualizarEstado(id, dto);
        return ResponseEntity.ok(statusActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "METODO DELETE BOOKING", description = "Elimina una reserva existente con ID")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Operación exitosa. La reserva fue eliminada correctamente. No se devuelve contenido.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. El formato del ID proporcionado no es correcto.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Reserva no encontrada. El ID solicitado no existe en el sistema.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo eliminar la reserva.",
                    content = @Content
            )
    })
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        log.info("Eliminar Reserva: Eliminando reserva con ID {}", id);
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

}
