package cine.proyect.cinemaservice.Controller;


import cine.proyect.cinemaservice.DTO.CinemaDTO;
import cine.proyect.cinemaservice.Model.Cinema;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/api/v1/cine/cinemas")
public class ControllerCinema {

    @Autowired
    private cine.proyect.cinemaservice.Service.ServiceCinema service;

    @GetMapping
    public ResponseEntity<List<Cinema>> getAllCinemas() {
        log.info("REST request: Solicitando lista completa de sucursales");
        return ResponseEntity.ok(service.getAllCinemas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cinema> getCinemaById(@PathVariable Long id) {
        log.info("REST request: Solicitando sucursal con ID: {}", id);
        return ResponseEntity.ok(service.getCinemaById(id));
    }

    @PostMapping
    public ResponseEntity<Cinema> crearCinema(@Valid @RequestBody CinemaDTO dto) {
        log.info("REST request: Solicitud para crear sucursal: {}", dto.getCine());
        Cinema crearCinema = service.crearCinema(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(crearCinema);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cinema> actualizarCinema(@PathVariable Long id, @Valid @RequestBody CinemaDTO dto) {
        log.info("REST request: Solicitud para actualizar la sucursal con ID: {}", id);
        Cinema actualizarCinema = service.actualizarCinema(id, dto);
        return ResponseEntity.ok(actualizarCinema);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCinema(@PathVariable Long id) {
        log.info("REST request: Solicitud para eliminar la sucursal con ID: {}", id);
        service.eliminarCinema(id);
        return ResponseEntity.noContent().build();
    }




}
