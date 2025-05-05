package com.example.Evaluación_Final_Transversal.Model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonFormat;  // Importar JsonFormat
import java.util.Date;
import java.util.List;

@Entity
public class Factura {

    public enum EstadoFactura {
        PENDIENTE,
        PAGADA
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")  // Formato de fecha
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaEmision;

    private double total;

    @Enumerated(EnumType.STRING)
    private EstadoFactura estado;

    @ManyToMany
    @JoinTable(
        name = "factura_servicio",
        joinColumns = @JoinColumn(name = "factura_id"),
        inverseJoinColumns = @JoinColumn(name = "servicio_id")
    )
    @JsonManagedReference
    private List<Servicio> servicios;

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaEmision() {
        return fechaEmision;
    }

    public void setFechaEmision(Date fechaEmision) {
        this.fechaEmision = fechaEmision;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public EstadoFactura getEstado() {
        return estado;
    }

    public void setEstado(EstadoFactura estado) {
        this.estado = estado;
    }

    public List<Servicio> getServicios() {
        return servicios;
    }

    public void setServicios(List<Servicio> servicios) {
        this.servicios = servicios;
    }
}
