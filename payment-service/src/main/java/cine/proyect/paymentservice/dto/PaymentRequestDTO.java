package cine.proyect.paymentservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequestDTO {

    @NotNull(message = "El monto no puede ser nulo")
    @Min(value = 1, message = "El monto debe ser mayor a cero")
    private Double monto;

    @NotBlank(message = "La moneda es obligatoria (ej: CLP, USD)")
    private String moneda;

    @NotNull(message = "El ID de la reserva es obligatorio")
    private Long reservaId;
}