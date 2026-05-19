package cine.proyect.ticketservice.Repository;

import cine.proyect.ticketservice.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket,Long> {
    boolean existsByBookingId(Long bookingId);
}
