package com.autoscolombia.parqueadero.service;

import com.autoscolombia.parqueadero.exception.NegocioException;
import com.autoscolombia.parqueadero.model.Celda;
import com.autoscolombia.parqueadero.model.TipoVehiculo;
import com.autoscolombia.parqueadero.repository.CeldaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CeldaService {

    private final CeldaRepository celdaRepository;

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
}
