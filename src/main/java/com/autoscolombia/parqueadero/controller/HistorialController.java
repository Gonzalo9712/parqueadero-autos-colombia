package com.autoscolombia.parqueadero.controller;

import com.autoscolombia.parqueadero.service.AccesoService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;

@Controller
@RequiredArgsConstructor
public class HistorialController {

    private final AccesoService accesoService;

    @GetMapping("/historial")
    public String historial(@RequestParam(required = false) String placa,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate desde,
                            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate hasta,
                            Model model) {
        model.addAttribute("registros", accesoService.consultarHistorial(placa, desde, hasta));
        model.addAttribute("placa", placa);
        model.addAttribute("desde", desde);
        model.addAttribute("hasta", hasta);
        return "historial";
    }
}
