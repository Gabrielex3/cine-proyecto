package cine.proyect.seatservice.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "room-service", url = "http://localhost:8081")
public interface SalaClient {

    @GetMapping("/api/sala/{id}")
    Object obtenerSalaPorId(@PathVariable("id") Long id);
}