package com.autoscolombia.parqueadero.controller;

import com.autoscolombia.parqueadero.dto.ClienteForm;
import com.autoscolombia.parqueadero.dto.MensualidadForm;
import com.autoscolombia.parqueadero.dto.VehiculoForm;
import com.autoscolombia.parqueadero.exception.NegocioException;
import com.autoscolombia.parqueadero.model.Cliente;
import com.autoscolombia.parqueadero.service.ClienteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
@RequestMapping("/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public String listar(@RequestParam(required = false) String buscar, Model model) {
        model.addAttribute("clientes", clienteService.listar(buscar));
        model.addAttribute("buscar", buscar);
        return "clientes";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("clienteForm", new ClienteForm());
        return "cliente-form";
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("clienteForm") ClienteForm form,
                        BindingResult result, Model model, RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "cliente-form";
        }
        try {
            Cliente c = clienteService.crear(form);
            ra.addFlashAttribute("ok", "Cliente registrado. Ahora agregue su vehículo y su mensualidad.");
            return "redirect:/clientes/" + c.getIdCliente();
        } catch (NegocioException e) {
            model.addAttribute("error", e.getMessage());
            return "cliente-form";
        }
    }

    @GetMapping("/{id}")
    public String detalle(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            model.addAttribute("cliente", clienteService.obtener(id));
            model.addAttribute("vehiculos", clienteService.vehiculosDe(id));
            model.addAttribute("mensualidades", clienteService.mensualidadesDe(id));
            model.addAttribute("tipos", clienteService.tiposVehiculo());
            if (!model.containsAttribute("vehiculoForm")) {
                model.addAttribute("vehiculoForm", new VehiculoForm());
            }
            if (!model.containsAttribute("mensualidadForm")) {
                MensualidadForm m = new MensualidadForm();
                m.setFechaInicio(LocalDate.now());
                m.setFechaFin(LocalDate.now().plusDays(30));
                model.addAttribute("mensualidadForm", m);
            }
            return "cliente-detalle";
        } catch (NegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes";
        }
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Integer id, Model model, RedirectAttributes ra) {
        try {
            Cliente c = clienteService.obtener(id);
            ClienteForm f = new ClienteForm();
            f.setId(c.getIdCliente());
            f.setTipoDocumento(c.getTipoDocumento());
            f.setDocumento(c.getDocumento());
            f.setNombres(c.getNombres());
            f.setApellidos(c.getApellidos());
            f.setTelefono(c.getTelefono());
            f.setEmail(c.getEmail());
            model.addAttribute("clienteForm", f);
            return "cliente-form";
        } catch (NegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
            return "redirect:/clientes";
        }
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Integer id,
                             @Valid @ModelAttribute("clienteForm") ClienteForm form,
                             BindingResult result, Model model, RedirectAttributes ra) {
        form.setId(id);
        if (result.hasErrors()) {
            return "cliente-form";
        }
        try {
            clienteService.actualizar(id, form);
            ra.addFlashAttribute("ok", "Cliente actualizado correctamente.");
            return "redirect:/clientes/" + id;
        } catch (NegocioException e) {
            model.addAttribute("error", e.getMessage());
            return "cliente-form";
        }
    }

    @PostMapping("/{id}/vehiculos")
    public String agregarVehiculo(@PathVariable Integer id,
                                  @Valid @ModelAttribute("vehiculoForm") VehiculoForm form,
                                  BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("error", result.getAllErrors().get(0).getDefaultMessage());
            return "redirect:/clientes/" + id;
        }
        try {
            clienteService.agregarVehiculo(id, form);
            ra.addFlashAttribute("ok", "Vehículo registrado correctamente.");
        } catch (NegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/clientes/" + id;
    }

    @PostMapping("/{id}/mensualidades")
    public String agregarMensualidad(@PathVariable Integer id,
                                     @Valid @ModelAttribute("mensualidadForm") MensualidadForm form,
                                     BindingResult result, RedirectAttributes ra) {
        if (result.hasErrors()) {
            ra.addFlashAttribute("error", "Verifique las fechas de la mensualidad.");
            return "redirect:/clientes/" + id;
        }
        try {
            clienteService.agregarMensualidad(id, form);
            ra.addFlashAttribute("ok", "Mensualidad registrada correctamente.");
        } catch (NegocioException e) {
            ra.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/clientes/" + id;
    }
}
