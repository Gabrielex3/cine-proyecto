package cine.proyect.paymentservice.model;

import lombok.Data;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "pagos")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false)
    private PaymentStatus estado;

    @Column(name = "reserva_id", nullable = false,unique = true)
    private Long reservaId;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;
}