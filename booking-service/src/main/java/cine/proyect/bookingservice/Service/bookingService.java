package cine.proyect.bookingservice.Service;

import cine.proyect.bookingservice.Client.showtimeClient;
import cine.proyect.bookingservice.DTO.bookingDTO;
import cine.proyect.bookingservice.DTO.showtimeDTO;
import cine.proyect.bookingservice.DTO.userDTO;
import cine.proyect.bookingservice.Model.booking;
import cine.proyect.bookingservice.Model.bookingStatus;
import cine.proyect.bookingservice.Repository.bookingRepository;
import cine.proyect.bookingservice.Client.userClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class bookingService {

    @Autowired
    private bookingRepository repo;

    private userClient userClient;
    private showtimeClient showtimeClient;


    public List<booking> findAllBookings() {
        log.info("Buscar todas las reservas: Iniciando consulta.");
        List<booking> bookingsList = repo.findAll();
        log.info("Buscar todas las reservas: consulta finalizada. Registros recuperados: {}", bookingsList.size());
        return bookingsList;
    }

    public booking findBookingById(Long id) {
        log.info("Buscando reserva por id: Buscando reserva con ID: {}", id);
        return repo.findById(id).orElseThrow(() -> {
            log.error("Buscando reserva por id: Fallo de búsqueda, no existe la reserva con ID {}", id);
            return new RuntimeException("Buscando reserva por id: reserva no encontrada con ID: " + id);
        });
    }


    public booking createBooking(bookingDTO dto) {
        try {
            log.info("Creacion booking: Iniciando creación de reserva para usuario ID: {}", dto.getUserId());
            userDTO user = userClient.obtenerPorIdUser(dto.getUserId());

            if (user == null) {
                log.error("Creacion booking: Usuario no encontrado con ID: {}", dto.getUserId());
                throw new RuntimeException("Creacion booking: No existe un usuario registrado con el ID: " + dto.getUserId());
            }

            booking booking = new booking();
            booking.setUserId(dto.getUserId());
            booking.setSeatId(dto.getSeatId());
            booking.setStatus(bookingStatus.CREATED);
            log.info("Creacion booking: Usuario verificado con exito User id: {}", dto.getUserId());

            showtimeDTO showtime = showtimeClient.obtenerPorIdShowtime(dto.getShowtimeId());
            if (showtime == null) {
                log.error("Creacion booking: funciones no encontradas con ID: {}", dto.getShowtimeId());
                throw new RuntimeException("Creacion booking: No existe una funcion registrada con el ID: " + dto.getShowtimeId());
            }
            booking.setShowtimeId(dto.getShowtimeId());



            log.info("Creacion booking: Reserva creada exitosamente para el usuario: {}", dto.getUserId());
            return repo.save(booking);

        } catch (Exception e) {
            log.error("Creacion booking: error crítico al crear reserva: {}", e.getMessage());
            throw new RuntimeException("Creacion booking: error crítico al crear la reserva: " + e.getMessage());
        }
    }

    public void deleteBooking(Long id) {
        log.info("Eliminar booking: Iniciando proceso de eliminación de reserva para el ID: {}", id);

        if (!repo.existsById(id)) {
            log.error("Eliminar booking: Fallo al eliminar reserva, El ID {} no existe", id);
            throw new RuntimeException("Eliminar booking: No se puede eliminar, Reserva con ID " + id + " no existe");
        }

        try {
            repo.deleteById(id);
            log.info("Eliminar booking: Reserva con ID {} eliminado exitosamente", id);
        } catch (Exception e) {
            log.error("Eliminar booking: Error al eliminar reserva, usuario ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Eliminar booking: Error al intentar borrar la reserva con ID: "+id);
        }
    }


}
