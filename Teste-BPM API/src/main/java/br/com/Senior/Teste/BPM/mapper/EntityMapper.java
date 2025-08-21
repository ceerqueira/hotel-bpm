package br.com.Senior.Teste.BPM.mapper;

import br.com.Senior.Teste.BPM.dto.CheckInDTO;
import br.com.Senior.Teste.BPM.dto.ConsultaHospedesDTO;
import br.com.Senior.Teste.BPM.dto.PessoaDTO;
import br.com.Senior.Teste.BPM.entity.CheckIn;
import br.com.Senior.Teste.BPM.entity.Pessoa;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class EntityMapper {
    
    // Conversão Pessoa Entity para DTO
    public static PessoaDTO toPessoaDTO(Pessoa pessoa) {
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
    
    // Conversão Pessoa DTO para Entity
    public static Pessoa toPessoaEntity(PessoaDTO dto) {
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
    
    // Conversão CheckIn Entity para DTO
    public static CheckInDTO toCheckInDTO(CheckIn checkIn) {
        if (checkIn == null) {
            return null;
        }
        
        return new CheckInDTO(
            checkIn.getId(),
            toPessoaDTO(checkIn.getPessoa()),
            checkIn.getDataEntrada(),
            checkIn.getDataSaidaPrevista(),
            checkIn.getAdicionalVeiculo(),
            checkIn.getValorTotal()
        );
    }
    
    // Conversão CheckIn DTO para Entity
    public static CheckIn toCheckInEntity(CheckInDTO dto) {
        if (dto == null) {
            return null;
        }
        
        CheckIn checkIn = new CheckIn();
        checkIn.setId(dto.getId());
        checkIn.setPessoa(toPessoaEntity(dto.getPessoa()));
        checkIn.setDataEntrada(dto.getDataEntrada());
        checkIn.setDataSaidaPrevista(dto.getDataSaidaPrevista());
        checkIn.setAdicionalVeiculo(dto.getAdicionalVeiculo());
        checkIn.setValorTotal(dto.getValorTotal());
        
        return checkIn;
    }
    
    // Conversão para ConsultaHospedesDTO
    public static ConsultaHospedesDTO toConsultaHospedesDTO(CheckIn checkIn) {
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
            toPessoaDTO(checkIn.getPessoa()),
            checkIn.getDataEntrada(),
            checkIn.getDataSaidaPrevista(),
            checkIn.getAdicionalVeiculo(),
            checkIn.getValorTotal(),
            status,
            numeroDias
        );
    }
    
    // Conversão de listas
    public static List<PessoaDTO> toPessoaDTOList(List<Pessoa> pessoas) {
        if (pessoas == null) {
            return null;
        }
        
        return pessoas.stream()
                .map(EntityMapper::toPessoaDTO)
                .collect(Collectors.toList());
    }
    
    public static List<CheckInDTO> toCheckInDTOList(List<CheckIn> checkIns) {
        if (checkIns == null) {
            return null;
        }
        
        return checkIns.stream()
                .map(EntityMapper::toCheckInDTO)
                .collect(Collectors.toList());
    }
    
    public static List<ConsultaHospedesDTO> toConsultaHospedesDTOList(List<CheckIn> checkIns) {
        if (checkIns == null) {
            return null;
        }
        
        return checkIns.stream()
                .map(EntityMapper::toConsultaHospedesDTO)
                .collect(Collectors.toList());
    }
}
