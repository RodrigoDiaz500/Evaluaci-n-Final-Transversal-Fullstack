package com.example.Evaluación_Final_Transversal.Model;

import java.util.List;

public class FacturaRequest {

    private List<Long> servicios; // Lista de IDs de los servicios

    public List<Long> getServicios() {
        return servicios;
    }

    public void setServicios(List<Long> servicios) {
        this.servicios = servicios;
    }
}
