package com.doug.teste_tecnico.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "sinal_vital")
public class SinalVital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "paciente_id", nullable = false, length = 50)
    private String pacienteId;

    @Column(name = "paciente_nome", nullable = false, length = 100)
    private String pacienteNome;

    @Column(name = "paciente_cpf", nullable = false, length = 14, unique = true)
    private String pacienteCpf;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    @Column(name = "hr")
    private Double hr;

    @Column(name = "spo2")
    private Double spo2;

    @Column(name = "pressao_sys")
    private Double pressaoSys;

    @Column(name = "pressao_dia")
    private Double pressaoDia;

    @Column(name = "temp")
    private Double temp;

    @Column(name = "resp_freq")
    private Double respFreq;

    @Column(name = "status", length = 50)
    private String status;
}
