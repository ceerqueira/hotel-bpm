package br.com.Senior.Teste.BPM.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInDTO {
    
    private Long id;
    private PessoaDTO pessoa;
    private LocalDateTime dataEntrada;
    private LocalDateTime dataSaidaPrevista;
    private Boolean adicionalVeiculo;
    private BigDecimal valorTotal;
}
