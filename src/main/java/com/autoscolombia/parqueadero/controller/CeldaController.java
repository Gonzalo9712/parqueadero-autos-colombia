package com.autoscolombia.parqueadero.controller;

import com.autoscolombia.parqueadero.dto.CeldaForm;
import com.autoscolombia.parqueadero.exception.NegocioException;
import com.autoscolombia.parqueadero.model.Celda;
import com.autoscolombia.parqueadero.service.CeldaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/celdas")
@RequiredArgsConstructor
public class CeldaController {

    private final CeldaService celdaService;

    @GetMapping
    public String listar(Model model) {
        cargarListado(model);
        if (!model.containsAttribute("celdaForm")) {
            model.addAttribute("celdaForm", new CeldaForm());
        }
        return "celdas";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("celdaForm") CeldaForm form,
                        BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            cargarListado(model);
            return "celdas";
        }
        try {
            celdaService.crear(form);
            ra.addFlashAttribute("ok", "Celda creada correctamente.");
            return "redirect:/celdas";
        } catch (NegocioException e) {
            cargarListado(model);
            model.addAttribute("error", e.getMessage());
            return "celdas";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            Celda c = celdaService.obtener(id);
            CeldaForm f = new CeldaForm();
            f.setId(c.getIdCelda());
            f.setCodigo(c.getCodigo());
            f.setIdTipo(c.getTipo().getIdTipo());
            f.setEstado(c.getEstado());
            return formulario(model, f);
        } catch (NegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/celdas";
        }
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Integer id,
                             @Valid @ModelAttribute("celdaForm") CeldaForm form,
                             BindingResult result, Model model, RedirectAttributes ra) {
        form.setId(id);
        if (result.hasErrors()) {
            return formulario(model, form);
        }
        try {
            celdaService.actualizar(id, form);
            ra.addFlashAttribute("ok", "Celda actualizada correctamente.");
            return "redirect:/celdas";
        } catch (NegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/celdas";
        }
    }

    private String formulario(Model model, CeldaForm form) {
        model.addAttribute("celdaForm", form);
        model.addAttribute("tipos", celdaService.tiposVehiculo());
        return "celda-form";
    }

    /** RF-20 y RF-24: listado de celdas y resumen de ocupación. */
    private void cargarListado(Model model) {
        List<Celda> celdas = celdaService.listar();
        model.addAttribute("celdas", celdas);
        model.addAttribute("tipos", celdaService.tiposVehiculo());
        model.addAttribute("total", celdas.size());
        model.addAttribute("disponibles", celdas.stream().filter(c -> "DISPONIBLE".equals(c.getEstado())).count());
        model.addAttribute("ocupadas", celdas.stream().filter(c -> "OCUPADA".equals(c.getEstado())).count());
        model.addAttribute("inactivas", celdas.stream().filter(c -> "INACTIVA".equals(c.getEstado())).count());
    }
}
