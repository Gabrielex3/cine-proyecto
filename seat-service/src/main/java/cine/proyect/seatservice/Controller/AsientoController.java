package cine.proyect.seatservice.Controller;

import cine.proyect.seatservice.Dto.AsientoRequestDTO;
import cine.proyect.seatservice.Model.Asiento;
import cine.proyect.seatservice.Service.AsientoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/cine/asiento")
@RequiredArgsConstructor
public class AsientoController {

    private final AsientoService asientoService;

    @GetMapping
    public ResponseEntity<List<Asiento>> getAllAsientos(){
        List<Asiento> listaAsientos =asientoService.listarAsientos();
        return new ResponseEntity<>(listaAsientos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Asiento> crear(@Valid @RequestBody AsientoRequestDTO dto) {
        Asiento nuevo = asientoService.crearAsiento(dto);
        return new ResponseEntity<>(nuevo, HttpStatus.CREATED);
    }

    @GetMapping("/sala/{salaId}")
    public ResponseEntity<List<Asiento>> listar(@PathVariable Long salaId) {
        return ResponseEntity.ok(asientoService.listarPorSala(salaId));
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Asiento> actualizarEstado(@PathVariable Long id, @RequestParam boolean disponible) {
        return ResponseEntity.ok(asientoService.actualizarEstado(id, disponible));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        asientoService.eliminarAsiento(id);
        return ResponseEntity.noContent().build();
    }
}