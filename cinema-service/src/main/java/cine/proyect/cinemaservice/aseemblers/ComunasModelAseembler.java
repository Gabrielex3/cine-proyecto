package cine.proyect.cinemaservice.aseemblers;

import cine.proyect.cinemaservice.Controller.ComunasControllerV2;
import cine.proyect.cinemaservice.Model.comunas;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class ComunasModelAseembler implements RepresentationModelAssembler<comunas, EntityModel<comunas>> {

    @Override
    public EntityModel<comunas> toModel(comunas comuna) {
        return EntityModel.of(comuna,
                linkTo(methodOn(ComunasControllerV2.class).obtenerComunaPorId(comuna.getId())).withRel("findComuna"),
                linkTo(methodOn(ComunasControllerV2.class).actualizarComuna(comuna.getId(), null)).withRel("updateComuna"),
                linkTo(methodOn(ComunasControllerV2.class).eliminarComuna(comuna.getId())).withRel("deleteComuna"),
                linkTo(methodOn(ComunasControllerV2.class).listarComunas()).withRel("findAllComunas")
        );
    }
}
