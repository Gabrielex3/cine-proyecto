package cine.proyect.cinemaservice.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CinemaDTO {

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String cine;

    @NotBlank(message = "La direccion no puede estar vacia")
    private String direccion;

    @NotBlank(message = "La ciudad es obligatoria")
    private String ciudad;

}
