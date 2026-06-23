package cine.proyect.notificationservice.Service;

import cine.proyect.notificationservice.DTO.NotificationRequestDTO;
import cine.proyect.notificationservice.Model.Notification;
import cine.proyect.notificationservice.Model.notificationStatus;
import cine.proyect.notificationservice.Repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.management.RuntimeErrorException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {
    @Autowired
    private NotificationRepository notificationRepository;

    @Transactional
    public void enviarNotificacion(NotificationRequestDTO dto) {
        if (dto.getIdTicket() == null || dto.getIdUsuario() == null) {
            log.error("Fallo al guardar: ID de Ticket ({}) o Usuario ({}) es NULL",
                    dto.getIdTicket(), dto.getIdUsuario());
            return;
        }
        log.info("Guardando notificación para Ticket ID: {} y Usuario: {}",
                dto.getIdTicket(), dto.getIdUsuario());
        try {
            Notification notification = new Notification();
            notification.setIdTicket(dto.getIdTicket());
            notification.setIdUsuario(dto.getIdUsuario());
            notification.setMensaje(dto.getMensaje());
            notification.setTipo(dto.getTipo());
            notification.setFechaEnvio(LocalDateTime.now());
            notification.setEstado(notificationStatus.NO_VIEW);

            notificationRepository.save(notification);
            log.info("Notificación guardada exitosamente.");

        } catch (Exception e) {
            log.error("Error al persistir la notificación: {}", e.getMessage());
            throw new RuntimeException("Error interno en la base de datos de notificaciones");
        }
    }

    public List<Notification> obtenerTodas() {
        log.info("Iniciando consulta global de notificaciones.");
        List<Notification> notifications = notificationRepository.findAll();
        log.info("Consulta finalizada. Registros recuperados: {}", notifications.size());
        return notifications;
    }


    public Notification obtenerPorId(Long id) {
        log.info("Buscando notificacion por  ID: {}", id);
        return notificationRepository.findById(id).orElseThrow(() -> {
            log.error("Fallo de búsqueda: No existe la notificacion con ID {}", id);
            return new RuntimeException("Notificacion no encontrada con ID: " + id);
        });
    }

    public List<Notification> obtenerPorUsuario(Long idUsuario) {
        log.info("Buscando notificaciones para el usuario: {}", idUsuario);
        List<Notification> notificaciones = notificationRepository.findByIdUsuario(idUsuario);

        if (notificaciones.isEmpty()) {
            log.warn("No se encontraron notificaciones para el usuario: {}", idUsuario);
        }

        return notificaciones;
    }

    public Notification actualizarNotificacion(Long id, NotificationRequestDTO dto) {
        log.info("Actualizando notificacion por  ID: {}", id);
        Notification existente = obtenerPorId(id);

        existente.setIdUsuario(dto.getIdUsuario() != null ? dto.getIdUsuario() : existente.getIdUsuario());
        existente.setMensaje(dto.getMensaje() != null ? dto.getMensaje() : existente.getMensaje());
        existente.setTipo(dto.getTipo() != null ? dto.getTipo() : existente.getTipo());
        existente.setEstado(dto.getEstado() != null ? dto.getEstado() : existente.getEstado());

        return notificationRepository.save(existente);
    }

    public void eliminarNotificacion(Long id) {
        try {
            Notification notificacionExistente = obtenerPorId(id);
            notificationRepository.delete(notificacionExistente);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error interno al intentar eliminar la notificación");
        }
    }
}
