package cine.proyect.seatservice.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsientoRequestDTO {

    @NotBlank(message = "El número o fila no puede estar vacío")
    private String numeroFila;

    @NotNull(message = "El estado de disponibilidad es obligatorio")
    private Boolean disponible;

    @NotNull(message = "El ID de la sala es obligatorio")
    private Long salaId;
}