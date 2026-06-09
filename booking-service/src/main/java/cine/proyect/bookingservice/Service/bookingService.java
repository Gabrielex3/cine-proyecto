package cine.proyect.bookingservice.Service;

import cine.proyect.bookingservice.Client.cinemaClient;
import cine.proyect.bookingservice.Client.seatClient;
import cine.proyect.bookingservice.Client.showtimeClient;
import cine.proyect.bookingservice.DTO.*;
import cine.proyect.bookingservice.Model.booking;
import cine.proyect.bookingservice.Model.bookingStatus;
import cine.proyect.bookingservice.Repository.bookingRepository;
import cine.proyect.bookingservice.Client.userClient;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class bookingService {

    @Autowired
    private bookingRepository repo;

    @Autowired
    private userClient userClient;
    @Autowired
    private showtimeClient showtimeClient;
    @Autowired
    private seatClient seatClient;

    @Autowired
    private cinemaClient cinemaClient;


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

    @Transactional
    public booking createBooking(bookingDTO dto) {
        log.info("Creacion booking: Iniciando reserva para Usuario: {}, Asiento: {}, Función: {}",
                dto.getUserId(), dto.getSeatId(), dto.getShowtimeId());

        try {
            userDTO user = userClient.obtenerPorIdUser(dto.getUserId());
            if (user == null) throw new RuntimeException("Creacion booking: No existe ese usuario con id: " + dto.getUserId());
        } catch (Exception e) {
            log.error("Creacion booking: Usuario {} no encontrado", dto.getUserId());
            throw new RuntimeException("Creacion booking: El usuario con ID " + dto.getUserId() + " No encontrado.");
        }

        try {
            showtimeDTO showtime = showtimeClient.obtenerPorIdShowtime(dto.getShowtimeId());
            if (showtime == null) throw new RuntimeException("Creacion booking: No existe funcion con ese id: "+dto.getShowtimeId());
        } catch (Exception e) {
            log.error("Creacion booking: Función {} no encontrada", dto.getShowtimeId());
            throw new RuntimeException("Creacion booking: La función con ID " + dto.getShowtimeId() + " No encontrada.");
        }
        try {
            seatDTO asiento = seatClient.obtenerAsientoPorId(dto.getSeatId());
            if (asiento == null) throw new RuntimeException("Creacion booking: No existe asiento con ese id: : "+dto.getSeatId());
        } catch (Exception e) {
            log.error("Creacion booking: Asiento {} no encontrado", dto.getSeatId());
            throw new RuntimeException("Creacion booking: El asiento con ID " + dto.getSeatId() + " No encontrada.");
        }try {
            cinemaDTO cinema = cinemaClient.obtenerCinemaPorId(dto.getCinema());
            if (cinema == null) throw new RuntimeException("Creacion booking: No existe cinema con ese id: : "+dto.getSeatId());
        } catch (Exception e) {
            throw new RuntimeException("Creacion booking: El cinema con ID " + dto.getCinema() + " No encontrada.");
        }
        try {
            booking booking = new booking();
            booking.setUserId(dto.getUserId());
            booking.setSeatId(dto.getSeatId());
            booking.setShowtimeId(dto.getShowtimeId());
            booking.setStatus(bookingStatus.CREATED);
            booking.setCinema(dto.getCinema());

            log.info("Creacion booking: Guardando reserva en DB...");
            return repo.save(booking);

        } catch (Exception e) {
            log.error("Creacion booking: Error al guardar en DB: {}", e.getMessage());
            throw new RuntimeException("Creacion booking: Error interno al procesar la reserva.");
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


    @Transactional
    public booking updateBooking(Long id, bookingDTO dto) {
        log.info("Actualizar booking: Iniciando edición de reserva ID: {}", id);
        booking reservaExistente = repo.findById(id).orElseThrow(() -> {
                    log.error("Actualizar booking: Reserva {} no encontrada", id);
                    return new RuntimeException("Actualizar booking: La reserva con ID " + id + " No encontrada.");});

        if (dto.getSeatId() != null) {
            try {
                seatDTO asiento = seatClient.obtenerAsientoPorId(dto.getSeatId());
                if (asiento == null) throw new RuntimeException();
                reservaExistente.setSeatId(dto.getSeatId());
            } catch (Exception e) {
                log.error("Actualizar booking: Error al validar asiento {}", dto.getSeatId());
                throw new RuntimeException("Actualizar booking: El asiento con ID " + dto.getSeatId() + " No encontrada.");
            }
        }

        if (dto.getShowtimeId() != null) {
            try {
                showtimeDTO funcion = showtimeClient.obtenerPorIdShowtime(dto.getShowtimeId());
                if (funcion == null) throw new RuntimeException();
                reservaExistente.setShowtimeId(dto.getShowtimeId());
            } catch (Exception e) {
                log.error("Actualizar booking: Error al validar funcion {}", dto.getShowtimeId());
                throw new RuntimeException("Actualizar booking: La funcion con ID " + dto.getShowtimeId() + " No encontrada.");
            }
        }
        try {
            if (dto.getStatus() != null) {
                reservaExistente.setStatus(dto.getStatus());
            }

            log.info("Actualizar booking: Guardando cambios para reserva ID: {}", id);
            return repo.save(reservaExistente);
        } catch (Exception e) {
            log.error("Actualizar booking: Error al guardar en DB: {}", e.getMessage());
            throw new RuntimeException("Actualizar booking: Error interno al procesar la actualización.");
        }
    }

    @Transactional
    public booking actualizarEstado(Long id, bookingDTO dto) {
        log.info("Actualizar estado: Iniciando actualización para booking ID {}", id);
        booking reserva = repo.findById(id).orElseThrow(() -> {
            log.error("Actualizar estado: Reserva {} no encontrada", id);
            return new RuntimeException("Reserva no encontrada");
        });

        reserva.setStatus(dto.getStatus());

        log.info("Actualizar estado: Estado actualizado correctamente para booking {}", id);

        return repo.save(reserva);
    }

}
