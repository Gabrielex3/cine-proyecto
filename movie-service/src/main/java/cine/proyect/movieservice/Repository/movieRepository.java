package cine.proyect.movieservice.Repository;

import cine.proyect.movieservice.Model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface movieRepository extends JpaRepository<Movie, Long>{
}
