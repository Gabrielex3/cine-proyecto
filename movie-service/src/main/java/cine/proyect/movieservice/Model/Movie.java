package cine.proyect.movieservice.Model;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "MOVIES")
@Data
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false)
    private String genero;

    @Column(nullable = false)
    private Integer duracion;

}
