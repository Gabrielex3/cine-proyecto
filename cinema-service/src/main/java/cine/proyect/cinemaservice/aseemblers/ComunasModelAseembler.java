package cine.proyect.cinemaservice.aseemblers;

import cine.proyect.cinemaservice.Controller.ComunasControllerV2;
import cine.proyect.cinemaservice.DTO.comunasDTO;
import cine.proyect.cinemaservice.Model.comunas;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ComunasModelAseembler implements RepresentationModelAssembler<comunas, EntityModel<comunasDTO>> {

    @Override
    public EntityModel<comunasDTO> toModel(comunas entity) {
        comunasDTO dto = new comunasDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());

        return EntityModel.of(dto,
                linkTo(methodOn(ComunasControllerV2.class).obtenerComunaPorId(entity.getId())).withSelfRel(),
                linkTo(methodOn(ComunasControllerV2.class).listarComunas()).withRel("findAllComunas"),
                linkTo(methodOn(ComunasControllerV2.class).actualizarComuna(entity.getId(), null)).withRel("updateComuna"),
                linkTo(methodOn(ComunasControllerV2.class).eliminarComuna(entity.getId())).withRel("deleteComuna")
        );
    }
}
