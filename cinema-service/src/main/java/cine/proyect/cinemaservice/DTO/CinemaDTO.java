package cine.proyect.cinemaservice.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CinemaDTO {

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String cine;

    @NotBlank(message = "La direccion no puede estar vacia")
    private String direccion;

    @NotNull(message = "Debes seleccionar una comuna válida")
    private Long comunaId;

}
