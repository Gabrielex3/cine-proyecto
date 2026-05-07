package cine.project.roomservices.repository;

import cine.project.roomservices.model.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;


@Service
public interface SalaRepository extends JpaRepository<Sala, Long> {
}
