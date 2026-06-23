package cine.proyect.notificationservice.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name= "notificaciones")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_ticket", nullable = false)
    private Long idTicket;

    @Column(name = "USUARIOS", nullable = false)
    private Long idUsuario;

    @Column(nullable = false, name = "MENSAJE")
    private String mensaje;

    @Column(nullable = false, name = "TIPO_MENSAJE")
    private String tipo;

    @Column(nullable = false)
    private LocalDateTime fechaEnvio;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "STATUS")
    private notificationStatus estado;


}
