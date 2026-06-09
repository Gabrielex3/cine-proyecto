package cine.proyect.ticketservice.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestDTO {

    private Long reservaId;
    private BigDecimal monto;
    private String estado;


}