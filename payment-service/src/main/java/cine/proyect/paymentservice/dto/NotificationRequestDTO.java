package cine.proyect.paymentservice.dto;

import lombok.Data;

@Data
public class NotificationRequestDTO {

    private Long idTicket;

    private Long idUsuario;

    private String mensaje;

    private String tipo;
}
