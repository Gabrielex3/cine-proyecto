package cine.proyect.seatservice.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Table;
import lombok.Data;
import jakarta.persistence.*;

@Data
@Entity
@Table(name = "asientos")
public class Asiento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_fila", nullable = false, length = 10)
    private String numeroFila;

    @Column(name = "disponible", nullable = false)
    private boolean disponible;

    @Column(name = "sala_id", nullable = false)
    private Long salaId;

}
