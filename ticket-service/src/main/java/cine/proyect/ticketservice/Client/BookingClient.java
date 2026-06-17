package cine.proyect.ticketservice.Client;

import cine.proyect.ticketservice.DTO.bookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name= "booking-service")
public interface BookingClient {

    @GetMapping("/api/v2/cine/bookings/{id}")
    bookingDTO buscarReservaPorId(@PathVariable("id") Long id);
}
