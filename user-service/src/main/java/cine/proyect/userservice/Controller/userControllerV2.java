package cine.proyect.userservice.Controller;

import cine.proyect.userservice.DTO.userDTO;
import cine.proyect.userservice.Model.User;
import cine.proyect.userservice.Service.userService;
import cine.proyect.userservice.assemblers.UserModelAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Slf4j
@RestController
@RequestMapping("/api/v2/cine/users")
@Tag(name = "USUARIOS V2", description = "API RELACIONADA A LA CREACION DE USUARIOS CON SOPORTE HATEOAS")
public class userControllerV2 {

    @Autowired
    private userService service;

    @Autowired
    private UserModelAssembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO GET FIND ALL USERS V2", description = "Lista todos los usuarios con formato HAL JSON")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content)
    })
    public CollectionModel<EntityModel<User>> findAllUsers() {
        log.info("Listar Users V2: Buscando todos los usuarios");

        List<EntityModel<User>> users = service.findAll().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(users,
                linkTo(methodOn(userControllerV2.class).findAllUsers()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO GET FIND USER BY ID V2", description = "Busca un usuario por id específica con enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa. Se encontró el usuario."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public EntityModel<User> getUserById(@PathVariable Long id) {
        log.info("BuscarPorId V2: Obteniendo usuario con ID {}", id);
        User user = service.findUserById(id);
        return assembler.toModel(user);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO POST CREATE USER V2", description = "Crea un nuevo usuario aportando el URI de acceso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuario creado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Petición no válida."),
            @ApiResponse(responseCode = "409", description = "Conflicto. Ya existe el usuario."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<EntityModel<User>> create(@Valid @RequestBody userDTO dto) {
        log.info("Crear V2: Creando nuevo usuario con RUT {}", dto.getRut());
        User nuevoUsuario = service.createUser(dto);

        EntityModel<User> entityModel = assembler.toModel(nuevoUsuario);

        return ResponseEntity
                .created(linkTo(methodOn(userControllerV2.class).getUserById(nuevoUsuario.getId())).toUri())
                .body(entityModel);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO PUT UPDATE USER V2", description = "Actualiza los datos de un usuario existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa. El usuario fue actualizado correctamente."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor, problema de actualizador de usuario.")
    })
    public ResponseEntity<EntityModel<User>> updateUser(@PathVariable Long id, @RequestBody userDTO dto) {
        log.info("Actualizar V2: Modificando usuario con ID {}", id);
        User usuarioActualizado = service.updateUser(id, dto);

        return ResponseEntity.ok(assembler.toModel(usuarioActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO DELETE USER V2", description = "Elimina un usuario existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operación exitosa. No se devuelve contenido."),
            @ApiResponse(responseCode = "404", description = "Usuario no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> delete(@PathVariable Long id) {
        log.info("Eliminar V2: Eliminando usuario con ID {}", id);
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
