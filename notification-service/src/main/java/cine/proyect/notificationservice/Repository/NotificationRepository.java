package cine.proyect.notificationservice.Repository;

import cine.proyect.notificationservice.Model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findByIdUsuario(Long idUsuario);
    List<Notification> findByEstado(String estado);
}
