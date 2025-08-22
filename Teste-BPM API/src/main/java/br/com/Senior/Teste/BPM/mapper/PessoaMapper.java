package br.com.Senior.Teste.BPM.mapper;

import br.com.Senior.Teste.BPM.controller.dto.PessoaDTO;
import br.com.Senior.Teste.BPM.entity.Pessoa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PessoaMapper {

    public PessoaDTO converterDTO(Pessoa pessoa) {
        if (pessoa == null) {
            return null;
        }
        
        return new PessoaDTO(
            pessoa.getId(),
            pessoa.getNome(),
            pessoa.getDocumento(),
            pessoa.getTelefone()
        );
    }

    public Pessoa converterEntity(PessoaDTO dto) {
        if (dto == null) {
            return null;
        }
        
        return new Pessoa(
            dto.getId(),
            dto.getNome(),
            dto.getDocumento(),
            dto.getTelefone()
        );
    }
}

