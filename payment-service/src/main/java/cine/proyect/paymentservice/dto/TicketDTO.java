package cine.proyect.paymentservice.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDTO {

    private Long id;

    private Long bookingId;

    private Long paymentId;
    private LocalDateTime issueDate;
}
