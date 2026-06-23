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

    public List<Sala> obtenerTodas() {
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

    public Sala desactivarSala(Long id, boolean actualizarEstado) {
        log.info("Actualizar Sala: iniciando proceso para actualizar estado de la sala con ID: {}", id);

        Sala sala = repoSala.findById(id).orElseThrow(() -> {
            log.error("Actualizar Sala: fallo al actualizar: No existe ninguna sala con el ID: {}", id);
            return new RuntimeException("Actualizar Sala: no se encontró la sala con ID " + id + ". Operación cancelada.");
        });

        if (!sala.getActivo()) {
            log.warn("Actualizar Sala: la sala con ID: {} ya se encuentra desactivada.", id);
        } else {
            log.warn("Actualizar Sala: la sala con ID: {} ya se encuentra activada.", id);
        }

        sala.setActivo(actualizarEstado);
        log.info("Sala ID: {} actualizada exitosamente a estado: {}", id, actualizarEstado);
        return repoSala.save(sala);
    }
}