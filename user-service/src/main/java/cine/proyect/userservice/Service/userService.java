package cine.proyect.userservice.Service;

import cine.proyect.userservice.DTO.userDTO;
import cine.proyect.userservice.Model.User;
import cine.proyect.userservice.Repository.userRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class userService {

    @Autowired
    private userRepository repo;

    public List<User> findAll() {
        log.info("Iniciando consulta global de usuarios.");
        List<User> users = repo.findAll();
        log.info("Consulta finalizada. Registros recuperados: {}", users.size());
        return users;
    }

    public User createUser(userDTO dto) {
        log.info("Validando existencia del RUT: {}", dto.getRut());

        if (repo.findByRut(dto.getRut()).isPresent()) {
            log.error("Fallo en creación: El RUT {} ya está registrado", dto.getRut());
            throw new RuntimeException("El RUT ingresado ya pertenece a otro usuario.");
        }

        try {
            User user = new User();
            user.setRut(dto.getRut());
            user.setNombre(dto.getNombre());
            user.setApellido(dto.getApellido());
            user.setCorreo(dto.getCorreo());
            user.setTelefono(dto.getTelefono());

            User guardado = repo.save(user);
            log.info("Usuario creado exitosamente con ID: {}", guardado.getId());
            return guardado;
        } catch (Exception e) {
            log.error("Error al crear el nuevo usuario: {}", e.getMessage());
            throw new RuntimeException("No se pudo crear el usuario en la base de datos.");
        }
    }

    public User findUserById(Long id) {
        log.info("Buscando usuario con ID: {}", id);
        return repo.findById(id).orElseThrow(() -> {
            log.error("Fallo de búsqueda: No existe el usuario con ID {}", id);
            return new RuntimeException("Usuario no encontrado con ID: " + id);
        });
    }

    public User updateUser(Long id, userDTO dto) {
        log.info("Iniciando actualización para el ID: {}", id);

        User userExistente = repo.findById(id).orElseThrow(() -> {
            log.error("Fallo de actualización: El ID {} no existe", id);
            return new RuntimeException("Usuario no encontrado con ID: " + id);
        });

        try {
            userExistente.setNombre(dto.getNombre() != null ? dto.getNombre() : userExistente.getNombre());
            userExistente.setApellido(dto.getApellido() != null ? dto.getApellido() : userExistente.getApellido());
            userExistente.setCorreo(dto.getCorreo() != null ? dto.getCorreo() : userExistente.getCorreo());
            userExistente.setTelefono(dto.getTelefono() != null ? dto.getTelefono() : userExistente.getTelefono());
            userExistente.setRut(dto.getRut() != null ? dto.getRut() : userExistente.getRut());

            User actualizado = repo.save(userExistente);
            log.info("Actualización exitosa para el ID: {}", actualizado.getId());
            return actualizado;

        } catch (Exception e) {
            log.error("Error al actualizar el usuario ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error interno al intentar actualizar el usuario.");
        }
    }


    public void deleteUser(Long id) {
        log.info("Iniciando proceso de eliminación para el ID: {}", id);

        if (!repo.existsById(id)) {
            log.error("Fallo al eliminar: El ID {} no existe", id);
            throw new RuntimeException("No se puede eliminar: Usuario con ID " + id + " no existe");
        }

        try {
            repo.deleteById(id);
            log.info("Usuario con ID {} eliminado exitosamente", id);
        } catch (Exception e) {
            log.error("Error al eliminar el usuario ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Error al intentar borrar el registro de la base de datos.");
        }
    }
}
