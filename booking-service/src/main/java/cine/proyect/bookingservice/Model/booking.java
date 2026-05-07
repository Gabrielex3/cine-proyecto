package cine.proyect.bookingservice.Model;

import jakarta.persistence.*;

@Entity
@Table(name = "bookings")
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
    @Column(nullable = false,name = "STATUS")
    private bookingStatus status;

}