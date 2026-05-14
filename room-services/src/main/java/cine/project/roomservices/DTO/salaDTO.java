package cine.project.roomservices.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class salaDTO {
    @NotBlank(message = "El nombre de la sala no puede estar vacío")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "El tipo de sala es obligatorio (ej: 2D, 3D, IMAX)")
    @Size(max = 50, message = "El tipo de sala no puede superar los 50 caracteres")
    private String tipo;

    @NotNull(message = "El estado activo/inactivo es obligatorio")
    private Boolean activo;
}
