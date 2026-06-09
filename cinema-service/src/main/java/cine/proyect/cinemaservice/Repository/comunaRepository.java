package cine.proyect.cinemaservice.Repository;

import cine.proyect.cinemaservice.Model.comunas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface comunaRepository extends JpaRepository<comunas, Long> {

}
