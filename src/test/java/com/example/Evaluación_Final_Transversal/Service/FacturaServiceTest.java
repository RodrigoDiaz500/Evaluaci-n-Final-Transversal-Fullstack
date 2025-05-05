package com.example.Evaluación_Final_Transversal.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.Evaluación_Final_Transversal.Model.Factura;
import com.example.Evaluación_Final_Transversal.Model.Servicio;
import com.example.Evaluación_Final_Transversal.Repository.FacturaRepository;
import com.example.Evaluación_Final_Transversal.Repository.ServicioRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Collections;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FacturaServiceTest {

    @Mock
    private ServicioRepository servicioRepository;

    @Mock
    private FacturaRepository facturaRepository;

    @InjectMocks
    private FacturaService facturaService;

    private Servicio servicio1;
    private Servicio servicio2;
    private Factura factura1;

    @BeforeEach
    void setUp() {
        servicio1 = new Servicio();
        servicio1.setId(1L);
        servicio1.setNombre("Consulta Veterinaria");
        servicio1.setDescripcion("Consulta general.");
        servicio1.setPrecio(50.0);

        servicio2 = new Servicio();
        servicio2.setId(2L);
        servicio2.setNombre("Vacunación");
        servicio2.setDescripcion("Vacuna antirrábica.");
        servicio2.setPrecio(30.0);

        factura1 = new Factura();
        factura1.setId(100L);
        factura1.setTotal(80.0);
        factura1.setEstado(Factura.EstadoFactura.PENDIENTE);
        factura1.setServicios(Arrays.asList(servicio1, servicio2));
    }

    @Test
    void crearServicio_deberiaGuardarYRetornarServicio() {
        when(servicioRepository.save(any(Servicio.class))).thenReturn(servicio1);
        Servicio nuevoServicio = facturaService.crearServicio(new Servicio());
        assertNotNull(nuevoServicio.getId());
        assertEquals(servicio1.getNombre(), nuevoServicio.getNombre());
        verify(servicioRepository, times(1)).save(any(Servicio.class));
    }

    @Test
    void obtenerServicios_deberiaRetornarListaDeServicios() {
        when(servicioRepository.findAll()).thenReturn(Arrays.asList(servicio1, servicio2));
        List<Servicio> servicios = facturaService.obtenerServicios();
        assertEquals(2, servicios.size());
        assertEquals(servicio1.getNombre(), servicios.get(0).getNombre());
        assertEquals(servicio2.getNombre(), servicios.get(1).getNombre());
        verify(servicioRepository, times(1)).findAll();
    }

    @Test
    void crearFactura_conIdsServiciosNoValidos_deberiaLanzarIllegalArgumentException() {
        List<Long> idsServicios = Arrays.asList(99L, 100L); // IDs que no existen
        when(servicioRepository.findAllById(idsServicios)).thenReturn(Collections.emptyList());
        assertThrows(IllegalArgumentException.class, () -> facturaService.crearFactura(idsServicios));
        verify(servicioRepository, times(1)).findAllById(idsServicios);
        verify(facturaRepository, never()).save(any(Factura.class)); // Aseguramos que no se guardó ninguna factura
    }
    

    @Test
    void pagarFactura_conIdExistente_deberiaActualizarEstadoAPagada() {
        Long facturaId = 100L;
        Factura facturaPendiente = new Factura();
        facturaPendiente.setId(facturaId);
        facturaPendiente.setEstado(Factura.EstadoFactura.PENDIENTE);
        Factura facturaPagada = new Factura();
        facturaPagada.setId(facturaId);
        facturaPagada.setEstado(Factura.EstadoFactura.PAGADA);
        when(facturaRepository.findById(facturaId)).thenReturn(Optional.of(facturaPendiente));
        when(facturaRepository.save(any(Factura.class))).thenReturn(facturaPagada);
        Factura resultado = facturaService.pagarFactura(facturaId);
        assertEquals(Factura.EstadoFactura.PAGADA, resultado.getEstado());
        verify(facturaRepository, times(1)).findById(facturaId);
        verify(facturaRepository, times(1)).save(any(Factura.class));
    }



}