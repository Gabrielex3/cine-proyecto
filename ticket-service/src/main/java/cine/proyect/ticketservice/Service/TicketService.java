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

        Ticket ticket = new Ticket();
        ticket.setBookingId(dto.getBookingId());
        ticket.setPaymentId(dto.getPaymentId());
        ticket.setIssueDate(LocalDateTime.now());

        return ticketRepository.save(ticket);
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