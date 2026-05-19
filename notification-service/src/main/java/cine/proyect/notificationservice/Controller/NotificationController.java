package cine.proyect.notificationservice.Controller;

import cine.proyect.notificationservice.DTO.NotificationRequestDTO;
import cine.proyect.notificationservice.Model.Notification;
import cine.proyect.notificationservice.Service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cine/notification")
@Slf4j
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @PostMapping("/send")
    public ResponseEntity<String> enviar(@RequestBody NotificationRequestDTO dto) { // <--- ¡Asegúrate de tener @RequestBody!
        notificationService.enviarNotificacion(dto);
        return ResponseEntity.ok("Notificación enviada");
    }

    @GetMapping("/historial")
    public ResponseEntity<List<Notification>>verHistorial(){
        return ResponseEntity.ok(notificationService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notification> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.obtenerPorId(id));
    }


    @GetMapping("/user/{id}")
    public ResponseEntity<List<Notification>> buscarPorIdUser(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.obtenerPorUsuario(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Notification> actualizar(@PathVariable Long id, @RequestBody NotificationRequestDTO dto) {
        return ResponseEntity.ok(notificationService.actualizarNotificacion(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        notificationService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
