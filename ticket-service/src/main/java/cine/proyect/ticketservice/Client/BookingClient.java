package cine.proyect.ticketservice.Client;

import cine.proyect.ticketservice.DTO.bookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@FeignClient(name= "booking-service", url = "http://localhost:8085/api/v1/cine/bookings")
public interface BookingClient {

    @GetMapping("/api/v1/cine/bookings/{id}")
    bookingDTO buscarReservaPorId(@PathVariable("id") Long id);
}
