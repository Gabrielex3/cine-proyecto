package cine.proyect.cinemaservice.Service;

import cine.proyect.cinemaservice.DTO.comunasDTO;
import cine.proyect.cinemaservice.Model.comunas;
import cine.proyect.cinemaservice.Repository.comunaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class comunaService {

    @Autowired
    private comunaRepository repo;


    public List<comunas> obtenerComunas(){
        log.info("Iniciando proceso de busqueda de todas las comunas");
        List<comunas> listaComunas =repo.findAll();
        log.info("Se han encontrado {} comunas",listaComunas.size());
        return listaComunas;
    }

    public comunas crearComuna(comunasDTO dto) {
        try {
            log.info("Iniciando creación de comuna: {}", dto.getNombre());

            comunas comuna = new comunas();
            comuna.setNombre(dto.getNombre());

            comunas comunaCreada = repo.save(comuna);
            log.info("Comuna creada exitosamente con ID:{}  Nombre: {}", comunaCreada.getId(),comunaCreada.getNombre());
            return comunaCreada;

        } catch (Exception e) {
            log.error("Error al guardar la comuna en la BD: {}", e.getMessage());
            throw new RuntimeException("Error interno al intentar crear la comuna");
        }
    }

    public comunas obtenerComunaPorId(Long id) {
        log.info("Iniciando obtener comuna por id: {}", id);
        try {
            return repo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Comuna no encontrada con ID: " + id));
        }catch (Exception e){
            log.error("Error al obtener la comuna con ID: {}", e.getMessage());
            throw new RuntimeException("Error al obtener la comuna");
        }

    }

    public comunas actualizarComuna(Long id, comunasDTO dto) {
        log.info("Iniciando actualización de la comuna con ID: {}", id);

        comunas comunaExistente = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede actualizar: La comuna con ID " + id + " no existe."));
        try {
            comunaExistente.setNombre(dto.getNombre());

            comunas comunaActualizada = repo.save(comunaExistente);
            log.info("Comuna con ID: {} actualizada exitosamente", id);
            return comunaActualizada;

        } catch (Exception e) {
            log.error("Error al actualizar la comuna: {}", e.getMessage());
            throw new RuntimeException("Error interno al intentar actualizar la comuna.");
        }
    }

    public void eliminarComuna(Long id) {
        log.info("Iniciando proceso para eliminar comuna con ID: {}", id);

        if (!repo.existsById(id)) {
            throw new RuntimeException("No se puede eliminar: La comuna con ID " + id + " no existe.");
        }
        try {
            repo.deleteById(id);
            log.info("Comuna con ID: {} eliminada correctamente", id);
        } catch (Exception e) {
            log.error("Error al eliminar la comuna: {}", e.getMessage());
            throw new RuntimeException("No se puede eliminar la comuna porque podría tener cines asociados o hubo un error de base de datos.");
        }
    }
}
