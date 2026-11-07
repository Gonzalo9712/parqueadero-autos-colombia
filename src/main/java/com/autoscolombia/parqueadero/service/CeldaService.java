package com.autoscolombia.parqueadero.service;

import com.autoscolombia.parqueadero.dto.CeldaForm;
import com.autoscolombia.parqueadero.exception.NegocioException;
import com.autoscolombia.parqueadero.model.Celda;
import com.autoscolombia.parqueadero.model.TipoVehiculo;
import com.autoscolombia.parqueadero.repository.CeldaRepository;
import com.autoscolombia.parqueadero.repository.TipoVehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CeldaService {

    private final CeldaRepository celdaRepository;
    private final TipoVehiculoRepository tipoRepository;

    // ------------------- Iteración 1: asignación y liberación -------------------

    /** RF-03: busca una celda disponible del tipo del vehículo y la marca como ocupada. */
    public Celda asignarCelda(TipoVehiculo tipo) {
        Celda celda = celdaRepository
                .findFirstByEstadoAndTipo_IdTipoOrderByCodigoAsc("DISPONIBLE", tipo.getIdTipo())
                .orElseThrow(() -> new NegocioException(
                        "No hay celdas disponibles para vehículos tipo " + tipo.getNombre() + "."));
        celda.setEstado("OCUPADA");
        return celdaRepository.save(celda);
    }

    /** RF-06: libera la celda al registrar la salida. */
    public void liberarCelda(Celda celda) {
        celda.setEstado("DISPONIBLE");
        celdaRepository.save(celda);
    }

    // ------------------- Iteración 2: gestión de celdas -------------------

    /** RF-20 y RF-24: listado de celdas con su estado de ocupación. */
    @Transactional(readOnly = true)
    public List<Celda> listar() {
        return celdaRepository.findAllByOrderByCodigoAsc();
    }

    @Transactional(readOnly = true)
    public Celda obtener(Integer id) {
        return celdaRepository.findById(id)
                .orElseThrow(() -> new NegocioException("La celda no existe."));
    }

    @Transactional(readOnly = true)
    public List<TipoVehiculo> tiposVehiculo() {
        return tipoRepository.findAll();
    }

    /** RF-21: crear una celda (código único) para un tipo de vehículo. */
    @Transactional
    public Celda crear(CeldaForm f) {
        String codigo = f.getCodigo().trim().toUpperCase();
        if (celdaRepository.findByCodigoIgnoreCase(codigo).isPresent()) {
            throw new NegocioException("Ya existe una celda con el código " + codigo + ".");
        }
        Celda c = new Celda();
        c.setCodigo(codigo);
        c.setTipo(buscarTipo(f.getIdTipo()));
        c.setEstado("DISPONIBLE");
        return celdaRepository.save(c);
    }

    /** RF-22 y RF-23: editar código y tipo, o inactivar/reactivar una celda. */
    @Transactional
    public Celda actualizar(Integer id, CeldaForm f) {
        Celda c = obtener(id);
        if ("OCUPADA".equals(c.getEstado())) {
            throw new NegocioException("No se puede modificar la celda " + c.getCodigo() + " porque está ocupada.");
        }
        String estado = f.getEstado();
        if (!"DISPONIBLE".equals(estado) && !"INACTIVA".equals(estado)) {
            throw new NegocioException("El estado debe ser DISPONIBLE o INACTIVA.");
        }
        String codigo = f.getCodigo().trim().toUpperCase();
        boolean repetido = celdaRepository.findByCodigoIgnoreCase(codigo)
                .filter(otra -> !otra.getIdCelda().equals(id))
                .isPresent();
        if (repetido) {
            throw new NegocioException("Ya existe otra celda con el código " + codigo + ".");
        }
        c.setCodigo(codigo);
        c.setTipo(buscarTipo(f.getIdTipo()));
        c.setEstado(estado);
        return celdaRepository.save(c);
    }

    private TipoVehiculo buscarTipo(Integer idTipo) {
        return tipoRepository.findById(idTipo)
                .orElseThrow(() -> new NegocioException("El tipo de vehículo no existe."));
    }
}
