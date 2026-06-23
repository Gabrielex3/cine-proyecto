package cine.proyect.seatservice.Repository;

import cine.proyect.seatservice.Model.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AsientoRepository  extends JpaRepository<Asiento, Long> {
    List<Asiento> findBySalaId (Long salaId);
}
