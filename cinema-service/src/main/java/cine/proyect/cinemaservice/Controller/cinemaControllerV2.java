package cine.proyect.cinemaservice.Controller;

import cine.proyect.cinemaservice.DTO.CinemaDTO;
import cine.proyect.cinemaservice.Model.Cinema;
import cine.proyect.cinemaservice.Service.ServiceCinema;
import cine.proyect.cinemaservice.aseemblers.CinemaModelAseembler;
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
@RequestMapping("/api/v2/cine/cinemas")
@Tag(name = "CINEMAS V2", description = "API RELACIONADA A LA CREACION DE CINEMAS CON SOPORTE HATEOAS")
public class cinemaControllerV2 {

    @Autowired
    private ServiceCinema service;

    @Autowired
    private CinemaModelAseembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO GET FIND ALL CINEMAS V2", description = "Lista todos los cines con formato HAL JSON")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content)
    })
    public CollectionModel<EntityModel<Cinema>> getAllCinemas() {
        log.info("REST request V2: Solicitando lista completa de sucursales");

        List<EntityModel<Cinema>> cines = service.obtenerTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(cines,
                linkTo(methodOn(cinemaControllerV2.class).getAllCinemas()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO GET FIND CINEMA BY ID V2", description = "Busca un cine por id especifico con enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa. Se encontró el Cine."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content)
    })
    public EntityModel<Cinema> getCinemaById(@PathVariable Long id) {
        log.info("REST request V2: Solicitando sucursal con ID: {}", id);
        Cinema cinema = service.getCinemaById(id);
        return assembler.toModel(cinema);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO POST CREATE CINEMA V2", description = "Crea un nuevo cine aportando el URI de acceso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cine creado exitosamente."),
            @ApiResponse(responseCode = "400", description = "Petición no valida."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<EntityModel<Cinema>> crearCinema(@Valid @RequestBody CinemaDTO dto) {
        log.info("REST request V2: Solicitud para crear sucursal: {}", dto.getCine());

        Cinema nuevoCinema = service.crearCinema(dto);
        EntityModel<Cinema> entityModel = assembler.toModel(nuevoCinema);

        return ResponseEntity
                .created(linkTo(methodOn(cinemaControllerV2.class).getCinemaById(nuevoCinema.getId())).toUri())
                .body(entityModel);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO PUT UPDATE CINEMA V2", description = "Actualiza los datos de un cine existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa. El cine fue actualizado correctamente."),
            @ApiResponse(responseCode = "400", description = "Petición inválida."),
            @ApiResponse(responseCode = "404", description = "Cine no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<EntityModel<Cinema>> actualizarCinema(@PathVariable Long id, @RequestBody CinemaDTO dto) {
        log.info("REST request V2: Solicitud para actualizar la sucursal con ID: {}", id);

        Cinema cinemaActualizado = service.actualizarCinema(id, dto);
        return ResponseEntity.ok(assembler.toModel(cinemaActualizado));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO DELETE CINEMA V2", description = "Elimina un Cine existente con ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operación exitosa. El cine fue eliminado correctamente."),
            @ApiResponse(responseCode = "400", description = "Petición inválida."),
            @ApiResponse(responseCode = "404", description = "Cine no encontrado."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> eliminarCinema(@PathVariable Long id) {
        log.info("REST request V2: Solicitud para eliminar la sucursal con ID: {}", id);
        service.eliminarCinema(id);
        return ResponseEntity.noContent().build();
    }
}