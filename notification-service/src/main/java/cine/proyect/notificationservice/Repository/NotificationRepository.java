package cine.proyect.notificationservice.Repository;

import cine.proyect.notificationservice.Model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface notificationRepository extends JpaRepository<Notificacion, Long> {

}
