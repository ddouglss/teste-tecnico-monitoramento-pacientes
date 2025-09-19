package com.doug.healthgo.service;

import com.doug.healthgo.entity.SinalVital;
import com.doug.healthgo.repository.SinalVitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SinalVitalService {
    @Autowired
    private SinalVitalRepository repository;

    //formato padrão utilizado, pode ser alterado conforme origem dos dados.
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy HH:mm";
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT);

    //Upload de arquivo CSV de sinais vitais, faz as validações dos dados...
    public int uploadCsv(MultipartFile file, String dateFormt) throws IOException {
        BufferedReader render = new BufferedReader(new InputStreamReader(file.getInputStream()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormt != null ? dateFormt : DEFAULT_DATE_FORMAT);

        String line;
        int count = 0;
        int lineNumber = 1;
        List<SinalVital> batch = new ArrayList<>();
        render.readLine();

        while ((line = render.readLine()) != null) {
            String[] values = line.split(",");
            try{
                //Valida quantidade de colunas
                if(values.length < 11) throw new IllegalArgumentException("Linha incompleta");

                LocalDateTime timestamp = LocalDateTime.parse(values[0], formatter);
                Double hr = tryParseDouble(values[4]);
                Double spo2 = tryParseDouble(values[5]);
                Double pressaoSys = tryParseDouble(values[6]);
                Double pressaoDia = tryParseDouble(values[7]);
                Double temp = tryParseDouble(values[8]);
                Double resFreq = tryParseDouble(values[9]);
                String status = values[10];

                SinalVital v = new SinalVital(
                        null,
                        values[1], values[2], values[3], timestamp,
                        hr, spo2,pressaoSys, pressaoDia, temp, resFreq, status);
                batch.add(v);
                count++;
            }
            catch(Exception e){
               System.err.println("Erro ao processar linha " + lineNumber + ":" + line);
               System.err.println("Detalhe:" + e.getMessage());
            }
            lineNumber++;
        }
        repository.saveAll(batch);
        return count;
    }

    //Função auxiliar para o "parse" seguro de double
    private Double tryParseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            System.err.println("Erro ao converter para double: " + value);
            return null;
        }
    }

    //Busca todos os dados ordenados de um paciente.
    public List<SinalVital> getPacienteData(String pacienteId) {
        return repository.findByPacienteIdOrderByTimestampAsc(pacienteId);
    }

    //Busca dados de um paciente filtrados por intervalo de timestamp.
    public List<SinalVital> getPacienteDataByInterval(String pacienteId, String startTimestamp, String endTimestamp, String dateFormat) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat != null ? dateFormat : DEFAULT_DATE_FORMAT);
        LocalDateTime formatStart = LocalDateTime.parse(startTimestamp, formatter);
        LocalDateTime formatEnd = LocalDateTime.parse(endTimestamp, formatter);
        List<SinalVital> all = getPacienteData(pacienteId);
        return all.stream()
                .filter(v -> v.getTimestamp() != null &&
                        !v.getTimestamp().isBefore(formatStart) &&
                        !v.getTimestamp().isAfter(formatEnd))
                .collect(Collectors.toList());
    }

    //Exporta lista de sinais vitais para CSV
    public ByteArrayInputStream exportToCsv(List<SinalVital> data, String dateFormat) {
        final String HEADER = "timestamp,paciente_id,paciente_nome,paciente_cpf,hr,spo2,pressao_sys,pressao_dia,temp,resp_freq,status";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat != null ? dateFormat : DEFAULT_DATE_FORMAT);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(out);
        writer.println(HEADER);

        for (SinalVital values : data) {
            writer.println(String.join(",",
                    values.getTimestamp() != null ? values.getTimestamp().format(formatter) : "",
                    values.getPacienteId(),
                    values.getPacienteNome(),
                    values.getPacienteCpf(),
                    safeToString(values.getHr()),
                    safeToString(values.getSpo2()),
                    safeToString(values.getPressaoSys()),
                    safeToString(values.getPressaoDia()),
                    safeToString(values.getTemp()),
                    safeToString(values.getRespFreq()),
                    values.getStatus()
            ));
        }
        writer.flush();
        return new ByteArrayInputStream(out.toByteArray());
    }
    //Função auxiliar para o "String.valueOf" seguro de double
    private String safeToString(Double value){
        return value != null ? String.valueOf(value) : "";
    }

    public String exportToJson(List<SinalVital> data) {
        StringBuilder json = new StringBuilder("[");
        for(int i = 0; i < data.size(); i++){
            SinalVital sinal = data.get(i);
            json.append("{")
                    .append("\"timestemp\":\"").append(sinal.getTimestamp()).append("\",")
                    .append("\"pacienteId\":\"").append(sinal.getPacienteId()).append("\",")
                    .append("\"pacienteNome\":\"").append(sinal.getPacienteNome()).append("\",")
                    .append("\"pacienteCpf\":\"").append(sinal.getPacienteCpf()).append("\",")
                    .append("\"hr\":\"").append(sinal.getHr()).append("\",")
                    .append("\"spo2\":\"").append(sinal.getSpo2()).append("\",")
                    .append("\"pressaoSys\":\"").append(sinal.getPressaoSys()).append("\",")
                    .append("\"pressaoDia\":\"").append(sinal.getPressaoDia()).append("\",")
                    .append("\"temp\":\"").append(sinal.getTemp()).append("\",")
                    .append("\"respFreq\":\"").append(sinal.getRespFreq()).append("\",")
                    .append("\"status\":\"").append(sinal.getStatus()).append("\"")
                    .append("}");
            if(i < data.size() - 1) json.append(",");
        }
        json.append("]");
        return json.toString();
    }

    //Download completo em CSV do paciente
    public ByteArrayInputStream downloadPacienteCsv(String pacienteId, String dateFormat) {
        List<SinalVital> data = getPacienteData(pacienteId);
        return exportToCsv(data, dateFormat);
    }

    //Download dos dados filtrados em CSV
    public ByteArrayInputStream downloadPacienteCsvInterval(String pacienteId, String startTimestamp, String endTimestamp, String dateFormat) {
        List<SinalVital> filtered = getPacienteDataByInterval(pacienteId, startTimestamp, endTimestamp, dateFormat);
        return exportToCsv(filtered, dateFormat);
    }
}