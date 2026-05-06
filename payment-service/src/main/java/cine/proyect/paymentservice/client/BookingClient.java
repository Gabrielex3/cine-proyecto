package cine.proyect.paymentservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "booking-service", url = "http://localhost:8085")
public interface BookingClient {

    @GetMapping("/api/booking/{id}")
    Object buscarReservaPorId(@PathVariable("id") Long id);
}