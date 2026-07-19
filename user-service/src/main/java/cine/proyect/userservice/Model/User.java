package cine.proyect.userservice.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", catalog = "cine_users_db")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, name = "RUT")
    private String rut;

    @Column(name = "NOMBRE")
    private String nombre;

    @Column(name = "APELLIDO")
    private String apellido;

    @Column(unique = true, nullable = false, name = "CORREO")
    private String correo;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;

    @Column(unique = true, nullable = false, name = "TELEFONO")
    private String telefono;
}
