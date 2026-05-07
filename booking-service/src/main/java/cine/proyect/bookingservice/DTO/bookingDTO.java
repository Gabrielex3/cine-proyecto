package cine.proyect.bookingservice.DTO;

import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class bookingDTO {

    @NotNull(message = "El ID de usuario es obligatorio")
    @Positive(message = "El ID de usuario debe ser un número positivo")
    private Long userId;

    @NotNull(message = "El ID de la función es obligatorio")
    @Positive(message = "El ID de la función debe ser un número positivo")
    private Long showtimeId;

    @NotNull(message = "El ID del asiento es obligatorio")
    @Positive(message = "El ID del asiento debe ser un número positivo")
    private Long seatId;

    @NotNull(message = "El estado de la reserva es obligatorio")
    private String status;
}
