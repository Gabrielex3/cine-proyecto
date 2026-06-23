package cine.proyect.cinemaservice.Controller;

import cine.proyect.cinemaservice.DTO.comunasDTO;
import cine.proyect.cinemaservice.Model.comunas;
import cine.proyect.cinemaservice.Service.comunaService;
import cine.proyect.cinemaservice.aseemblers.ComunasModelAseembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@RestController
@RequestMapping("/api/v2/cine/comunas")
@Tag(name = "COMUNAS V2", description = "API RELACIONADA A LA CREACION DE COMUNAS CON SOPORTE HATEOAS")
public class ComunasControllerV2 {

    @Autowired
    private comunaService service;



    @Autowired
    private ComunasModelAseembler assembler;

    @GetMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO GET FIND ALL COMUNAS V2", description = "Lista todas las comunas con formato HAL JSON")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa", content = @Content(mediaType = MediaTypes.HAL_JSON_VALUE)),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.", content = @Content)
    })
    public CollectionModel<EntityModel<comunasDTO>> listarComunas() {
        List<EntityModel<comunasDTO>> comunas = service.obtenerComunas().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(comunas,
                linkTo(methodOn(ComunasControllerV2.class).listarComunas()).withSelfRel());
    }

    @GetMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO GET FIND COMUNA BY ID V2", description = "Busca una comuna por ID específica con enlaces HATEOAS")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa. Se encontró la comuna."),
            @ApiResponse(responseCode = "404", description = "Comuna no encontrada."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public EntityModel<comunasDTO> obtenerComunaPorId(@PathVariable Long id) {
        comunas comuna = service.obtenerComunaPorId(id);
        return assembler.toModel(comuna);
    }

    @PostMapping(produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO POST CREATE COMUNA V2", description = "Crea una nueva Comuna aportando el URI de acceso.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comuna creada exitosamente."),
            @ApiResponse(responseCode = "400", description = "Petición no valida."),
            @ApiResponse(responseCode = "409", description = "Conflicto. Ya existe una comuna."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<EntityModel<comunasDTO>> crearComuna(@Valid @RequestBody comunasDTO dto) {
        comunas nuevaComuna = service.crearComuna(dto);
        EntityModel<comunasDTO> entityModel = assembler.toModel(nuevaComuna);

        return ResponseEntity
                .created(linkTo(methodOn(ComunasControllerV2.class).obtenerComunaPorId(nuevaComuna.getId())).toUri())
                .body(entityModel);
    }

    @PutMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO PUT UPDATE COMUNA V2", description = "Actualiza los datos de una comuna existente mediante su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa. La comuna fue actualizada correctamente."),
            @ApiResponse(responseCode = "400", description = "Petición inválida."),
            @ApiResponse(responseCode = "404", description = "Comuna no encontrada."),
            @ApiResponse(responseCode = "409", description = "Conflicto. Nombre ya en uso."),
            @ApiResponse(responseCode = "500", description = "Error interno.")
    })
    public ResponseEntity<EntityModel<comunasDTO>> actualizarComuna(@PathVariable Long id, @RequestBody comunasDTO dto) {
        comunas comunaActualizada = service.actualizarComuna(id, dto);
        return ResponseEntity.ok(assembler.toModel(comunaActualizada));
    }

    @DeleteMapping(value = "/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    @Operation(summary = "METODO DELETE COMUNA V2", description = "Elimina una comuna existente con ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Operación exitosa."),
            @ApiResponse(responseCode = "400", description = "Petición inválida."),
            @ApiResponse(responseCode = "404", description = "Comuna no encontrada."),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor.")
    })
    public ResponseEntity<?> eliminarComuna(@PathVariable Long id) {
        service.eliminarComuna(id);
        return ResponseEntity.noContent().build();
    }
}