package com.autoscolombia.parqueadero.service;

import com.autoscolombia.parqueadero.dto.NovedadForm;
import com.autoscolombia.parqueadero.exception.NegocioException;
import com.autoscolombia.parqueadero.model.Novedad;
import com.autoscolombia.parqueadero.model.RegistroAcceso;
import com.autoscolombia.parqueadero.repository.NovedadRepository;
import com.autoscolombia.parqueadero.repository.RegistroAccesoRepository;
import com.autoscolombia.parqueadero.repository.UsuarioSistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NovedadService {

    private final NovedadRepository novedadRepository;
    private final RegistroAccesoRepository registroRepository;
    private final UsuarioSistemaRepository usuarioRepository;

    /** RF-09: la novedad queda ligada a la entrada activa de la placa. */
    @Transactional
    public Novedad registrarNovedad(NovedadForm form, String username) {
        String placa = form.getPlaca().trim().toUpperCase();

        RegistroAcceso registro = registroRepository
                .findByVehiculo_PlacaIgnoreCaseAndFechaHoraSalidaIsNull(placa)
                .orElseThrow(() -> new NegocioException(
                        "El vehículo " + placa + " no tiene una entrada activa; no se puede registrar la novedad."));

        Novedad n = new Novedad();
        n.setTipo(form.getTipo());
        n.setDescripcion(form.getDescripcion().trim());
        n.setFechaHora(LocalDateTime.now());
        n.setRegistro(registro);
        n.setUsuario(usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NegocioException("Usuario del sistema no encontrado.")));
        return novedadRepository.save(n);
    }

    @Transactional(readOnly = true)
    public List<Novedad> ultimasNovedades() {
        return novedadRepository.findTop20ByOrderByFechaHoraDesc();
    }
}
