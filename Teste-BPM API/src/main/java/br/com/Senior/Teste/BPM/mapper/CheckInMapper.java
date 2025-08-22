package br.com.Senior.Teste.BPM.mapper;

import br.com.Senior.Teste.BPM.dto.CheckInDTO;
import br.com.Senior.Teste.BPM.dto.ConsultaHospedesDTO;
import br.com.Senior.Teste.BPM.entity.CheckIn;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    public ConsultaHospedesDTO converterConsultaHospedesDTO(CheckIn checkIn) {
        if (checkIn == null) {
            return null;
        }
        
        LocalDateTime horaAtual = LocalDateTime.now();
        String status = horaAtual.isAfter(checkIn.getDataSaidaPrevista()) ? "FINALIZADO" : "ATIVO";
        Long numeroDias = java.time.temporal.ChronoUnit.DAYS.between(
            checkIn.getDataEntrada().toLocalDate(), 
            checkIn.getDataSaidaPrevista().toLocalDate()
        );
        
        return new ConsultaHospedesDTO(
            checkIn.getId(),
            pessoaMapper.converterDTO(checkIn.getPessoa()),
            checkIn.getDataEntrada(),
            checkIn.getDataSaidaPrevista(),
            checkIn.getAdicionalVeiculo(),
            checkIn.getValorTotal(),
            status,
            numeroDias
        );
    }

    public List<CheckInDTO> converterDTOList(List<CheckIn> checkIns) {
        if (checkIns == null) {
            return null;
        }
        
        return checkIns.stream()
                .map(this::converterDTO)
                .collect(Collectors.toList());
    }

    public List<ConsultaHospedesDTO> converterConsultaHospedesDTOList(List<CheckIn> checkIns) {
        if (checkIns == null) {
            return null;
        }
        
        return checkIns.stream()
                .map(this::converterConsultaHospedesDTO)
                .collect(Collectors.toList());
    }
}

