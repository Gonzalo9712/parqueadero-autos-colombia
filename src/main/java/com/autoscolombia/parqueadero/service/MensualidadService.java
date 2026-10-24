package com.autoscolombia.parqueadero.service;

import com.autoscolombia.parqueadero.exception.NegocioException;
import com.autoscolombia.parqueadero.model.Mensualidad;
import com.autoscolombia.parqueadero.model.Vehiculo;
import com.autoscolombia.parqueadero.repository.MensualidadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MensualidadService {

    private final MensualidadRepository mensualidadRepository;

    /** RF-02: el cliente dueño del vehículo debe tener una mensualidad vigente. */
    public void validarVigencia(Vehiculo vehiculo) {
        boolean vigente = mensualidadRepository
                .findByCliente_IdCliente(vehiculo.getCliente().getIdCliente())
                .stream()
                .anyMatch(Mensualidad::estaVigente);
        if (!vigente) {
            throw new NegocioException("La mensualidad del cliente está vencida o no existe. No se permite el ingreso.");
        }
    }
}
