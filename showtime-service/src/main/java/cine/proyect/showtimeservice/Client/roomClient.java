package cine.proyect.showtimeservice.Client;

import cine.proyect.showtimeservice.dto.roomDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "room-services")
public interface roomClient {
    @GetMapping("/api/v2/cine/room/{id}")
    roomDTO obtenerSalaPorId(@PathVariable("id") Long id);
}
