package com.autoscolombia.parqueadero.controller;

import com.autoscolombia.parqueadero.repository.ComprobanteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class ComprobanteController {

    private final ComprobanteRepository comprobanteRepository;

    @GetMapping("/comprobantes/{id}")
    public String ver(@PathVariable Integer id, Model model) {
        model.addAttribute("comprobante", comprobanteRepository.findById(id).orElse(null));
        return "comprobante";
    }
}
