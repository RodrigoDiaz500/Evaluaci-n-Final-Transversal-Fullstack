package com.example.Evaluación_Final_Transversal.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Evaluación_Final_Transversal.Model.Participante;

@Repository
public interface ParticipanteRepository extends JpaRepository<Participante, Long> {
}
