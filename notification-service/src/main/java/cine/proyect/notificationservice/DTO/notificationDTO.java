package cine.proyect.notificationservice.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class notificationDTO {

    @NotBlank(message = "El destinatario es obligatorio")
    @Email(message = "El formato del correo no es válido")
    private String receptor;

    @NotBlank(message = "El asunto es obligatorio")
    private String asunto;

    @NotBlank(message = "El mensaje no puede estar vacío")
    private String message;
}
