package com.autoscolombia.parqueadero.controller;

import com.autoscolombia.parqueadero.exception.NegocioException;
import com.autoscolombia.parqueadero.model.Comprobante;
import com.autoscolombia.parqueadero.service.AccesoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class AccesoController {

    private final AccesoService accesoService;

    @GetMapping("/")
    public String inicio() {
        return "redirect:/accesos";
    }

    @GetMapping("/accesos")
    public String listar(@RequestParam(required = false) String buscar, Model model) {
        model.addAttribute("vehiculos", accesoService.listarVehiculosAdentro(buscar));
        model.addAttribute("buscar", buscar);
        return "accesos";
    }

    @PostMapping("/accesos/entrada")
    public String entrada(@RequestParam String placa, Principal principal, RedirectAttributes ra) {
        try {
            Comprobante c = accesoService.registrarEntrada(placa, principal.getName());
            return "redirect:/comprobantes/" + c.getIdComprobante();
        } catch (NegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/accesos";
        }
    }

    @PostMapping("/accesos/salida")
    public String salida(@RequestParam String placa, Principal principal, RedirectAttributes ra) {
        try {
            Comprobante c = accesoService.registrarSalida(placa, principal.getName());
            return "redirect:/comprobantes/" + c.getIdComprobante();
        } catch (NegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/accesos";
        }
    }
}
