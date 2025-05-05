package com.example.Evaluación_Final_Transversal.Controller;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.Evaluación_Final_Transversal.Model.Evento;
import com.example.Evaluación_Final_Transversal.Model.Participante;
import com.example.Evaluación_Final_Transversal.Repository.ParticipanteRepository;
import com.example.Evaluación_Final_Transversal.Service.EventoService;

import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/api/eventos")
public class EventoController {

    private final EventoService eventoService;


    // Crear evento
    @PostMapping
    public ResponseEntity<EntityModel<Evento>> crearEvento(@RequestBody Evento evento) {
        Evento nuevoEvento = eventoService.crearEvento(evento);
        EntityModel<Evento> eventoModel = EntityModel.of(nuevoEvento);
        eventoModel.add(linkTo(methodOn(EventoController.class).obtenerEvento(nuevoEvento.getId())).withSelfRel());
        eventoModel.add(linkTo(methodOn(EventoController.class).obtenerEventos()).withRel("eventos"));
        return ResponseEntity.ok(eventoModel);
    }

    // Obtener todos los eventos
    @GetMapping
    public ResponseEntity<List<EntityModel<Evento>>> obtenerEventos() {
        List<EntityModel<Evento>> eventoModels = eventoService.obtenerEventos().stream()
                .map(evento -> {
                    EntityModel<Evento> model = EntityModel.of(evento);
                    model.add(linkTo(methodOn(EventoController.class).obtenerEvento(evento.getId())).withSelfRel());
                    return model;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventoModels);
    }

    // Obtener evento por ID
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Evento>> obtenerEvento(@PathVariable Long id) {
        try {
            Evento evento = eventoService.obtenerEventoPorId(id);
            EntityModel<Evento> eventoModel = EntityModel.of(evento);
            eventoModel.add(linkTo(methodOn(EventoController.class).obtenerEvento(id)).withSelfRel());
            eventoModel.add(linkTo(methodOn(EventoController.class).obtenerEventos()).withRel("eventos"));
            eventoModel.add(linkTo(methodOn(EventoController.class).obtenerParticipantesPorEvento(id)).withRel("participantes"));
            return ResponseEntity.ok(eventoModel);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Actualizar evento
    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Evento>> actualizarEvento(@PathVariable Long id, @RequestBody Evento evento) {
        try {
            Evento eventoActualizado = eventoService.actualizarEvento(id, evento);
            EntityModel<Evento> eventoModel = EntityModel.of(eventoActualizado);
            eventoModel.add(linkTo(methodOn(EventoController.class).obtenerEvento(id)).withSelfRel());
            eventoModel.add(linkTo(methodOn(EventoController.class).obtenerEventos()).withRel("eventos"));
            eventoModel.add(linkTo(methodOn(EventoController.class).obtenerParticipantesPorEvento(id)).withRel("participantes"));
            return ResponseEntity.ok(eventoModel);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar evento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEvento(@PathVariable Long id) {
        try {
            eventoService.eliminarEvento(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Inscribir participante a un evento
    @PostMapping("/{eventoId}/participantes")
    public ResponseEntity<EntityModel<Participante>> inscribirParticipante(@PathVariable Long eventoId, @RequestBody Participante participante) {
        Participante participanteInscrito = eventoService.inscribirParticipante(eventoId, participante);
        EntityModel<Participante> participanteModel = EntityModel.of(participanteInscrito);
        participanteModel.add(linkTo(methodOn(EventoController.class).obtenerParticipante(participanteInscrito.getId())).withSelfRel());
        participanteModel.add(linkTo(methodOn(EventoController.class).obtenerParticipantesPorEvento(eventoId)).withRel("participantes"));
        return ResponseEntity.ok(participanteModel);
    }

    // Obtener participantes por evento
    @GetMapping("/{eventoId}/participantes")
    public ResponseEntity<List<EntityModel<Participante>>> obtenerParticipantesPorEvento(@PathVariable Long eventoId) {
        List<EntityModel<Participante>> participanteModels = eventoService.obtenerParticipantesPorEvento(eventoId).stream()
                .map(participante -> {
                    EntityModel<Participante> model = EntityModel.of(participante);
                    model.add(linkTo(methodOn(EventoController.class).obtenerParticipante(participante.getId())).withSelfRel());
                    return model;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(participanteModels);
    }

    // Obtener participante por ID
    @GetMapping("/participantes/{id}")
    public ResponseEntity<EntityModel<Participante>> obtenerParticipante(@PathVariable Long id) {
        try {
            Participante participante = participanteRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Participante no encontrado con ID: " + id));
            EntityModel<Participante> participanteModel = EntityModel.of(participante);
            participanteModel.add(linkTo(methodOn(EventoController.class).obtenerParticipante(id)).withSelfRel());
            return ResponseEntity.ok(participanteModel);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }


    // Actualizar participante
    @PutMapping("/participantes/{id}")
    public ResponseEntity<EntityModel<Participante>> actualizarParticipante(@PathVariable Long id, @RequestBody Participante participante) {
        try {
            Participante participanteActualizado = eventoService.actualizarParticipante(id, participante);
            EntityModel<Participante> participanteModel = EntityModel.of(participanteActualizado);
            participanteModel.add(linkTo(methodOn(EventoController.class).obtenerParticipante(id)).withSelfRel());
            return ResponseEntity.ok(participanteModel);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar participante
    @DeleteMapping("/participantes/{id}")
    public ResponseEntity<Void> eliminarParticipante(@PathVariable Long id) {
        try {
            eventoService.eliminarParticipante(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Necesitas inyectar el repositorio de Participante
    private final ParticipanteRepository participanteRepository;

    public EventoController(EventoService eventoService, ParticipanteRepository participanteRepository) {
        this.eventoService = eventoService;
        this.participanteRepository = participanteRepository;
    }
}