package cine.proyect.cinemaservice.Controller;


import cine.proyect.cinemaservice.DTO.comunasDTO;
import cine.proyect.cinemaservice.Model.comunas;
import cine.proyect.cinemaservice.Service.comunaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cine/comunas")
public class comunasController {

    @Autowired
    private comunaService service;

    @GetMapping
    public ResponseEntity<List<comunas>> listar() {
        return ResponseEntity.ok(service.obtenerComunas());
    }

    @PostMapping
    public ResponseEntity<comunas> crear(@Valid @RequestBody comunasDTO dto) {
        comunas nuevaComuna = service.crearComuna(dto);
        return new ResponseEntity<>(nuevaComuna, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<comunas> actualizar(@PathVariable Long id, @Valid @RequestBody comunasDTO dto) {
        return ResponseEntity.ok(service.actualizarComuna(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminarComuna(id);
        return ResponseEntity.noContent().build();
    }
}
