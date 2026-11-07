package com.autoscolombia.parqueadero.controller;

import com.autoscolombia.parqueadero.dto.UsuarioForm;
import com.autoscolombia.parqueadero.exception.NegocioException;
import com.autoscolombia.parqueadero.model.UsuarioSistema;
import com.autoscolombia.parqueadero.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public String listar(Model model, Principal principal) {
        model.addAttribute("usuarios", usuarioService.listar());
        model.addAttribute("usuarioActual", principal.getName());
        return "usuarios";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        return formulario(model, new UsuarioForm());
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            UsuarioSistema u = usuarioService.obtener(id);
            UsuarioForm f = new UsuarioForm();
            f.setId(u.getIdUsuarioSistema());
            f.setNombre(u.getNombre());
            f.setUsername(u.getUsername());
            f.setIdRol(u.getRol().getIdRol());
            f.setActivo(u.getActivo());
            return formulario(model, f);
        } catch (NegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/usuarios";
        }
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("usuarioForm") UsuarioForm form,
                        BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return formulario(model, form);
        }
        try {
            usuarioService.crear(form);
            ra.addFlashAttribute("ok", "Usuario creado correctamente.");
            return "redirect:/usuarios";
        } catch (NegocioException e) {
            model.addAttribute("error", e.getMessage());
            return formulario(model, form);
        }
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Integer id,
                             @Valid @ModelAttribute("usuarioForm") UsuarioForm form,
                             BindingResult result, Principal principal,
                             Model model, RedirectAttributes ra) {
        form.setId(id);
        if (result.hasErrors()) {
            return formulario(model, form);
        }
        try {
            usuarioService.actualizar(id, form, principal.getName());
            ra.addFlashAttribute("ok", "Usuario actualizado correctamente.");
            return "redirect:/usuarios";
        } catch (NegocioException e) {
            model.addAttribute("error", e.getMessage());
            return formulario(model, form);
        }
    }

    @PostMapping("/{id}/estado")
    public String cambiarEstado(@PathVariable Integer id, Principal principal, RedirectAttributes ra) {
        try {
            UsuarioSistema u = usuarioService.cambiarEstado(id, principal.getName());
            ra.addFlashAttribute("ok", "Usuario " + u.getUsername() + (u.getActivo() ? " activado." : " desactivado."));
        } catch (NegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/usuarios";
    }

    private String formulario(Model model, UsuarioForm form) {
        model.addAttribute("usuarioForm", form);
        model.addAttribute("roles", usuarioService.roles());
        return "usuario-form";
    }
}
