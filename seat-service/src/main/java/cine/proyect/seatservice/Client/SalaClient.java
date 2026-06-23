package cine.proyect.seatservice.Client;

import cine.proyect.seatservice.Dto.roomDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "room-services")
public interface SalaClient {

    @GetMapping("/api/v2/cine/room/{id}")
    roomDTO obtenerSalaPorId(@PathVariable("id") Long id);
}