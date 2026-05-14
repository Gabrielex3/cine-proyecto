package cine.proyect.cinemaservice.Model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "cinemas")
@Data
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String cine;

    @Column(nullable = false, unique = true)
    private String direccion;

    @Column(nullable = false)
    private String ciudad;

}
