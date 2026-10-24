package com.autoscolombia.parqueadero.config;

import com.autoscolombia.parqueadero.model.*;
import com.autoscolombia.parqueadero.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/** Carga datos iniciales (roles, usuarios, celdas y datos de prueba) si la base está vacía. */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RolRepository rolRepository;
    private final TipoVehiculoRepository tipoRepository;
    private final UsuarioSistemaRepository usuarioRepository;
    private final CeldaRepository celdaRepository;
    private final ClienteRepository clienteRepository;
    private final VehiculoRepository vehiculoRepository;
    private final MensualidadRepository mensualidadRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        Rol admin = rolRepository.findByNombre("ADMINISTRADOR").orElseGet(() -> rolRepository.save(new Rol("ADMINISTRADOR")));
        Rol operario = rolRepository.findByNombre("OPERARIO").orElseGet(() -> rolRepository.save(new Rol("OPERARIO")));
        TipoVehiculo carro = tipoRepository.findByNombre("CARRO").orElseGet(() -> tipoRepository.save(new TipoVehiculo("CARRO")));
        TipoVehiculo moto = tipoRepository.findByNombre("MOTO").orElseGet(() -> tipoRepository.save(new TipoVehiculo("MOTO")));

        crearUsuario("Administrador General", "admin", "admin123", admin);
        crearUsuario("Operario Turno 1", "operario", "operario123", operario);

        if (celdaRepository.count() == 0) {
            for (int i = 1; i <= 10; i++) celdaRepository.save(new Celda(String.format("C%02d", i), carro));
            for (int i = 1; i <= 5; i++) celdaRepository.save(new Celda(String.format("M%02d", i), moto));
        }

        if (clienteRepository.count() == 0) {
            Cliente c1 = crearCliente("CC", "1000000001", "Juan", "Pérez");
            Cliente c2 = crearCliente("CC", "1000000002", "María", "Gómez");
            Cliente c3 = crearCliente("CC", "1000000003", "Carlos", "Ruiz");

            crearVehiculo("ABC123", "Mazda", "Rojo", c1, carro);
            crearVehiculo("XYZ98A", "Yamaha", "Negro", c2, moto);
            crearVehiculo("VEN123", "Renault", "Gris", c3, carro); // mensualidad vencida (para probar el error)

            crearMensualidad(c1, LocalDate.now().minusDays(5), LocalDate.now().plusDays(25), "ACTIVA");
            crearMensualidad(c2, LocalDate.now().minusDays(10), LocalDate.now().plusDays(20), "ACTIVA");
            crearMensualidad(c3, LocalDate.now().minusDays(60), LocalDate.now().minusDays(30), "VENCIDA");
        }
    }

    private void crearUsuario(String nombre, String username, String clave, Rol rol) {
        if (usuarioRepository.findByUsername(username).isEmpty()) {
            UsuarioSistema u = new UsuarioSistema();
            u.setNombre(nombre);
            u.setUsername(username);
            u.setPasswordHash(passwordEncoder.encode(clave));
            u.setActivo(true);
            u.setRol(rol);
            usuarioRepository.save(u);
        }
    }

    private Cliente crearCliente(String tipoDoc, String doc, String nombres, String apellidos) {
        Cliente c = new Cliente();
        c.setTipoDocumento(tipoDoc);
        c.setDocumento(doc);
        c.setNombres(nombres);
        c.setApellidos(apellidos);
        return clienteRepository.save(c);
    }

    private void crearVehiculo(String placa, String marca, String color, Cliente cliente, TipoVehiculo tipo) {
        Vehiculo v = new Vehiculo();
        v.setPlaca(placa);
        v.setMarca(marca);
        v.setColor(color);
        v.setCliente(cliente);
        v.setTipo(tipo);
        vehiculoRepository.save(v);
    }

    private void crearMensualidad(Cliente cliente, LocalDate inicio, LocalDate fin, String estado) {
        Mensualidad m = new Mensualidad();
        m.setCliente(cliente);
        m.setFechaInicio(inicio);
        m.setFechaFin(fin);
        m.setEstado(estado);
        mensualidadRepository.save(m);
    }
}
