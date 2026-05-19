package cine.proyect.paymentservice.client;

import cine.proyect.paymentservice.dto.showtimeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "showtime-service", url = "http://localhost:8082")
public interface showtimeClient {
    @GetMapping("/api/v1/cine/showtime/{id}")
    showtimeDTO obtenerPorIdShowtime(@PathVariable("id") Long id);

}
