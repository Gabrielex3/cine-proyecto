package cine.proyect.userservice.assemblers;

import cine.proyect.userservice.Controller.userControllerV2;
import cine.proyect.userservice.Model.User;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<User, EntityModel<User>> {
    @Override
    public EntityModel<User> toModel(User user) {
        return EntityModel.of(user,
                linkTo(methodOn(userControllerV2.class).getUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(userControllerV2.class).updateUser(user.getId(), null)).withRel("update"),
                linkTo(methodOn(userControllerV2.class).delete(user.getId())).withRel("delete"),
                linkTo(methodOn(userControllerV2.class).findAllUsers()).withRel("find users")
        );
    }
}
