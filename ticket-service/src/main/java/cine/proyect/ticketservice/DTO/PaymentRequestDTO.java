package cine.proyect.ticketservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequestDTO {

    private Long id;
    private Long reservaId;
    private BigDecimal monto;
    private String estado;


}