package cine.proyect.bookingservice.Client;

import cine.proyect.bookingservice.DTO.seatDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "seat-service", url = "http://localhost:8083")
public interface seatClient {
    @GetMapping("/api/v1/cine/asiento/{id}")
    seatDTO obtenerAsientoPorId(@PathVariable("id") Long id);
}

