package cine.proyect.ticketservice.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDTO {

    private Long id;

    @NotNull(message = "El ID de la reserva es obligatorio")
    private Long bookingId;

    @NotNull(message = "El ID del pago es obligatorio")
    private Long paymentId;

    private LocalDateTime issueDate;
}
