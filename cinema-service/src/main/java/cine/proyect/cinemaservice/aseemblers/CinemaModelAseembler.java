package cine.proyect.cinemaservice.aseemblers;

import cine.proyect.cinemaservice.Controller.cinemaControllerV2;
import cine.proyect.cinemaservice.Model.Cinema;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class CinemaModelAseembler implements RepresentationModelAssembler<Cinema, EntityModel<Cinema>> {

    @Override
    public EntityModel<Cinema> toModel(Cinema cinema) {
        return EntityModel.of(cinema,
                linkTo(methodOn(cinemaControllerV2.class).getCinemaById(cinema.getId())).withRel("findCinemaById"),
                linkTo(methodOn(cinemaControllerV2.class).actualizarCinema(cinema.getId(), null)).withRel("updateCinema"),
                linkTo(methodOn(cinemaControllerV2.class).eliminarCinema(cinema.getId())).withRel("deleteCinema"),
                linkTo(methodOn(cinemaControllerV2.class).getAllCinemas()).withRel("cinemas")
        );
    }
}