package cine.proyect.bookingservice.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public class showtimeDTO {
    private Long movieId;
    private Long roomId;
    private LocalDateTime fechaHora;
    private Double precioTicket;
}
