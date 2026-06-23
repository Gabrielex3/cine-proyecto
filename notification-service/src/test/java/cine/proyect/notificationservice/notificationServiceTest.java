package cine.proyect.notificationservice;

import cine.proyect.notificationservice.DTO.NotificationRequestDTO;
import cine.proyect.notificationservice.Model.Notification;
import cine.proyect.notificationservice.Model.notificationStatus;
import cine.proyect.notificationservice.Repository.NotificationRepository;
import cine.proyect.notificationservice.Service.NotificationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static cine.proyect.notificationservice.Model.notificationStatus.NO_VIEW;
import static cine.proyect.notificationservice.Model.notificationStatus.SEEN;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("local")
public class notificationServiceTest {

    @Autowired
    private NotificationService service;

    @MockitoBean
    private NotificationRepository repo;

    @Test
    public void testEnviarNotificacion() {
        NotificationRequestDTO dto = new NotificationRequestDTO(1L, 5L, "Tu pago fue aprobado", "TICKET_CONFIRMATION", NO_VIEW);

        Notification guardada = new Notification(1L, 5L, 1L, "Tu pago fue aprobado", "TICKET_CONFIRMATION", LocalDateTime.now(), NO_VIEW);

        when(repo.save(any(Notification.class))).thenReturn(guardada);

        service.enviarNotificacion(dto);

        verify(repo, times(1)).save(any(Notification.class));
    }


    @Test
    public void testObtenerTodas() {
        Notification notification = new Notification(1L, 5L, 1L, "Tu pago fue aprobado", "TICKET_CONFIRMATION", LocalDateTime.now(), NO_VIEW);
        when(repo.findAll()).thenReturn(List.of(notification));
        List<Notification> resultados = service.obtenerTodas();
        assertFalse(resultados.isEmpty());
    }

    @Test
    public void testObtenerPorId() {
        Notification notification = new Notification(1L, 5L, 1L, "Tu pago fue aprobado", "TICKET_CONFIRMATION", LocalDateTime.now(), NO_VIEW);
        when(repo.findById(1L)).thenReturn(Optional.of(notification));

        Notification resultado = service.obtenerPorId(1L);
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
    }

    @Test
    public void testActualizarNotificacion() {
        Long id = 1L;

        NotificationRequestDTO notificationDto = new NotificationRequestDTO(5L,5L,"Mensaje Editado","TICKET_UPDATED",SEEN);

        Notification existente = new Notification(1L, 5L, 1L, "Tu pago fue aprobado", "TICKET_CONFIRMATION", LocalDateTime.now(), NO_VIEW);

        Notification actualizada = new Notification(1L, 5L, 5L, "Mensaje Editado", "TICKET_UPDATED", LocalDateTime.now(), SEEN);

        when(repo.findById(1L)).thenReturn(Optional.of(existente));
        when(repo.save(any(Notification.class))).thenReturn(actualizada);

        Notification resultado = service.actualizarNotificacion(id, notificationDto);

        assertEquals(5L, resultado.getIdUsuario(), "El ID de usuario no coincide");
        assertEquals("TICKET_UPDATED", resultado.getTipo(), "El tipo no debió borrarse");
        assertEquals("Mensaje Editado", resultado.getMensaje(), "El mensaje debió actualizarse");
    }
}