package cine.proyect.paymentservice.service;

import cine.proyect.paymentservice.client.BookingClient;
import cine.proyect.paymentservice.client.TicketClient;
import cine.proyect.paymentservice.client.notificationClient;
import cine.proyect.paymentservice.client.showtimeClient;
import cine.proyect.paymentservice.dto.*;
import cine.proyect.paymentservice.model.*;
import cine.proyect.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final BookingClient bookingClient;
    @Autowired
    private showtimeClient showtimeClient;
    @Autowired
    private TicketClient ticketClient;

    @Autowired
    notificationClient notificationClient;

    public Payment procesarPago(PaymentRequestDTO dto) {
        log.info("Iniciando proceso de pago para Reserva ID: {}", dto.getReservaId());

        bookingDTO reserva = bookingClient.buscarReservaPorId(dto.getReservaId());
        if (reserva == null) {
            throw new RuntimeException("Error: La reserva no existe.");
        }
        if (!"CREATED".equalsIgnoreCase(reserva.getStatus())) {
            throw new RuntimeException("La reserva no está en estado CREATED");
        }
        showtimeDTO showtime = showtimeClient.obtenerPorIdShowtime(reserva.getShowtimeId());
        if (showtime == null) {
            throw new RuntimeException("Error: La función no existe.");
        }

        Payment payment = new Payment();
        payment.setReservaId(dto.getReservaId());
        payment.setMonto(dto.getMonto());
        payment.setTimestamp(LocalDateTime.now());
        boolean esExitoso = dto.getMonto().compareTo(showtime.getPrecioTicket()) >= 0;

        if (esExitoso) {
            payment.setEstado(PaymentStatus.APPROVED);
            payment = paymentRepository.save(payment);
            log.info("Pago registrado internamente y confirmado en BD con ID: {}", payment.getId());

            try {
                reserva.setStatus("CONFIRMED");
                bookingClient.actualizarStatus(reserva.getId(), reserva);

                TicketDTO solicitudTicket = new TicketDTO();
                solicitudTicket.setBookingId(reserva.getId());
                solicitudTicket.setPaymentId(payment.getId());

                TicketDTO ticketCreado = ticketClient.crearTicket(solicitudTicket);
                log.info("Ticket generado exitosamente con ID: {}", ticketCreado.getId());

                NotificationRequestDTO notifDto = new NotificationRequestDTO();
                notifDto.setIdTicket(ticketCreado.getId());
                notifDto.setIdUsuario(reserva.getUserId());
                notifDto.setMensaje("¡Pago aprobado! Tu entrada es la #" + ticketCreado.getId());
                notifDto.setTipo("TICKET_CONFIRMATION");

                notificationClient.enviarNotificacion(notifDto);
                log.info("Proceso de pago y notificación completado.");

            } catch (Exception e) {
                log.error("Fallo crítico en el flujo post-pago: {}", e.getMessage());
                throw new RuntimeException("Fallo en la cadena de servicios: " + e.getMessage());
            }

        } else {
            payment.setEstado(PaymentStatus.REJECTED);
            payment = paymentRepository.save(payment);
            log.warn("Pago rechazado por monto insuficiente.");
        }

        return payment;
    }

    public Payment buscarPorId(Long id) {
        log.info("Iniciando proceso de busqueda para Payment ID: {}", id);
        return paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El pago con ID " + id + " no existe."));
    }

    public List<Payment> listarTodos() {
        return paymentRepository.findAll();
    }

    public void eliminarPago(Long id) {
        log.info("Eliminando registro de pago ID: {}", id);
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("El pago con ID " + id + " no existe.");
        }
        paymentRepository.deleteById(id);
    }
}