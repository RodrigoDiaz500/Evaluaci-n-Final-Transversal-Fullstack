package com.example.Evaluación_Final_Transversal.Service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.Evaluación_Final_Transversal.Model.*;
import com.example.Evaluación_Final_Transversal.Model.Factura.EstadoFactura;
import com.example.Evaluación_Final_Transversal.Repository.FacturaRepository;
import com.example.Evaluación_Final_Transversal.Repository.ServicioRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class FacturaService {

    private final ServicioRepository servicioRepository;
    private final FacturaRepository facturaRepository;

    public FacturaService(ServicioRepository servicioRepository, FacturaRepository facturaRepository) {
        this.servicioRepository = servicioRepository;
        this.facturaRepository = facturaRepository;
    }

    public Servicio crearServicio(Servicio servicio) {
        return servicioRepository.save(servicio);
    }

    public List<Servicio> obtenerServicios() {
        return servicioRepository.findAll();
    }

    public Optional<Servicio> obtenerServicioPorId(Long id) {
        return servicioRepository.findById(id);
    }
    
    public boolean eliminarServicio(Long id) {
        if (servicioRepository.existsById(id)) {
            servicioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public Factura crearFactura(List<Long> idsServicios) {
        if (idsServicios == null || idsServicios.isEmpty()) {
            throw new IllegalArgumentException("La factura debe contener al menos un servicio.");
        }

        // Buscar los servicios desde la base de datos usando los IDs proporcionados
        List<Servicio> servicios = servicioRepository.findAllById(idsServicios);

        if (servicios.isEmpty()) {
            throw new IllegalArgumentException("Ningún servicio válido fue encontrado con los IDs proporcionados.");
        }

        // Calcular el total de la factura sumando los precios de los servicios
        double total = servicios.stream().mapToDouble(Servicio::getPrecio).sum();

        // Crear la nueva factura
        Factura factura = new Factura();
        factura.setFechaEmision(new Date());
        factura.setTotal(total);
        factura.setEstado(EstadoFactura.PENDIENTE);
        factura.setServicios(servicios);

        return facturaRepository.save(factura);
    }

    public List<Factura> obtenerFacturas() {
        return facturaRepository.findAll();
    }

    public Optional<Factura> obtenerFacturaPorId(Long id) {
        return facturaRepository.findById(id);
    }

    @Transactional
    public Factura pagarFactura(Long id) {
        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Factura no encontrada con ID: " + id));

        factura.setEstado(EstadoFactura.PAGADA);
        return facturaRepository.save(factura);
    }

    public void eliminarFactura(Long id) {
        if (!facturaRepository.existsById(id)) {
            throw new EntityNotFoundException("Factura no encontrada con ID: " + id);
        }
        facturaRepository.deleteById(id);
    }
}
