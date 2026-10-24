package com.autoscolombia.parqueadero.service;

import com.autoscolombia.parqueadero.model.Comprobante;
import com.autoscolombia.parqueadero.model.RegistroAcceso;
import com.autoscolombia.parqueadero.repository.ComprobanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ComprobanteService {

    private final ComprobanteRepository comprobanteRepository;

    /** RF-10: genera el comprobante con número consecutivo. */
    @Transactional
    public Comprobante generar(RegistroAcceso registro, String tipo) {
        int siguiente = comprobanteRepository.findTopByOrderByConsecutivoDesc()
                .map(c -> c.getConsecutivo() + 1)
                .orElse(1);
        Comprobante c = new Comprobante();
        c.setConsecutivo(siguiente);
        c.setTipo(tipo);
        c.setFechaHora(LocalDateTime.now());
        c.setRegistro(registro);
        return comprobanteRepository.save(c);
    }
}
