package com.autoscolombia.parqueadero.service;

import com.autoscolombia.parqueadero.dto.UsuarioForm;
import com.autoscolombia.parqueadero.exception.NegocioException;
import com.autoscolombia.parqueadero.model.Rol;
import com.autoscolombia.parqueadero.model.UsuarioSistema;
import com.autoscolombia.parqueadero.repository.RolRepository;
import com.autoscolombia.parqueadero.repository.UsuarioSistemaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/** Gestión de usuarios del sistema (Iteración 2). */
@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioSistemaRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public List<UsuarioSistema> listar() {
        return usuarioRepository.findAllByOrderByNombreAsc();
    }

    @Transactional(readOnly = true)
    public UsuarioSistema obtener(Integer id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new NegocioException("El usuario no existe."));
    }

    @Transactional(readOnly = true)
    public List<Rol> roles() {
        return rolRepository.findAll();
    }

    /** RF-12: crear usuario con rol y contraseña cifrada. */
    @Transactional
    public UsuarioSistema crear(UsuarioForm f) {
        String username = f.getUsername().trim();
        if (usuarioRepository.findByUsername(username).isPresent()) {
            throw new NegocioException("Ya existe un usuario con el nombre «" + username + "».");
        }
        validarClave(f.getPassword(), true);

        UsuarioSistema u = new UsuarioSistema();
        u.setNombre(f.getNombre().trim());
        u.setUsername(username);
        u.setPasswordHash(passwordEncoder.encode(f.getPassword()));
        u.setActivo(f.isActivo());
        u.setRol(buscarRol(f.getIdRol()));
        return usuarioRepository.save(u);
    }

    /** RF-13 y RF-15: editar nombre, rol, estado y (opcionalmente) cambiar la contraseña. */
    @Transactional
    public UsuarioSistema actualizar(Integer id, UsuarioForm f, String usernameActual) {
        UsuarioSistema u = obtener(id);
        Rol rol = buscarRol(f.getIdRol());

        boolean esElMismo = u.getUsername().equals(usernameActual);
        if (esElMismo && (!f.isActivo() || !"ADMINISTRADOR".equals(rol.getNombre()))) {
            throw new NegocioException("No puede desactivarse ni quitarse el rol de administrador a sí mismo.");
        }

        u.setNombre(f.getNombre().trim());
        u.setRol(rol);
        u.setActivo(f.isActivo());

        if (f.getPassword() != null && !f.getPassword().isBlank()) {
            validarClave(f.getPassword(), true);
            u.setPasswordHash(passwordEncoder.encode(f.getPassword()));
        }
        return usuarioRepository.save(u);
    }

    /** RF-14: activar o desactivar un usuario (no se elimina para conservar la trazabilidad). */
    @Transactional
    public UsuarioSistema cambiarEstado(Integer id, String usernameActual) {
        UsuarioSistema u = obtener(id);
        if (u.getUsername().equals(usernameActual)) {
            throw new NegocioException("No puede desactivar su propio usuario.");
        }
        u.setActivo(!u.getActivo());
        return usuarioRepository.save(u);
    }

    private Rol buscarRol(Integer idRol) {
        return rolRepository.findById(idRol)
                .orElseThrow(() -> new NegocioException("El rol seleccionado no existe."));
    }

    private void validarClave(String clave, boolean obligatoria) {
        if (clave == null || clave.isBlank()) {
            if (obligatoria) throw new NegocioException("La contraseña es obligatoria.");
            return;
        }
        if (clave.length() < 6) {
            throw new NegocioException("La contraseña debe tener al menos 6 caracteres.");
        }
    }
}
