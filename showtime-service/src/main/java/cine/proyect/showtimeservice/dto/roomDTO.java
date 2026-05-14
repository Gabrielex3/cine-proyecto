package cine.proyect.showtimeservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class roomDTO {
    private String nombre;
    private String tipo;
    private Boolean activo;
}
