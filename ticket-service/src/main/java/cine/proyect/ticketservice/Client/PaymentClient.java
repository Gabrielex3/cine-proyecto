package cine.proyect.ticketservice.Client;

import cine.proyect.ticketservice.DTO.PaymentRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



@FeignClient(name = "payment-service")
public interface PaymentClient {

    @GetMapping("/api/v2/cine/payment/{id}")
    PaymentRequestDTO getPaymentByBookingId(@PathVariable("id") Long bookingId);
}
