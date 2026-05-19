package cine.proyect.paymentservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class showtimeDTO {
    private Long movieId;
    private Long roomId;
    private LocalDateTime fechaHora;
    private BigDecimal precioTicket;
}
