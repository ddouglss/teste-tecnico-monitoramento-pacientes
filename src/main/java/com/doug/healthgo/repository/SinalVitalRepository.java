package com.doug.healthgo.repository;

import com.doug.healthgo.entity.SinalVital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SinalVitalRepository extends JpaRepository<SinalVital, Long> {
    List<SinalVital> findByPacienteIdOrderByTimestampAsc(String patientId);
}
