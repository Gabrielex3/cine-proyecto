package cine.proyect.userservice;

import cine.proyect.userservice.DTO.userDTO;
import cine.proyect.userservice.Model.User;
import cine.proyect.userservice.Repository.userRepository;
import cine.proyect.userservice.Service.userService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@Slf4j
@SpringBootTest
public class userServiceTest {
    @Autowired
    private userService service;
    @MockitoBean
    private userRepository repo;

    @Test
    public void findAll() {
        log.info("Iniciando test de findAll...");

        List<User> users = service.findAll();

        log.info("Consulta terminada...");

        assertNotNull(users, "La lista de usuarios no debería ser nula");
        assertFalse(users.isEmpty(), "La lista debería tener al menos un usuario");
    }

    @Test
    public void testFindByID() {
        Long id = 1L; // 🪄 Usamos 1L para indicar que es un tipo Long
        User user = new User(id,"22294681-6","Gabriel","Cornejo","cornejogabriel28@gmail.com","+56982579052");

        // Define el comportamiento del mock: cuando se busque el ID 1, devuelve el usuario falso.
        when(repo.findById(id)).thenReturn(Optional.of(user));

        User found = service.findUserById(id);

        assertNotNull(found, "El usuario no debería ser nulo");
        assertEquals(id.toString(), found.getId(), "El ID del usuario devuelto no coincide");
    }


}
