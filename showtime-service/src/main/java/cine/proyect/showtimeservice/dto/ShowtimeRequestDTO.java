package cine.proyect.showtimeservice.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

@Data
public class ShowtimeRequestDTO {
    @NotNull(message = "El ID de la pelicula es obligatorio")
    private Long movieId;

    @NotNull(message = "El ID de la sala es obligatorio")
    private Long roomId;

    @NotNull(message = "La fecha y hora son obligatorias")
    private LocalDateTime fechaHora;

    @Positive(message = "El precio debe ser un valor positivo")
    private Double precioTicket;
}
