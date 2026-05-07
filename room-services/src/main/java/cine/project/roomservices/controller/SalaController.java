package cine.project.roomservices.controller;

import cine.project.roomservices.DTO.salaDTO;
import cine.project.roomservices.model.Sala;
import cine.project.roomservices.service.SalaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cine/room")
@Slf4j
@RequiredArgsConstructor
public class SalaController {
    @Autowired
    private SalaService salaService;

    @PostMapping
    public ResponseEntity<Sala> crear(@Valid @RequestBody salaDTO dto){
        log.info("Iniciando crear sala : {}", dto.getNombre());
        Sala nuevaSala = salaService.crearSala(dto);
        return new ResponseEntity<>(nuevaSala, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Sala>> listarSalas(){
        log.info("Iniciando lista de salas");
        return ResponseEntity.ok(salaService.obtenerTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Sala>  buscarPorId(@PathVariable Long id){
        log.info("Iniciando lista de sala : {}", id);
        return ResponseEntity.ok(salaService.obtenerPorId(id));
    }


    @PutMapping("/desactivar/{id}")
    public ResponseEntity<String> desactivarSala(@PathVariable Long id) {
        salaService.desactivarSala(id);
        return ResponseEntity.ok("La sala con ID " + id + " ha sido desactivada correctamente.");
    }

}
