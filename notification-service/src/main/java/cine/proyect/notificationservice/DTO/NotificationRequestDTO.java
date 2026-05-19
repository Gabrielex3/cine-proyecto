package cine.proyect.notificationservice.DTO;

import cine.proyect.notificationservice.Model.notificationStatus;
import lombok.Data;

@Data
public class NotificationRequestDTO {

    private Long idTicket;

    private Long idUsuario;

    private String mensaje;

    private String tipo;

    private notificationStatus estado;
}
