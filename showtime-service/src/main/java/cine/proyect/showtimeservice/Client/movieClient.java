package cine.proyect.showtimeservice.Client;

import cine.proyect.showtimeservice.dto.movieDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "movie-service")
public interface movieClient {
    @GetMapping("/api/v2/cine/movie/{id}")
    movieDTO obtenerPeliculaPorId(@PathVariable("id") Long id);
}
