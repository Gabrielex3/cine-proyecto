package cine.proyect.ticketservice;

import cine.proyect.ticketservice.Client.BookingClient;
import cine.proyect.ticketservice.Client.PaymentClient;
import cine.proyect.ticketservice.DTO.PaymentRequestDTO;
import cine.proyect.ticketservice.DTO.TicketDTO;
import cine.proyect.ticketservice.DTO.bookingDTO;
import cine.proyect.ticketservice.Model.Ticket;
import cine.proyect.ticketservice.Repository.TicketRepository;
import cine.proyect.ticketservice.Service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
public class ticketServiceTest {

    @Autowired
    private TicketService service;

    @MockitoBean
    private TicketRepository repo;
    @MockitoBean
    private BookingClient bookingClient;
    @MockitoBean
    private PaymentClient paymentClient;

    @Test
    public void createTicket() {
        TicketDTO dtoEntrada = new TicketDTO(1L,100L, 200L, LocalDateTime.now());

        bookingDTO reservaSimulada = new bookingDTO(100L, 1L, 1L, 1L, "CONFIRMED", 1L);

        PaymentRequestDTO pagoSimulado = new PaymentRequestDTO(1L,100L,new BigDecimal("5000"),"APPROVED");

        Ticket ticketGuardado = new Ticket(1L, 100L, 200L, LocalDateTime.now());

        when(repo.existsByBookingId(100L)).thenReturn(false);
        when(bookingClient.buscarReservaPorId(100L)).thenReturn(reservaSimulada);
        when(paymentClient.getPaymentByBookingId(100L)).thenReturn(pagoSimulado);
        when(repo.save(any(Ticket.class))).thenReturn(ticketGuardado);


        Ticket resultado = service.generateTicket(dtoEntrada);

        assertNotNull(resultado, "El ticket devuelto no debería ser nulo");
        assertEquals(1L, resultado.getId(), "El ID del ticket debe ser 1");
        assertEquals(100L, resultado.getBookingId(), "El ID de la reserva debe coincidir");
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        Ticket ticket = new Ticket(id, 100L, 200L, LocalDateTime.now());

        when(repo.findById(id)).thenReturn(Optional.of(ticket));

        TicketDTO found = service.getTicketById(id);
        
        assertNotNull(found);
        assertEquals(id, found.getId(), "El ID del ticket debe ser 1");
    }

}