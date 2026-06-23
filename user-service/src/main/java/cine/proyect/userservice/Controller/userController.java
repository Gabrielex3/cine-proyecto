package cine.proyect.userservice.Controller;

import cine.proyect.userservice.DTO.userDTO;
import cine.proyect.userservice.Model.User;
import cine.proyect.userservice.Service.userService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "USUARIOS", description = "API RELACIONADA A LA CREACION DE USUARIOS")
public class userController {

    @Autowired
    private userService service;

    @GetMapping
    @Operation(summary = "METODO GET FIND ALL USERS", description = "Lista todos los usuarios")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<List<User>> findAllUsers() {
        log.info("Listar Users : Buscando todos los usuarios");
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "METODO GET FIND USER BY ID", description = "Busca un usuario por id especifica")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. Se encontró el usuario.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor.",
                    content = @Content
            )
    })
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        log.info("BuscarPorId: Obteniendo usuario con ID {}", id);
        return ResponseEntity.ok(service.findUserById(id));
    }

    @PostMapping
    @Operation(summary = "METODO POST CREATE USER", description = "Crea un nuevo usuario.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado exitosamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición no valida. Los datos enviados no cumplen con las validaciones requeridas (ej. RUT inválido, campos obligatorios vacíos).",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto. Ya existe un usuario registrado con el mismo RUT o Email.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No  se pudo crear el usuario",
                    content = @Content
            )
    })
    public ResponseEntity<User> create(@Valid @RequestBody userDTO dto) {
        log.info("Crear: Creando nuevo usuario con RUT {}", dto.getRut());
        User nuevoUsuario = service.createUser(dto);
        return new ResponseEntity<User>(nuevoUsuario, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "METODO PUT UPDATE USER", description = "Actualiza los datos de un usuario existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Operación exitosa. El usuario fue actualizado correctamente.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. Los datos enviados en el formulario no son válidos.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado. El ID proporcionado no coincide con ningún registro.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflicto. Los nuevos datos (RUT o Email) ya están en uso por otro usuario.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo actualizar el registro.",
                    content = @Content
            )
    })
    public ResponseEntity<User> updateUser(@PathVariable Long id, @Valid @RequestBody userDTO dto) {
        User usuarioActualizado = service.updateUser(id, dto);

        return ResponseEntity.ok(usuarioActualizado);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "METODO DELETE USER", description = "Elimina un usuario existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Operación exitosa. El usuario fue eliminado correctamente. No se devuelve contenido.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Petición inválida. El formato del ID proporcionado no es correcto.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado. El ID solicitado no existe en el sistema.",
                    content = @Content
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor. No se pudo eliminar el usuario.",
                    content = @Content
            )
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("Eliminar: Eliminando usuario con ID {}", id);
        service.deleteUser(id);
        return ResponseEntity.noContent().build();
    }


}
