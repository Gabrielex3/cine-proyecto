package cine.proyect.cinemaservice.Repository;

import cine.proyect.cinemaservice.Model.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepositoryCinema extends JpaRepository<Cinema, Long> {
}
