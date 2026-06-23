package cine.proyect.paymentservice.client;

import cine.proyect.paymentservice.dto.bookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "booking-service")
public interface BookingClient {

    @GetMapping("/api/v2/cine/bookings/{id}")
    bookingDTO buscarReservaPorId(@PathVariable("id") Long id);
    @PutMapping("/api/v2/cine/bookings/cambiarestado/{id}")
    bookingDTO actualizarStatus(@PathVariable Long id, @RequestBody bookingDTO dto);
}