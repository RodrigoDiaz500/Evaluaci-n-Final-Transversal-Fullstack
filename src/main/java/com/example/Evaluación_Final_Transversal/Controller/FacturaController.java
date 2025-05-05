package com.example.Evaluación_Final_Transversal.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Evaluación_Final_Transversal.Model.Factura;
import com.example.Evaluación_Final_Transversal.Model.FacturaRequest;
import com.example.Evaluación_Final_Transversal.Model.Servicio;
import com.example.Evaluación_Final_Transversal.Service.FacturaService;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/facturas") // Modificamos la ruta base
public class FacturaController {

    @Autowired
    private FacturaService facturaService;

    // Servicios
    @PostMapping("/servicios")
    public ResponseEntity<EntityModel<Servicio>> crearServicio(@RequestBody Servicio servicio) {
        Servicio nuevoServicio = facturaService.crearServicio(servicio);
        EntityModel<Servicio> servicioModel = EntityModel.of(nuevoServicio);
        servicioModel.add(linkTo(methodOn(FacturaController.class).obtenerServicio(nuevoServicio.getId())).withSelfRel());
        servicioModel.add(linkTo(methodOn(FacturaController.class).obtenerServicios()).withRel("servicios"));
        return ResponseEntity.ok(servicioModel);
    }

    @GetMapping("/servicios")
    public ResponseEntity<List<EntityModel<Servicio>>> obtenerServicios() {
        List<EntityModel<Servicio>> servicioModels = facturaService.obtenerServicios().stream()
                .map(servicio -> {
                    EntityModel<Servicio> model = EntityModel.of(servicio);
                    model.add(linkTo(methodOn(FacturaController.class).obtenerServicio(servicio.getId())).withSelfRel());
                    return model;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(servicioModels);
    }

    @GetMapping("/servicios/{id}")
    public ResponseEntity<EntityModel<Servicio>> obtenerServicio(@PathVariable Long id) {
        return facturaService.obtenerServicioPorId(id)
                .map(servicio -> {
                    EntityModel<Servicio> servicioModel = EntityModel.of(servicio);
                    servicioModel.add(linkTo(methodOn(FacturaController.class).obtenerServicio(id)).withSelfRel());
                    servicioModel.add(linkTo(methodOn(FacturaController.class).obtenerServicios()).withRel("servicios"));
                    return ResponseEntity.ok(servicioModel);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/servicios/{id}")
    public ResponseEntity<EntityModel<Servicio>> actualizarServicio(@PathVariable Long id, @RequestBody Servicio servicioActualizado) {
        return facturaService.obtenerServicioPorId(id)
                .map(servicioExistente -> {
                    servicioExistente.setNombre(servicioActualizado.getNombre());
                    servicioExistente.setDescripcion(servicioActualizado.getDescripcion());
                    servicioExistente.setPrecio(servicioActualizado.getPrecio());
                    Servicio servicioGuardado = facturaService.crearServicio(servicioExistente);
                    EntityModel<Servicio> servicioModel = EntityModel.of(servicioGuardado);
                    servicioModel.add(linkTo(methodOn(FacturaController.class).obtenerServicio(id)).withSelfRel());
                    servicioModel.add(linkTo(methodOn(FacturaController.class).obtenerServicios()).withRel("servicios"));
                    return ResponseEntity.ok(servicioModel);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/servicios/{id}")
    public ResponseEntity<Void> eliminarServicio(@PathVariable Long id) {
        if (facturaService.eliminarServicio(id)) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Facturas
    @PostMapping
    public ResponseEntity<EntityModel<Factura>> crearFactura(@RequestBody FacturaRequest facturaRequest) {
        Factura nuevaFactura = facturaService.crearFactura(facturaRequest.getServicios());
        EntityModel<Factura> facturaModel = EntityModel.of(nuevaFactura);
        facturaModel.add(linkTo(methodOn(FacturaController.class).obtenerFactura(nuevaFactura.getId())).withSelfRel());
        facturaModel.add(linkTo(methodOn(FacturaController.class).obtenerFacturas()).withRel("facturas"));
        return ResponseEntity.ok(facturaModel);
    }

    @GetMapping
    public ResponseEntity<List<EntityModel<Factura>>> obtenerFacturas() {
        List<EntityModel<Factura>> facturaModels = facturaService.obtenerFacturas().stream()
                .map(factura -> {
                    EntityModel<Factura> model = EntityModel.of(factura);
                    model.add(linkTo(methodOn(FacturaController.class).obtenerFactura(factura.getId())).withSelfRel());
                    return model;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(facturaModels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Factura>> obtenerFactura(@PathVariable Long id) {
        return facturaService.obtenerFacturaPorId(id)
                .map(factura -> {
                    EntityModel<Factura> facturaModel = EntityModel.of(factura);
                    facturaModel.add(linkTo(methodOn(FacturaController.class).obtenerFactura(id)).withSelfRel());
                    facturaModel.add(linkTo(methodOn(FacturaController.class).obtenerFacturas()).withRel("facturas"));
                    return ResponseEntity.ok(facturaModel);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/pagar")
    public ResponseEntity<EntityModel<Factura>> pagarFactura(@PathVariable Long id) {
        try {
            Factura facturaPagada = facturaService.pagarFactura(id);
            EntityModel<Factura> facturaModel = EntityModel.of(facturaPagada);
            facturaModel.add(linkTo(methodOn(FacturaController.class).obtenerFactura(id)).withSelfRel());
            facturaModel.add(linkTo(methodOn(FacturaController.class).obtenerFacturas()).withRel("facturas"));
            return ResponseEntity.ok(facturaModel);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarFactura(@PathVariable Long id) {
        facturaService.eliminarFactura(id);
        return ResponseEntity.noContent().build();
    }
}