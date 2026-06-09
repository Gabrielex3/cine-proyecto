package cine.proyect.cinemaservice.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cinemas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cinema {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, unique = true)
    private String cine;

    @Column(nullable = false, unique = true)
    private String direccion;

    @ManyToOne
    @JoinColumn(name = "comuna_id", nullable = false)
    @JsonIgnoreProperties("cinemas")
    private comunas comuna;

}
