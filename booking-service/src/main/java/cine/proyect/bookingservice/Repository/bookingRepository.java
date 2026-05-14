package cine.proyect.bookingservice.Repository;

import cine.proyect.bookingservice.Model.booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface bookingRepository extends JpaRepository<booking, Long> {
}
