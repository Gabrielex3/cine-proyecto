package cine.proyect.bookingservice.Client;

import cine.proyect.bookingservice.DTO.userDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", url = "http://localhost:8080")
public interface userClient {
    @GetMapping("/api/v1/cine/users/{id}")
    userDTO obtenerPorIdUser(@PathVariable("id") Long id);
}
