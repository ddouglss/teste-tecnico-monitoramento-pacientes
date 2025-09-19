package com.doug.healthgo.controller;

import com.doug.healthgo.entity.SinalVital;
import com.doug.healthgo.service.SinalVitalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

@RestController
@RequestMapping("/api/sinais-vitais")
public class SinalVitalController {

    @Autowired
    private SinalVitalService service;

    //Responsavel por fazer o upload de um arquivo CSV de sinais vitais.
    @PostMapping("/upload")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "dataFormat", required = false) String dateFormat) {
        try {
            int count = service.uploadCsv(file,dateFormat);
            return ResponseEntity.ok("Upload realizado com sucesso! Registros processados: " + count);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erro no upload: " + e.getMessage());
        }
    }

    //Responsavel por listar todos os dados de sinais vitais de um paciente, ordenados por timestamp
    @GetMapping("/{pacienteId}")
    public ResponseEntity<List<SinalVital>> getPacienteData(@PathVariable String pacienteId){
        List<SinalVital> data = service.getPacienteData(pacienteId);
        if (data.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }

    //Responsavel por lista dados de um paciente filtrados por intervalo de tempo.
    @GetMapping("/{pacienteId}/intervalo")
    public ResponseEntity<List<SinalVital>> getPacienteDataInterval(@PathVariable String pacienteId, @RequestParam String startTimestamp, @RequestParam String endTimestamp,
                                                                    @RequestParam(value = "dataFormat", required = false) String dateFormat){
        List<SinalVital> data = service.getPacienteDataByInterval(pacienteId, startTimestamp, endTimestamp, dateFormat);
        if (data.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(data);
    }

    // Responsavel por fazer o download dos dados completos de um paciente em CSV.
    @GetMapping("/{pacienteId}/download")
    public ResponseEntity<byte[]> downloadPacienteCsv(@PathVariable String pacienteId, @RequestParam(value = "dataFormat", required = false) String dateFormat){
        ByteArrayInputStream csvStrem = service.downloadPacienteCsv(pacienteId, dateFormat);
        byte[] csvBytes = csvStrem.readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pacienteId + "text/csv");

        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }

    // Reponsavel por fazer o download dos dados filtrados por intervalo em CSV.
    @GetMapping("/{pacienteId}/download-intervalo")
    public ResponseEntity<byte[]> downloadPacienteCsvInterval(@PathVariable String pacienteId,
                                                              @RequestParam String startTimestamp,
                                                              @RequestParam String endTimestamp,
                                                              @RequestParam(value = "dataFormat", required = false) String dateFormat){
        ByteArrayInputStream csvStream = service.downloadPacienteCsvInterval(pacienteId, startTimestamp, endTimestamp, dateFormat);
        byte[] csvBytes = csvStream.readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + pacienteId + "_intervalo.csv");
        headers.add(HttpHeaders.CONTENT_TYPE, "text/csv");

        return ResponseEntity.ok().headers(headers).body(csvBytes);
    }
}
