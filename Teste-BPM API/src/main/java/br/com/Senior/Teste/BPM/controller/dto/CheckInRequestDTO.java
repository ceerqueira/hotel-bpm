package br.com.Senior.Teste.BPM.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInRequestDTO {
    
    private Long pessoaId;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaidaPrevista;
    private Boolean adicionalVeiculo;
}
