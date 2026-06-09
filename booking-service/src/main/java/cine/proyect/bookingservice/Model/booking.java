package cine.proyect.bookingservice.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "BOOKINGS")
public class booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "USER_ID", nullable = false)
    private Long userId;

    @Column(name = "SHOWTIME_ID", nullable = false)
    private Long showtimeId;

    @Column(name = "SEAT_ID", nullable = false)
    private Long seatId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "STATUS", updatable = true)
    private bookingStatus status;

    @Column(name = "CINEMA_ID", nullable = false)
    private Long cinema;

}