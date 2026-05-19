package cine.proyect.ticketservice.Controller;

import cine.proyect.ticketservice.DTO.TicketDTO;
import cine.proyect.ticketservice.Model.Ticket;
import cine.proyect.ticketservice.Service.TicketService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/cine/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody TicketDTO dto) {
        Ticket response = ticketService.generateTicket(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        log.info("REST request: Solicitud recibida para buscar el ticket ID: {}", id);
        TicketDTO response = ticketService.getTicketById(id);
        return ResponseEntity.ok(response);
    }
}
