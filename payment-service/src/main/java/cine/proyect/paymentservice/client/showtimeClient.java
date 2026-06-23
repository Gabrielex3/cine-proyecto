package cine.proyect.paymentservice.client;

import cine.proyect.paymentservice.dto.showtimeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "showtime-service")
public interface showtimeClient {
    @GetMapping("/api/v2/cine/showtime/{id}")
    showtimeDTO obtenerPorIdShowtime(@PathVariable("id") Long id);

}
