package cine.proyect.paymentservice;

import cine.proyect.paymentservice.client.BookingClient;
import cine.proyect.paymentservice.client.TicketClient;
import cine.proyect.paymentservice.client.notificationClient;
import cine.proyect.paymentservice.client.showtimeClient;
import cine.proyect.paymentservice.dto.*;
import cine.proyect.paymentservice.model.*;
import cine.proyect.paymentservice.repository.PaymentRepository;
import cine.proyect.paymentservice.service.PaymentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("local")
public class paymentServiceTest {

    @Autowired
    private PaymentService service;

    @MockitoBean
    private PaymentRepository repo;

    @MockitoBean
    private BookingClient bookingClient;

    @MockitoBean
    private showtimeClient showtimeClient;

    @MockitoBean
    private TicketClient ticketClient;

    @MockitoBean
    private notificationClient notificationClient;

    @Test
    public void testProcesarPago() {
        PaymentRequestDTO dto = new PaymentRequestDTO(10L, new BigDecimal("5000"), PaymentStatus.CREATED);
        bookingDTO reserva = new bookingDTO(1L, 10L, 1L, 20L, "CREATED", 1L);
        showtimeDTO funcion = new showtimeDTO(1L, 1L, LocalDateTime.now(), new BigDecimal("5000"));
        Payment pagoGuardado = new Payment(1L, new BigDecimal("5000"), PaymentStatus.APPROVED, 10L, LocalDateTime.now());
        TicketDTO ticket = new TicketDTO(1L, 1L, 1L, LocalDateTime.now());

        when(bookingClient.buscarReservaPorId(10L)).thenReturn(reserva);
        when(showtimeClient.obtenerPorIdShowtime(1L)).thenReturn(funcion);
        when(repo.save(any(Payment.class))).thenReturn(pagoGuardado);

        when(bookingClient.actualizarStatus(anyLong(), any(bookingDTO.class))).thenReturn(null);
        when(ticketClient.crearTicket(any(TicketDTO.class))).thenReturn(ticket);
        doNothing().when(notificationClient).enviarNotificacion(any(NotificationRequestDTO.class));

        Payment resultado = service.procesarPago(dto);

        assertNotNull(resultado);
        assertEquals(PaymentStatus.APPROVED, resultado.getEstado(), "El pago debe ser aprobado");

        verify(bookingClient, times(1)).actualizarStatus(anyLong(), any());
        verify(ticketClient, times(1)).crearTicket(any());
        verify(notificationClient, times(1)).enviarNotificacion(any());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Payment pago = new Payment(id,new BigDecimal("5000"), PaymentStatus.APPROVED, 10L, LocalDateTime.now());

        when(repo.findById(id)).thenReturn(Optional.of(pago));

        Payment found = service.buscarPorId(id);

        assertNotNull(found);
        assertEquals(id, found.getId(), "El ID del pago devuelto no coincide");
    }

    @Test
    public void testDeleteById() {
        Long id = 1L;

        when(repo.existsById(id)).thenReturn(true);

        doNothing().when(repo).deleteById(id);

        service.eliminarPago(id);

        verify(repo, times(1)).deleteById(id);
    }
}