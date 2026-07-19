package cine.proyect.movieservice.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class MovieDTO {

    @NotBlank(message = "El titulo de la pelicula es obligatorio!")
    private String titulo;

    @NotBlank(message = "El genero de la pelicula es obligatorio!")
    private String genero;

    @NotNull(message = "La duracion es obligatoria!")
    @Min(value = 1, message = "La duracion debe ser mayor a 0 minutos")
    private Integer duracion;
    @NotNull(message = "La imagen es obligatoria!")
    private String imagen;
}
