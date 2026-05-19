package cine.proyect.paymentservice.client;

import cine.proyect.paymentservice.dto.TicketDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ticket-service", url = "http://localhost:8088")
public interface TicketClient {
    @PostMapping("/api/v1/cine/tickets")
    TicketDTO crearTicket(@RequestBody TicketDTO dto);
    }

