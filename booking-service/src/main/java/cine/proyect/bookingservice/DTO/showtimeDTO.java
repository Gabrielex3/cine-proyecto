package cine.proyect.bookingservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class showtimeDTO {
    private Long movieId;
    private Long roomId;
    private LocalDateTime fechaHora;
    private Double precioTicket;
}
