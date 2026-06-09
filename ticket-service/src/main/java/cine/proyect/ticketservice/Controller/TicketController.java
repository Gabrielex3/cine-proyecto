package cine.proyect.ticketservice.Controller;

import cine.proyect.ticketservice.DTO.TicketDTO;
import cine.proyect.ticketservice.Model.Ticket;
import cine.proyect.ticketservice.Service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/cine/tickets")
@Tag(name = "USUARIOS", description = "API RELACIONADA A LA CREACION DE USUARIOS")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping
    @Operation(summary = "METODO POST CREATE TICKET", description = "Crea un nuevo ticket.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Ticket creado exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición no valida. Los datos enviados no cumplen con las validaciones requeridas (ej. booking id invalido, campos obligatorios vacíos).",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No  se pudo crear el ticket",
                    content = @Content
            )
    })
    public ResponseEntity<Ticket> createTicket(@Valid @RequestBody TicketDTO dto) {
        Ticket response = ticketService.generateTicket(dto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "METODO GET FIND TICKET BY ID", description = "Busca un ticket por id especifica")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. Se encontró el ticket.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        log.info("REST request: Solicitud recibida para buscar el ticket ID: {}", id);
        TicketDTO response = ticketService.getTicketById(id);
        return ResponseEntity.ok(response);
    }
}
