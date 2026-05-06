package cine.proyect.movieservice.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class MovieDTO {

    @NotBlank(message = "El titulo de la pelicula es obligatorio!")
    private String titulo;

    @NotBlank(message = "El genero de la pelicula es obligatorio!")
    private String genero;

    @NotNull(message = "La duracion es obligatoria!")
    @Min(value = 1, message = "La duracion debe ser mayor a 0 minutos")
    private Integer duracion;
}
