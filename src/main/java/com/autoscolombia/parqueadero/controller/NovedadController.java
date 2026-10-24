package com.autoscolombia.parqueadero.controller;

import com.autoscolombia.parqueadero.dto.NovedadForm;
import com.autoscolombia.parqueadero.exception.NegocioException;
import com.autoscolombia.parqueadero.service.NovedadService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class NovedadController {

    private final NovedadService novedadService;

    @GetMapping("/novedades")
    public String ver(Model model) {
        if (!model.containsAttribute("novedadForm")) {
            model.addAttribute("novedadForm", new NovedadForm());
        }
        model.addAttribute("novedades", novedadService.ultimasNovedades());
        return "novedades";
    }

    @PostMapping("/novedades")
    public String guardar(@Valid @ModelAttribute("novedadForm") NovedadForm form,
                          BindingResult result, Principal principal,
                          Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            model.addAttribute("novedades", novedadService.ultimasNovedades());
            return "novedades";
        }
        try {
            novedadService.registrarNovedad(form, principal.getName());
            ra.addFlashAttribute("ok", "Novedad registrada correctamente.");
        } catch (NegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/novedades";
    }
}
