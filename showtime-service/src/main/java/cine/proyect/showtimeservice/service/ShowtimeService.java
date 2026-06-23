package cine.proyect.showtimeservice.service;

import cine.proyect.showtimeservice.Client.movieClient;
import cine.proyect.showtimeservice.Client.roomClient;
import cine.proyect.showtimeservice.dto.ShowtimeRequestDTO;
import cine.proyect.showtimeservice.dto.movieDTO;
import cine.proyect.showtimeservice.dto.roomDTO;
import cine.proyect.showtimeservice.model.Showtime;
import cine.proyect.showtimeservice.repository.ShowtimeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShowtimeService {
    @Autowired
    private ShowtimeRepository showtimeRepository;
    @Autowired
    private movieClient movieClient;
    @Autowired
    private roomClient roomClient;

    @Transactional
    public Showtime createShowtime(ShowtimeRequestDTO dto) {
        log.info("Crear funcion: iniciando validaciones...");

        movieDTO pelicula = movieClient.obtenerPeliculaPorId(dto.getMovieId());
        if (pelicula == null) {
            throw new RuntimeException("La película con ID " + dto.getMovieId() + " no existe.");
        }

        roomDTO sala = roomClient.obtenerSalaPorId(dto.getRoomId());
        if (sala == null) {
            throw new RuntimeException("La sala con ID " + dto.getRoomId() + " no existe.");
        }

        Showtime showtime = new Showtime();
        showtime.setMovieId(dto.getMovieId());
        showtime.setRoomId(dto.getRoomId());
        showtime.setPrecioTicket(dto.getPrecioTicket());
        showtime.setFechaHora(dto.getFechaFuncion());

        return showtimeRepository.save(showtime);
    }

    public List<Showtime> findAll() {
        log.info("Buscar all funciones: Iniciando consulta global de Funciones.");
        List<Showtime> funcionesList = showtimeRepository.findAll();
        log.info("Buscar all funciones: consulta finalizada. Funciones recuperadas: {}", funcionesList.size());
        return funcionesList;
    }

    public void deleteShowtime(Long id) {
        log.info("Iniciando proceso de eliminación para la funcion con ID: {}", id);

        if (!showtimeRepository.existsById(id)) {
            log.error("Fallo al eliminar: El ID {} no existe", id);
            throw new RuntimeException("No se puede eliminar: Funcion con ID " + id + " no existe");
        }

        try {
            showtimeRepository.deleteById(id);
            log.info("Usuario con ID {} eliminado exitosamente", id);
        } catch (Exception e) {
            log.error("Error al eliminar el usuario ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al intentar borrar el registro de la base de datos.");
        }
    }

    public Showtime buscarPorId(Long id) {
        log.info("Iniciando proceso de busqueda de funcion con ID: {}", id);
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La función con ID " + id + " no existe."));
    }

    public Showtime actualizar(Long id, ShowtimeRequestDTO dto){
        Showtime existente = buscarPorId(id);

        existente.setMovieId(dto.getMovieId());
        existente.setRoomId(dto.getRoomId());
        existente.setFechaHora(LocalDateTime.now());
        existente.setPrecioTicket(dto.getPrecioTicket());

        log.info("Actualizando funcion ID: {}", id);
        return showtimeRepository.save(existente);
    }
}
