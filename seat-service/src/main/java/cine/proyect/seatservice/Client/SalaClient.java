package cine.proyect.seatservice.Client;

import cine.proyect.seatservice.Dto.roomDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "room-service", url = "http://localhost:8086")
public interface SalaClient {

    @GetMapping("/api/v1/cine/room/{id}")
    roomDTO obtenerSalaPorId(@PathVariable("id") Long id);
}