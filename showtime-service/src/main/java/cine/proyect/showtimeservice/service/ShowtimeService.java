package cine.proyect.showtimeservice.service;

import cine.proyect.showtimeservice.dto.ShowtimeRequestDTO;
import cine.proyect.showtimeservice.model.Showtime;
import cine.proyect.showtimeservice.repository.ShowtimeRepository; // <--- AGREGA ESTO
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShowtimeService {
    private final ShowtimeRepository showtimeRepository;

    public Showtime crearFuncion(ShowtimeRequestDTO dto){
        log.info("Creando nueva función para la película ID: {}", dto.getMovieId());

        Showtime showtime = new Showtime();
        showtime.setMovieId(dto.getMovieId());
        showtime.setRoomId(dto.getRoomId());
        showtime.setFechaHora(dto.getFechaHora());
        showtime.setPrecioTicket(dto.getPrecioTicket());

        return showtimeRepository.save(showtime);
    }
    public List<Showtime> listarTodas() {
        return showtimeRepository.findAll();
    }

    public void deleteUser(Long id) {
        log.info("Iniciando proceso de eliminación para el ID: {}", id);

        if (!showtimeRepository.existsById(id)) {
            log.error("Fallo al eliminar: El ID {} no existe", id);
            throw new RuntimeException("No se puede eliminar: Usuario con ID " + id + " no existe");
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
        return showtimeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La función con ID " + id + " no existe."));
    }

    public Showtime actualizar(Long id, ShowtimeRequestDTO dto){
        Showtime existente = buscarPorId(id);

        existente.setMovieId(dto.getMovieId());
        existente.setRoomId(dto.getRoomId());
        existente.setFechaHora(dto.getFechaHora());
        existente.setPrecioTicket(dto.getPrecioTicket());

        log.info("Actualizando funcion ID: {}", id);
        return showtimeRepository.save(existente);
    }
}
