package com.example.Evaluación_Final_Transversal.Service;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.Evaluación_Final_Transversal.Model.Evento;
import com.example.Evaluación_Final_Transversal.Model.Participante;
import com.example.Evaluación_Final_Transversal.Repository.EventoRepository;
import com.example.Evaluación_Final_Transversal.Repository.ParticipanteRepository;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EventoServiceTest {

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private ParticipanteRepository participanteRepository;

    @InjectMocks
    private EventoService eventoService;

    private Evento evento1;
    private Evento evento2;
    private Participante participante1;

    @BeforeEach
    void setUp() {
        evento1 = new Evento();
        evento1.setId(1L);
        evento1.setNombre("Feria de Mascotas");
        evento1.setTipo("Feria");
        evento1.setLugar("Parque Central");
        evento1.setFecha(LocalDate.now());
        evento1.setDescripcion("Evento para amantes de las mascotas.");

        evento2 = new Evento();
        evento2.setId(2L);
        evento2.setNombre("Competencia Canina");
        evento2.setTipo("Competencia");
        evento2.setLugar("Estadio Municipal");
        evento2.setFecha(LocalDate.now().plusDays(7));
        evento2.setDescripcion("Competencia de habilidades caninas.");

        participante1 = new Participante();
        participante1.setId(1L);
        participante1.setNombre("Juan Pérez");
        participante1.setTipoMascota("Perro");
        participante1.setNombreMascota("Firulais");
    }

    @Test
    void crearEvento_deberiaGuardarYRetornarEvento() {
        when(eventoRepository.save(any(Evento.class))).thenReturn(evento1);
        Evento nuevoEvento = eventoService.crearEvento(new Evento());
        assertNotNull(nuevoEvento.getId());
        assertEquals(evento1.getNombre(), nuevoEvento.getNombre());
        verify(eventoRepository, times(1)).save(any(Evento.class));
    }

    @Test
    void obtenerEventos_deberiaRetornarListaDeEventos() {
        when(eventoRepository.findAll()).thenReturn(Arrays.asList(evento1, evento2));
        List<Evento> eventos = eventoService.obtenerEventos();
        assertEquals(2, eventos.size());
        assertEquals(evento1.getNombre(), eventos.get(0).getNombre());
        assertEquals(evento2.getNombre(), eventos.get(1).getNombre());
        verify(eventoRepository, times(1)).findAll();
    }

    @Test
    void obtenerEventoPorId_conIdExistente_deberiaRetornarEvento() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento1));
        Evento evento = eventoService.obtenerEventoPorId(1L);
        assertNotNull(evento);
        assertEquals(evento1.getNombre(), evento.getNombre());
        verify(eventoRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerEventoPorId_conIdNoExistente_deberiaLanzarEntityNotFoundException() {
        when(eventoRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> eventoService.obtenerEventoPorId(99L));
        verify(eventoRepository, times(1)).findById(99L);
    }

    @Test
    void inscribirParticipante_deberiaGuardarParticipanteYAsociarloAlEvento() {
        when(eventoRepository.findById(1L)).thenReturn(Optional.of(evento1));
        when(participanteRepository.save(any(Participante.class))).thenReturn(participante1);
        ArgumentCaptor<Participante> participanteCaptor = ArgumentCaptor.forClass(Participante.class);
        EventoService eventoService = new EventoService(eventoRepository, participanteRepository);
        Participante participanteInscrito = eventoService.inscribirParticipante(1L, new Participante());
        assertNotNull(participanteInscrito.getId());
        assertEquals(participante1.getNombre(), participanteInscrito.getNombre());
        verify(participanteRepository, times(1)).save(participanteCaptor.capture());
        Participante participanteGuardado = participanteCaptor.getValue();
        assertEquals(evento1, participanteGuardado.getEvento());
        verify(eventoRepository, times(1)).findById(1L);
    }
}