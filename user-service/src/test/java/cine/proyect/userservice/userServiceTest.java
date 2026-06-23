package cine.proyect.userservice;

import cine.proyect.userservice.DTO.userDTO;
import cine.proyect.userservice.Model.User;
import cine.proyect.userservice.Repository.userRepository;
import cine.proyect.userservice.Service.userService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("local")
public class userServiceTest {

    @Autowired
    private userService service;

    @MockitoBean
    private userRepository repo;

    @Test
    public void testFindAll() {
        when(repo.findAll()).thenReturn(List.of(new User(1L, "11111111-1", "Juan", "Perez", "juan@gmail.com", "+56911111111")));

        List<User> users = service.findAll();

        assertNotNull(users);
        assertEquals(1, users.size());
    }

    @Test
    public void testFindById() {
        Long id = 1L;
        User user = new User(id, "22243233-2", "Gabriel", "Exe", "gabriel@gmail.com", "+5623257242");

        when(repo.findById(id)).thenReturn(Optional.of(user));

        User found = service.findUserById(id);

        assertNotNull(found);
        assertEquals(id, found.getId(), "El ID del usuario devuelto no coincide");
    }

    @Test
    public void testSave() {
        userDTO dtoEntrada = new userDTO("22243233-2", "Gabriel", "Exe", "gabriel@gmail.com", "+5623257242");
        User usuarioNuevo = new User(1L,"22243233-2", "Gabriel", "Exe", "gabriel@gmail.com", "+5623257242");

        when(repo.findByRut(dtoEntrada.getRut())).thenReturn(Optional.empty());

        when(repo.save(any(User.class))).thenReturn(usuarioNuevo);

        User saved = service.createUser(dtoEntrada);

        assertNotNull(saved);
        assertEquals("Gabriel", saved.getNombre());
    }

    @Test
    public void testUpdate() {
        Long id = 1L;

        userDTO dtoActualizacion = new userDTO("22243233-2", "Gabriel Actualizado", "Exe", "gabriel@gmail.com", "+56999999999");

        User usuarioExistente = new User(id, "22243233-2", "Gabriel", "Exe", "gabriel@gmail.com", "+5623257242");

        User usuarioModificado = new User(id, "22243233-2", "Gabriel Actualizado", "Exe", "gabriel@gmail.com", "+56999999999");

        when(repo.findById(id)).thenReturn(Optional.of(usuarioExistente));

        when(repo.save(any(User.class))).thenReturn(usuarioModificado);

        User resultado = service.updateUser(id, dtoActualizacion);

        assertNotNull(resultado, "El usuario actualizado no debería ser nulo");
        assertEquals("Gabriel Actualizado", resultado.getNombre(), "El nombre no se actualizó correctamente");
        assertEquals("+56999999999", resultado.getTelefono(), "El teléfono no se actualizó correctamente");
    }


    @Test
    public void testDeleteById() {
        Long id = 1L;

        when(repo.existsById(id)).thenReturn(true);

        doNothing().when(repo).deleteById(id);

        service.deleteUser(id);

        verify(repo, times(1)).deleteById(id);
    }
}