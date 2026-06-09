package cine.proyect.ticketservice.Client;

import cine.proyect.ticketservice.DTO.PaymentRequestDTO;
import cine.proyect.ticketservice.DTO.bookingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "payment-service", url = "http://localhost:8084/api/v1/cine/payment")
public interface PaymentClient {

    @GetMapping("/payment/{id}")
    PaymentRequestDTO getPaymentByBookingId(@PathVariable("id") Long bookingId);
}
