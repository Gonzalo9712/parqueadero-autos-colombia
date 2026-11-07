package com.autoscolombia.parqueadero.service;

import com.autoscolombia.parqueadero.dto.ClienteForm;
import com.autoscolombia.parqueadero.dto.MensualidadForm;
import com.autoscolombia.parqueadero.dto.VehiculoForm;
import com.autoscolombia.parqueadero.exception.NegocioException;
import com.autoscolombia.parqueadero.model.*;
import com.autoscolombia.parqueadero.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Gestión de clientes, sus vehículos y sus mensualidades (Iteración 2). */
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;
    private final MensualidadRepository mensualidadRepository;
    private final TipoVehiculoRepository tipoRepository;

    @Transactional(readOnly = true)
    public List<Cliente> listar(String buscar) {
        if (buscar == null || buscar.isBlank()) {
            return clienteRepository.findAllByOrderByApellidosAscNombresAsc();
        }
        return clienteRepository.buscar(buscar.trim());
    }

    @Transactional(readOnly = true)
    public Cliente obtener(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new NegocioException("El cliente no existe."));
    }

    @Transactional(readOnly = true)
    public List<Vehiculo> vehiculosDe(Integer idCliente) {
        return vehiculoRepository.findByCliente_IdClienteOrderByPlacaAsc(idCliente);
    }

    @Transactional(readOnly = true)
    public List<Mensualidad> mensualidadesDe(Integer idCliente) {
        return mensualidadRepository.findByCliente_IdClienteOrderByFechaFinDesc(idCliente);
    }

    @Transactional(readOnly = true)
    public List<TipoVehiculo> tiposVehiculo() {
        return tipoRepository.findAll();
    }

    /** RF-16: registrar un cliente (documento único). */
    @Transactional
    public Cliente crear(ClienteForm f) {
        String documento = f.getDocumento().trim();
        if (clienteRepository.findByDocumento(documento).isPresent()) {
            throw new NegocioException("Ya existe un cliente con el documento " + documento + ".");
        }
        Cliente c = new Cliente();
        copiar(f, c);
        c.setDocumento(documento);
        return clienteRepository.save(c);
    }

    /** RF-17: editar los datos de un cliente. */
    @Transactional
    public Cliente actualizar(Integer id, ClienteForm f) {
        Cliente c = obtener(id);
        String documento = f.getDocumento().trim();
        boolean repetido = clienteRepository.findByDocumento(documento)
                .filter(otro -> !otro.getIdCliente().equals(id))
                .isPresent();
        if (repetido) {
            throw new NegocioException("Ya existe otro cliente con el documento " + documento + ".");
        }
        copiar(f, c);
        c.setDocumento(documento);
        return clienteRepository.save(c);
    }

    /** RF-18: asociar un vehículo a un cliente (placa única). */
    @Transactional
    public Vehiculo agregarVehiculo(Integer idCliente, VehiculoForm f) {
        Cliente cliente = obtener(idCliente);
        String placa = f.getPlaca().trim().toUpperCase();
        if (vehiculoRepository.findByPlacaIgnoreCase(placa).isPresent()) {
            throw new NegocioException("La placa " + placa + " ya está registrada.");
        }
        TipoVehiculo tipo = tipoRepository.findById(f.getIdTipo())
                .orElseThrow(() -> new NegocioException("El tipo de vehículo no existe."));

        Vehiculo v = new Vehiculo();
        v.setPlaca(placa);
        v.setMarca(vacioANull(f.getMarca()));
        v.setColor(vacioANull(f.getColor()));
        v.setCliente(cliente);
        v.setTipo(tipo);
        return vehiculoRepository.save(v);
    }

    /** RF-19: registrar una mensualidad para un cliente. */
    @Transactional
    public Mensualidad agregarMensualidad(Integer idCliente, MensualidadForm f) {
        Cliente cliente = obtener(idCliente);
        if (f.getFechaFin().isBefore(f.getFechaInicio())) {
            throw new NegocioException("La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        Mensualidad m = new Mensualidad();
        m.setCliente(cliente);
        m.setFechaInicio(f.getFechaInicio());
        m.setFechaFin(f.getFechaFin());
        m.setEstado("ACTIVA");
        return mensualidadRepository.save(m);
    }

    private void copiar(ClienteForm f, Cliente c) {
        c.setTipoDocumento(f.getTipoDocumento());
        c.setNombres(f.getNombres().trim());
        c.setApellidos(f.getApellidos().trim());
        c.setTelefono(vacioANull(f.getTelefono()));
        c.setEmail(vacioANull(f.getEmail()));
    }

    private String vacioANull(String s) {
        return (s == null || s.isBlank()) ? null : s.trim();
    }
}
