package com.autoscolombia.parqueadero.service;

import com.autoscolombia.parqueadero.exception.NegocioException;
import com.autoscolombia.parqueadero.model.*;
import com.autoscolombia.parqueadero.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccesoService {

    private final RegistroAccesoRepository registroRepository;
    private final VehiculoRepository vehiculoRepository;
    private final UsuarioSistemaRepository usuarioRepository;
    private final MensualidadService mensualidadService;
    private final CeldaService celdaService;
    private final ComprobanteService comprobanteService;

    /** RF-01, RF-02, RF-03, RF-04, RF-10 */
    @Transactional
    public Comprobante registrarEntrada(String placa, String username) {
        String p = normalizar(placa);

        Vehiculo vehiculo = vehiculoRepository.findByPlacaIgnoreCase(p)
                .orElseThrow(() -> new NegocioException("La placa " + p + " no está registrada en el sistema."));

        mensualidadService.validarVigencia(vehiculo);

        if (registroRepository.findByVehiculo_PlacaIgnoreCaseAndFechaHoraSalidaIsNull(p).isPresent()) {
            throw new NegocioException("El vehículo " + p + " ya se encuentra dentro del parqueadero.");
        }

        Celda celda = celdaService.asignarCelda(vehiculo.getTipo());

        RegistroAcceso registro = new RegistroAcceso();
        registro.setVehiculo(vehiculo);
        registro.setCelda(celda);
        registro.setFechaHoraEntrada(LocalDateTime.now());
        registro.setUsuarioEntrada(obtenerUsuario(username));
        registroRepository.save(registro);

        return comprobanteService.generar(registro, "ENTRADA");
    }

    /** RF-05, RF-06, RF-10 */
    @Transactional
    public Comprobante registrarSalida(String placa, String username) {
        String p = normalizar(placa);

        RegistroAcceso registro = registroRepository
                .findByVehiculo_PlacaIgnoreCaseAndFechaHoraSalidaIsNull(p)
                .orElseThrow(() -> new NegocioException("La placa " + p + " no tiene una entrada abierta."));

        registro.setFechaHoraSalida(LocalDateTime.now());
        registro.setUsuarioSalida(obtenerUsuario(username));
        registro.calcularPermanencia();
        registroRepository.save(registro);

        celdaService.liberarCelda(registro.getCelda());

        return comprobanteService.generar(registro, "SALIDA");
    }

    /** RF-07: vehículos actualmente dentro (con filtro opcional por placa). */
    @Transactional(readOnly = true)
    public List<RegistroAcceso> listarVehiculosAdentro(String buscar) {
        List<RegistroAcceso> adentro = registroRepository.findByFechaHoraSalidaIsNullOrderByFechaHoraEntradaDesc();
        if (buscar == null || buscar.isBlank()) {
            return adentro;
        }
        String filtro = buscar.trim().toUpperCase();
        return adentro.stream()
                .filter(r -> r.getVehiculo().getPlaca().toUpperCase().contains(filtro))
                .toList();
    }

    /** RF-08: historial por placa y/o rango de fechas. */
    @Transactional(readOnly = true)
    public List<RegistroAcceso> consultarHistorial(String placa, LocalDate desde, LocalDate hasta) {
        String p = (placa == null) ? "" : placa.trim();
        LocalDateTime d = (desde == null) ? LocalDateTime.of(2000, 1, 1, 0, 0) : desde.atStartOfDay();
        LocalDateTime h = (hasta == null) ? LocalDateTime.of(2100, 1, 1, 0, 0) : hasta.atTime(LocalTime.MAX);
        return registroRepository.buscarHistorial(p, d, h);
    }

    private String normalizar(String placa) {
        if (placa == null || placa.isBlank()) {
            throw new NegocioException("Debe ingresar la placa del vehículo.");
        }
        return placa.trim().toUpperCase();
    }

    private UsuarioSistema obtenerUsuario(String username) {
        return usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new NegocioException("Usuario del sistema no encontrado."));
    }
}
