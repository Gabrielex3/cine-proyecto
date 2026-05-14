package cine.proyect.showtimeservice.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class movieDTO {
    private String titulo;
    private String genero;
    private Integer duracion;
}
