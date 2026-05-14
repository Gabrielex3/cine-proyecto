package cine.project.roomservices.service;

import cine.project.roomservices.DTO.salaDTO;
import cine.project.roomservices.model.Sala;
import cine.project.roomservices.repository.SalaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
public class SalaService {

    @Autowired
    private SalaRepository repoSala;

    @Transactional
    public Sala crearSala(salaDTO dto) {
        log.info("Crear sala: intentando crear una nueva sala");
        try {
            Sala sala = new Sala();
            sala.setNombre(dto.getNombre());
            sala.setTipo(dto.getTipo());
            sala.setActivo(dto.getActivo());

            Sala salaGuardada = repoSala.save(sala);

            log.info("Crear sala: sala creada exitosamente con ID: {}", salaGuardada.getId());
            return salaGuardada;

        } catch (Exception e) {
            log.error("Crear sala: error al guardar la sala {}: {}", dto.getNombre(), e.getMessage());
            throw new RuntimeException("Crear sala: no se pudo crear la sala. Detalles: " + e.getMessage());
        }
    }

    public List<Sala> obtenerTodas(){
        log.info("Buscar todas las salas: Iniciando consulta.");
        List<Sala> salasList = repoSala.findAll();
        log.info("Buscar todas las salas: consulta finalizada. Registros recuperados: {}", salasList.size());
        return salasList;
    }

    public Sala obtenerPorId(Long id) {
        log.info("Buscando sala por id: Buscando sala con ID: {}", id);
        return repoSala.findById(id).orElseThrow(() -> {
            log.error("Buscando sala por id: Fallo de búsqueda, no existe la sala con ID {}", id);
            return new RuntimeException("Buscando sala por id: sala no encontrada con ID: " + id);
        });
    }
    public void desactivarSala(Long id) {
        log.info("Desactivar Sala: iniciando proceso para desactivar la sala con ID: {}", id);
        Sala sala = repoSala.findById(id).orElseThrow(() -> {
                    log.error("Desactivar Sala: fallo al desactivar: No existe ninguna sala con el ID: {}", id);
                    return new RuntimeException("Desactivar Sala: no se encontró la sala con ID " + id + ". Operación cancelada.");});
        if (!sala.getActivo()) {
            log.warn("Desactivar Sala: la sala con ID: {} ya se encuentra desactivada.", id);
            return;
        }
        sala.setActivo(false);
        repoSala.save(sala);
        log.info("Sala ID: {} desactivada exitosamente.", id);
    }
}
