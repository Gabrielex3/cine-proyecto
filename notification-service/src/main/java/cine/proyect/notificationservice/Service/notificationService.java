package cine.proyect.notificationservice.Service;

import cine.proyect.notificationservice.DTO.notificationDTO;
import cine.proyect.notificationservice.Model.Notificacion;
import cine.proyect.notificationservice.Repository.notificationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class notificationService {

    @Autowired
    private notificationRepository repository;


    public Notificacion procesoNotificacion(notificationDTO notificaciondto) {
        log.info("Iniciando procesamiento de notificación para: {}", notificaciondto.getReceptor());

        Notificacion notification = new Notificacion();
        notification.setReceptor(notificaciondto.getReceptor());
        notification.setAsunto(notificaciondto.getAsunto());
        notification.setMessage(notificaciondto.getMessage());
        notification.setSentAt(LocalDateTime.now());

        try {
            log.info("Simulando envío de correo a {} con asunto '{}'", notificaciondto.getReceptor(), notificaciondto.getAsunto());
            notification.setStatus("ENVIADO");

            Notificacion savedNotification = repository.save(notification);
            log.info("Notificación registrada en BD con ID: {}", savedNotification.getId());
            return savedNotification;

        } catch (Exception e) {
            log.error("Error al enviar el correo a {}: {}", notificaciondto.getReceptor(), e.getMessage());
            notification.setStatus("FALLIDO");
            repository.save(notification);

            throw new RuntimeException("Error interno al procesar la notificación");
        }
    }


    public List<Notificacion> getAllNotifications() {
        log.info("Consultando el historial de notificaciones enviadas");
        return repository.findAll();
    }
}
