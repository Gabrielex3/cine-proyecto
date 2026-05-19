package cine.proyect.ticketservice.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDTO {

    private Long id;

    @NotNull(message = "El ID de la reserva es obligatorio")
    private Long bookingId;

    private Long paymentId;

    private LocalDateTime issueDate;
}
