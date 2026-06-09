package cine.proyect.paymentservice.dto;

import cine.proyect.paymentservice.model.PaymentStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class bookingDTO {
    private Long id;

    private Long userId;

    private Long showtimeId;

    private Long seatId;

    private String status;

    private Long cinema;
}
