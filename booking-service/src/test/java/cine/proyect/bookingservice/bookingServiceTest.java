package cine.proyect.bookingservice;

import cine.proyect.bookingservice.Client.cinemaClient;
import cine.proyect.bookingservice.Client.seatClient;
import cine.proyect.bookingservice.Client.showtimeClient;
import cine.proyect.bookingservice.Client.userClient;
import cine.proyect.bookingservice.DTO.*;
import cine.proyect.bookingservice.Model.booking;
import cine.proyect.bookingservice.Model.bookingStatus;
import cine.proyect.bookingservice.Repository.bookingRepository;
import cine.proyect.bookingservice.Service.bookingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
@ActiveProfiles("local")
public class bookingServiceTest {

    @MockitoBean
    private bookingRepository repo;
    @Autowired
    private bookingService service;
    @MockitoBean
    private userClient userClient;
    @MockitoBean
    private showtimeClient showtimeClient;
    @MockitoBean
    private seatClient seatClient;

    @MockitoBean
    private cinemaClient cinemaClient;


    @Test
    public void testGetAll() {
        booking reserva = new booking(1L,1L,3L,1L, bookingStatus.CREATED,1L);

        when(repo.findAll()).thenReturn(List.of(reserva));

        List<booking> reservas = service.findAllBookings();
        assertFalse(reservas.isEmpty());
        assertEquals(1, reservas.size());

    }

    @Test
    public void testFindById() {
        Long id = 1L;
        booking reserva = new booking(1L,1L,3L,1L, bookingStatus.CREATED,1L);

        when(repo.findById(id)).thenReturn(Optional.of(reserva));

        booking found = service.findBookingById(id);

        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    public void testCreateBooking() {
        bookingDTO dto = new bookingDTO(10L, 20L, 30L, bookingStatus.CREATED, 1L);

        userDTO user = new userDTO();
        showtimeDTO Showtime = new showtimeDTO();
        seatDTO Seat = new seatDTO();
        cinemaDTO Cinema = new cinemaDTO();

        booking reservaGuardada = new booking();
        reservaGuardada.setId(100L);
        reservaGuardada.setUserId(10L);
        reservaGuardada.setStatus(bookingStatus.CREATED);

        when(userClient.obtenerPorIdUser(10L)).thenReturn(user);
        when(showtimeClient.obtenerPorIdShowtime(20L)).thenReturn(Showtime);
        when(seatClient.obtenerAsientoPorId(30L)).thenReturn(Seat);
        when(cinemaClient.obtenerCinemaPorId(1L)).thenReturn(Cinema);

        when(repo.save(any(booking.class))).thenReturn(reservaGuardada);

        booking resultado = service.createBooking(dto);

        assertNotNull(resultado, "La reserva no debería ser nula");
        assertEquals(100L, resultado.getId(), "El ID de la reserva guardada debe ser 100");
        assertEquals(bookingStatus.CREATED, resultado.getStatus(), "El estado debe ser CREATED");

        verify(userClient, times(1)).obtenerPorIdUser(10L);
        verify(showtimeClient, times(1)).obtenerPorIdShowtime(20L);
        verify(seatClient, times(1)).obtenerAsientoPorId(30L);

        verify(cinemaClient, times(1)).obtenerCinemaPorId(1L);
        verify(repo, times(1)).save(any(booking.class));
    }


    @Test
    public void testDeleteById() {
        Long id = 1L;

        when(repo.existsById(id)).thenReturn(true);

        doNothing().when(repo).deleteById(id);

        service.deleteBooking(id);

        verify(repo, times(1)).deleteById(id);
    }

    @Test
    public void testUpdateBooking() {
        Long idBooking = 1L;
        bookingDTO dto = new bookingDTO();
        dto.setSeatId(99L);
        dto.setShowtimeId(88L);
        dto.setStatus(bookingStatus.CREATED);

        booking reservaExistente = new booking(idBooking, 100L, 10L, 30L, bookingStatus.CREATED, 1L);

        when(repo.findById(idBooking)).thenReturn(Optional.of(reservaExistente));
        when(seatClient.obtenerAsientoPorId(99L)).thenReturn(new seatDTO());
        when(showtimeClient.obtenerPorIdShowtime(88L)).thenReturn(new showtimeDTO());
        when(repo.save(any(booking.class))).thenReturn(reservaExistente);


        booking resultado = service.updateBooking(idBooking, dto);


        assertNotNull(resultado);
        assertEquals(99L, resultado.getSeatId());
        assertEquals(88L, resultado.getShowtimeId());
        verify(seatClient, times(1)).obtenerAsientoPorId(99L);
        verify(showtimeClient, times(1)).obtenerPorIdShowtime(88L);
    }

    @Test
    public void testActualizarEstado() {

        Long idBooking = 1L;
        bookingDTO dto = new bookingDTO();
        dto.setStatus(bookingStatus.CONFIRMED);

        booking reserva = new booking(idBooking, 100L, 10L, 30L, bookingStatus.CREATED, 1L);

        when(repo.findById(idBooking)).thenReturn(Optional.of(reserva));
        when(repo.save(any(booking.class))).thenReturn(reserva);

        booking resultado = service.actualizarEstado(idBooking, dto);

        assertNotNull(resultado);
        assertEquals(bookingStatus.CONFIRMED, resultado.getStatus());
        verify(repo, times(1)).save(reserva);
    }

}
