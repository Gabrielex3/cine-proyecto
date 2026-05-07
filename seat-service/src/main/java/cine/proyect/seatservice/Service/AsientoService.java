package cine.proyect.seatservice.Service;

import cine.proyect.seatservice.Client.SalaClient;
import cine.proyect.seatservice.Dto.AsientoRequestDTO;
import cine.proyect.seatservice.Dto.roomDTO;
import cine.proyect.seatservice.Model.Asiento;
import cine.proyect.seatservice.Repository.AsientoRepository;
import feign.FeignException;
import jakarta.transaction.Transactional;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AsientoService {
    private final AsientoRepository asientoRepository;
    private final SalaClient salaClient;


    public List <Asiento> listarAsientos() {
        log.info("Iniciando listar asientos");
        List<Asiento> asientos = asientoRepository.findAll();
        log.info("Consulta finalizada. Registros recuperados: {}", asientos.size());
        return asientos;
    }

    @Transactional
    public Asiento crearAsiento(AsientoRequestDTO dto){
        log.info("Iniciando creación de asiento para la sala: {}", dto.getSalaId());
        try {
            try {
                salaClient.obtenerSalaPorId(dto.getSalaId());
            } catch (Exception e) {
                log.error("Validación fallida: La sala con ID {} no existe en room-service", dto.getSalaId());
                throw new RuntimeException("La sala especificada no existe.");
            }

            Asiento asiento = new Asiento();
            asiento.setNumeroFila(dto.getNumeroFila());
            asiento.setDisponible(dto.getDisponible());
            asiento.setSalaId(dto.getSalaId());

            Asiento guardado = asientoRepository.save(asiento);
            log.info("Asiento creado exitosamente con ID: {}", guardado.getId());
            return guardado;

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error inesperado al crear el asiento: {}", e.getMessage());
            throw new RuntimeException("Error interno al procesar el asiento.");
        }
    }

    public List<Asiento> listarPorSala(Long salaId) {
        log.info("Listar por sala: buscando sala con ID: {}", salaId);

        try {
            roomDTO sala = salaClient.obtenerSalaPorId(salaId);

            if (sala == null) {
                throw new RuntimeException("Listar por sala: la sala con ID " + salaId + " no existe.");
            }

            return asientoRepository.findBySalaId(salaId);

        } catch (FeignException e) {
            log.error("Listar por sala: Error de comunicación: {}", e.getMessage());
            throw new RuntimeException("Listar por sala: la sala con ID " + salaId + " no existe o no está disponible.");
        }
    }

    public Asiento actualizarEstado(Long id, boolean disponible) {
        log.info("Actualizando estado del asiento ID: {} a {}", id, disponible);
        Asiento asiento = asientoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El asiento con ID " + id + " no existe."));

        asiento.setDisponible(disponible);
        return asientoRepository.save(asiento);
    }

    public void eliminarAsiento(Long id) {
        log.info("Eliminando asiento con ID: {}", id);
        if (!asientoRepository.existsById(id)) {
            throw new RuntimeException("El asiento con ID " + id + " no existe.");
        }
        asientoRepository.deleteById(id);
    }
}
