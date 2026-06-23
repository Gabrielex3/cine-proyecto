package cine.proyect.ticketservice.Service;

import cine.proyect.ticketservice.Client.BookingClient;
import cine.proyect.ticketservice.Client.PaymentClient;
import cine.proyect.ticketservice.DTO.PaymentRequestDTO;
import cine.proyect.ticketservice.DTO.TicketDTO;
import cine.proyect.ticketservice.DTO.bookingDTO;
import cine.proyect.ticketservice.Model.Ticket;
import cine.proyect.ticketservice.Repository.TicketRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private BookingClient bookingClient;

    @Autowired
    private PaymentClient paymentClient;

    public Ticket generateTicket(TicketDTO dto) {
        log.info("Iniciando generación de ticket para Reserva ID: {}", dto.getBookingId());

        if (ticketRepository.existsByBookingId(dto.getBookingId())) {
            throw new RuntimeException("El ticket para la reserva " + dto.getBookingId() + " ya existe.");
        }
        bookingDTO reserva;
        try {
            reserva = bookingClient.buscarReservaPorId(dto.getBookingId());
            log.info("Reserva {} encontrada exitosamente.", reserva.getId());
        } catch (Exception e) {
            log.error("Fallo al validar reserva con Booking-Service: {}", e.getMessage());
            throw new RuntimeException("La reserva no existe o el servicio no responde.");
        }
        PaymentRequestDTO pago;
        try {
             pago = paymentClient.getPaymentByBookingId(dto.getBookingId());
            log.info("Payment {} encontrado exitosamente.", pago.getId());
        } catch (Exception e) {
            log.error("Fallo al buscar el pago con Payment-Service: {}", e.getMessage());
            throw new RuntimeException("No se encontró registro de pago para esta reserva.");
        }
        if (!"APPROVED".equalsIgnoreCase(pago.getEstado())) {
            log.error("Intento de emitir ticket con pago en estado: {}", pago.getEstado());
            throw new RuntimeException("No se puede generar el ticket. El estado del pago es: " + pago.getEstado());
        }

        Ticket ticket = new Ticket();
        ticket.setBookingId(dto.getBookingId());
        ticket.setPaymentId(dto.getPaymentId());
        ticket.setIssueDate(LocalDateTime.now());

        Ticket guardado = ticketRepository.save(ticket);
        log.info("Ticket generado exitosamente con ID: {}", guardado.getId());
        return guardado;
    }


    public TicketDTO getTicketById(Long id) {
        log.info("Buscando ticket con ID: {}", id);
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket no encontrado con ID: " + id));
        return mapToDTO(ticket);
    }

    private TicketDTO mapToDTO(Ticket ticket) {
        TicketDTO dto = new TicketDTO();
        dto.setId(ticket.getId());
        dto.setBookingId(ticket.getBookingId());
        dto.setPaymentId(ticket.getPaymentId());
        dto.setIssueDate(ticket.getIssueDate());
        return dto;
    }
}