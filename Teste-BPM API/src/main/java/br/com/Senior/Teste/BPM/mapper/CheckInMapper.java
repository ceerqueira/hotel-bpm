package br.com.Senior.Teste.BPM.mapper;

import br.com.Senior.Teste.BPM.controller.dto.CheckInDTO;
import br.com.Senior.Teste.BPM.entity.CheckIn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckInMapper {
    
    private final PessoaMapper pessoaMapper;

    public CheckInDTO converterDTO(CheckIn checkIn) {
        if (checkIn == null) {
            return null;
        }
        
        return new CheckInDTO(
            checkIn.getId(),
            pessoaMapper.converterDTO(checkIn.getPessoa()),
            checkIn.getDataEntrada(),
            checkIn.getDataSaidaPrevista(),
            checkIn.getAdicionalVeiculo(),
            checkIn.getValorTotal()
        );
    }

    public CheckIn converterEntity(CheckInDTO dto) {
        if (dto == null) {
            return null;
        }
        
        CheckIn checkIn = new CheckIn();
        checkIn.setId(dto.getId());
        checkIn.setPessoa(pessoaMapper.converterEntity(dto.getPessoa()));
        checkIn.setDataEntrada(dto.getDataEntrada());
        checkIn.setDataSaidaPrevista(dto.getDataSaidaPrevista());
        checkIn.setAdicionalVeiculo(dto.getAdicionalVeiculo());
        checkIn.setValorTotal(dto.getValorTotal());
        
        return checkIn;
    }
}

