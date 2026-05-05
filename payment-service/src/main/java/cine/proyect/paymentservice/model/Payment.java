package cine.proyect.paymentservice.model;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Double monto;
    private String moneda;

    @Enumerated(EnumType.STRING)
    private PaymentStatus estado;

    private Long reservaId;
    private LocalDateTime timestamp;
}