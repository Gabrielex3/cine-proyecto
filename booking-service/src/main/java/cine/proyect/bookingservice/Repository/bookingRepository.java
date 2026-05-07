package cine.proyect.bookingservice.Repository;

import cine.proyect.bookingservice.Model.booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface bookingRepository extends JpaRepository<booking, Long> {
}
