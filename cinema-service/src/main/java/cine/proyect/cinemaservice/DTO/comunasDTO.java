package cine.proyect.cinemaservice.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class comunasDTO {

    private Long id;

    @NotBlank(message = "El nombre de la comuna es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de la comuna debe tener entre 3 y 50 caracteres")
    private String nombre;

}
