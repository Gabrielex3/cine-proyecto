package cine.proyect.notificationservice.Controller;

import cine.proyect.notificationservice.Service.notificationService;
import cine.proyect.notificationservice.DTO.notificationDTO;
import cine.proyect.notificationservice.Model.Notificacion;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/cine/notificaciones")
public class notificationController {

    @Autowired
    private notificationService service;

    // 1. POST: Disparar una nueva notificación
    @PostMapping
    public ResponseEntity<Notificacion> sendNotification(@Valid @RequestBody notificationDTO notification) {
        log.info("REST request: Solicitud para enviar notificación a: {}", notification.getReceptor());
        Notificacion procesoNotificacion = service.procesoNotificacion(notification);
        return ResponseEntity.status(HttpStatus.CREATED).body(procesoNotificacion);
    }


    @GetMapping
    public ResponseEntity<List<Notificacion>> getAllNotifications() {
        log.info("REST request: Solicitando historial de notificaciones");
        return ResponseEntity.ok(service.getAllNotifications());
    }
}
