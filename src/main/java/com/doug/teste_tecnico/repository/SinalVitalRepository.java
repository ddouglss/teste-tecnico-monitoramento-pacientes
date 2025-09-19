package com.doug.teste_tecnico.repository;

import com.doug.teste_tecnico.entity.SinalVital;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SinalVitalRepository extends JpaRepository<SinalVital, Long> {
    List<SinalVital> findAllByStatus(String patientId);
}
