package cine.proyect.cinemaservice.Service;

import cine.proyect.cinemaservice.DTO.CinemaDTO;
import cine.proyect.cinemaservice.Model.Cinema;
import cine.proyect.cinemaservice.Model.comunas;
import cine.proyect.cinemaservice.Repository.RepositoryCinema;
import cine.proyect.cinemaservice.Repository.comunaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ServiceCinema {

    @Autowired
    private RepositoryCinema repo;
    @Autowired
    private comunaRepository crepo;


    public List<Cinema> obtenerTodos() {
        log.info("Iniciando consulta global de cinemas.");
        List<Cinema> cinemas = repo.findAll();
        log.info("Consulta finalizada. Registros recuperados: {}", cinemas.size());
        return cinemas;
    }


    public Cinema getCinemaById(Long id) {
        log.info("Buscando sucursal con ID: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Sucursal no encontrada con el ID: " + id));
    }


    public Cinema crearCinema(CinemaDTO dto) {
        comunas comuna = crepo.findById(dto.getComunaId())
                .orElseThrow(() -> new RuntimeException("La comuna con ID " + dto.getComunaId() + " no existe."));
        try {
            log.info("Iniciando creación de cine: {}", dto.getCine());

            Cinema cinema = new Cinema();
            cinema.setCine(dto.getCine());
            cinema.setDireccion(dto.getDireccion());
            cinema.setComuna(comuna);

            Cinema savedCinema = repo.save(cinema);
            log.info("Cine creado exitosamente con ID: {}", savedCinema.getId());
            return savedCinema;

        } catch (Exception e) {
            log.error("Error al guardar la cine en la BD: {}", e.getMessage());
            throw new RuntimeException("Error interno al intentar crear el cine");
        }
    }


    public Cinema actualizarCinema(Long id, CinemaDTO dto) {
        comunas comuna = crepo.findById(dto.getComunaId())
                .orElseThrow(() -> new RuntimeException("La comuna con ID " + dto.getComunaId() + " no existe."));
        try {
            log.info("Iniciando actualización de la sucursal con ID: {}", id);

            Cinema existingCinema = getCinemaById(id);

            existingCinema.setCine(dto.getCine());
            existingCinema.setDireccion(dto.getDireccion());
            existingCinema.setComuna(comuna);

            Cinema updatedCinema = repo.save(existingCinema);
            log.info("Sucursal con ID: {} actualizada exitosamente", updatedCinema.getId());
            return updatedCinema;

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al actualizar la sucursal {}: {}", id, e.getMessage());
            throw new RuntimeException("Error interno al intentar actualizar la sucursal");
        }
    }

    public void eliminarCinema(Long id) {
        try {
            log.info("Iniciando eliminación de la sucursal con ID: {}", id);

            Cinema antiguoCinema = getCinemaById(id);
            repo.delete(antiguoCinema);

            log.info("Sucursal con ID: {} eliminada correctamente", id);

        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error al eliminar la sucursal {}: {}", id, e.getMessage());
            throw new RuntimeException("Error interno al intentar eliminar la sucursal");
        }
    }
}