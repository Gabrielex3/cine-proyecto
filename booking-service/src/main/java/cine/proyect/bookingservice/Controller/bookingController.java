package cine.proyect.bookingservice.Controller;

import cine.proyect.bookingservice.DTO.bookingDTO;
import cine.proyect.bookingservice.Model.booking;
import cine.proyect.bookingservice.Model.bookingStatus;
import cine.proyect.bookingservice.Service.bookingService;
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
public class bookingController {

    @Autowired
    private bookingService bookingService;

    @GetMapping
    public ResponseEntity<List<booking>> findAllBookings() {
        log.info("Listar Reservas : Buscando todas las reservas");
        return ResponseEntity.ok(bookingService.findAllBookings());
    }

    @GetMapping("/{id}")
    public ResponseEntity<booking> getBookingById(@PathVariable Long id) {
        log.info("Buscar Reserva por Id: Obteniendo usuario con ID {}", id);
        return ResponseEntity.ok(bookingService.findBookingById(id));
    }

    @PostMapping
    public ResponseEntity<booking> createBooking(@Valid @RequestBody bookingDTO dto) {
        log.info("Crear Reserva: Creando nuevo reserva con RUT {}", dto.getUserId());
        booking nuevaReserva = bookingService.createBooking(dto);
        return new ResponseEntity<>(nuevaReserva, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<booking> updateBooking(@PathVariable Long id, @Valid @RequestBody bookingDTO dto) {
        booking reservaActualizada = bookingService.actualizarReserva(id, dto);

        return ResponseEntity.ok(reservaActualizada);
    }

    @PutMapping("/cambiarestado/{id}")
    public ResponseEntity<booking> updateStatusBooking(@PathVariable Long id, @RequestBody bookingDTO dto) {
        booking statusActualizado = bookingService.actualizarEstado(id, dto);
        return ResponseEntity.ok(statusActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        log.info("Eliminar Reserva: Eliminando reserva con ID {}", id);
        bookingService.deleteBooking(id);
        return ResponseEntity.noContent().build();
    }

}
