package cine.proyect.userservice.Controller;

import cine.proyect.userservice.DTO.userDTO;
import cine.proyect.userservice.Model.User;
import cine.proyect.userservice.Service.userService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/cine/users")
public class userController {

    @Autowired
    private userService service;

    @GetMapping
    public ResponseEntity<List<User>> findAllUsers() {
        log.info("Listar Users : Buscando todos los usuarios");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("BuscarPorId: Obteniendo usuario con ID {}", id);
        return ResponseEntity.ok(service.findUserById(id));
    }

    @PostMapping
    public ResponseEntity<User> create(@Valid @RequestBody userDTO dto) {
        log.info("Crear: Creando nuevo usuario con RUT {}", dto.getRut());
        User nuevoUsuario = service.createUser(dto);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody userDTO dto) {
        User usuarioActualizado = service.updateUser(id, dto);

        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Eliminar: Eliminando usuario con ID {}", id);
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
