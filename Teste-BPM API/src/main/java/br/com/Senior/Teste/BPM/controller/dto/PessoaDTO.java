package br.com.Senior.Teste.BPM.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PessoaDTO {
    
    private Long id;
    private String nome;
    private String documento;
    private String telefone;
}
