package cine.proyect.showtimeservice.controller;

import cine.proyect.showtimeservice.dto.ShowtimeRequestDTO;
import cine.proyect.showtimeservice.model.Showtime;
import cine.proyect.showtimeservice.service.ShowtimeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/showtime")
@RequiredArgsConstructor
public class ShowtimeController {
    private final ShowtimeService showtimeService;

    @PostMapping
    public ResponseEntity<Showtime> crear(@Valid @RequestBody ShowtimeRequestDTO dto){
        return new ResponseEntity<>(showtimeService.crearFuncion(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Showtime>>listar(){
        return ResponseEntity.ok(showtimeService.listarTodas());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Showtime> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(showtimeService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Showtime>actualizar(@PathVariable Long id, @Valid @RequestBody ShowtimeRequestDTO dto){
        return ResponseEntity.ok(showtimeService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>eliminar(@PathVariable Long id){
        showtimeService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
