package cine.proyect.bookingservice.Client;

import cine.proyect.bookingservice.DTO.cinemaDTO;
import cine.proyect.bookingservice.DTO.seatDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "cinema-service", url = "http://localhost:8087")
public interface cinemaClient {
    @GetMapping("/api/v1/cine/cinemas/{id}")
    cinemaDTO obtenerCinemaPorId(@PathVariable("id") Long id);
}
