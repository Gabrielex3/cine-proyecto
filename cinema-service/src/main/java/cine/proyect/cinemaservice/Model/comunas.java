package cine.proyect.cinemaservice.Model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comunas")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class comunas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @OneToMany(mappedBy = "comuna", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties("comuna")
    private List<Cinema> cinemas = new ArrayList<>();
}
