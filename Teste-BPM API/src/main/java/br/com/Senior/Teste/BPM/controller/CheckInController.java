package br.com.Senior.Teste.BPM.controller;

import br.com.Senior.Teste.BPM.dto.CheckInDTO;
import br.com.Senior.Teste.BPM.dto.CheckInRequestDTO;
import br.com.Senior.Teste.BPM.dto.ConsultaHospedesDTO;
import br.com.Senior.Teste.BPM.exception.BusinessException;
import br.com.Senior.Teste.BPM.exception.EntityNotFoundException;
import br.com.Senior.Teste.BPM.mapper.EntityMapper;
import br.com.Senior.Teste.BPM.service.CheckInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/check-in")
@Slf4j
@Validated
@CrossOrigin(origins = "*")
public class CheckInController {
    
    private final CheckInService checkInService;
    private final EntityMapper entityMapper;

    @PostMapping
    public ResponseEntity<CheckInDTO> realizarCheckIn(@Valid @RequestBody CheckInRequestDTO request) {
        log.info("Realizando check-in para pessoa ID: {}", request.getPessoaId());
        try {
            var checkIn = checkInService.realizarCheckIn(
                request.getPessoaId(), 
                request.getDataEntrada(),
                request.getDataSaidaPrevista(),
                request.getAdicionalVeiculo()
            );
            var checkInDTO = entityMapper.toCheckInDTO(checkIn);
            log.info("Check-in realizado com sucesso!");
            return ResponseEntity.ok(checkInDTO);
        } catch (EntityNotFoundException e) {
            log.warn("Pessoa não encontrada para check-in: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BusinessException e) {
            log.warn("Erro de negócio ao realizar check-in: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Erro ao realizar check-in: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/pessoas/{pessoaId}")
    public ResponseEntity<List<CheckInDTO>> buscarCheckInsPorPessoa(@PathVariable Long pessoaId) {
        log.info("Buscando check-ins da pessoa ID: {}", pessoaId);
        try {
            var checkIns = checkInService.buscarCheckInsPorPessoa(pessoaId);
            var checkInsDTO = entityMapper.toCheckInDTOList(checkIns);
            log.info("Check-ins encontrados com sucesso!");
            return ResponseEntity.ok(checkInsDTO);
        } catch (EntityNotFoundException e) {
            log.warn("Pessoa não encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            log.error("Erro ao buscar check-ins: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/hospedes/ativos")
    public ResponseEntity<List<ConsultaHospedesDTO>> buscarHospedesAtivos() {
        log.info("Buscando hóspedes ativos...");
        try {
            var checkIns = checkInService.buscarHospedesAtivos();
            var consultaDTO = entityMapper.toConsultaHospedesDTOList(checkIns);
            log.info("Hóspedes ativos encontrados com sucesso!");
            return ResponseEntity.ok(consultaDTO);
        } catch (RuntimeException e) {
            log.error("Erro ao buscar hóspedes ativos: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/hospedes/finalizados")
    public ResponseEntity<List<ConsultaHospedesDTO>> buscarHospedesFinalizados() {
        log.info("Buscando hóspedes finalizados...");
        try {
            var checkIns = checkInService.buscarHospedesFinalizados();
            var consultaDTO = entityMapper.toConsultaHospedesDTOList(checkIns);
            log.info("Hóspedes finalizados encontrados com sucesso!");
            return ResponseEntity.ok(consultaDTO);
        } catch (RuntimeException e) {
            log.error("Erro ao buscar hóspedes finalizados: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/pessoas/{pessoaId}/hospedes/ativos")
    public ResponseEntity<List<ConsultaHospedesDTO>> buscarHospedesAtivosPorPessoa(@PathVariable Long pessoaId) {
        log.info("Buscando hóspedes ativos da pessoa ID: {}", pessoaId);
        try {
            var checkIns = checkInService.buscarHospedesAtivosPorPessoa(pessoaId);
            var consultaDTO = entityMapper.toConsultaHospedesDTOList(checkIns);
            log.info("Hóspedes ativos da pessoa encontrados com sucesso!");
            return ResponseEntity.ok(consultaDTO);
        } catch (EntityNotFoundException e) {
            log.warn("Pessoa não encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            log.error("Erro ao buscar hóspedes ativos da pessoa: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/pessoas/{pessoaId}/hospedes/finalizados")
    public ResponseEntity<List<ConsultaHospedesDTO>> buscarHospedesFinalizadosPorPessoa(@PathVariable Long pessoaId) {
        log.info("Buscando hóspedes finalizados da pessoa ID: {}", pessoaId);
        try {
            var checkIns = checkInService.buscarHospedesFinalizadosPorPessoa(pessoaId);
            var consultaDTO = entityMapper.toConsultaHospedesDTOList(checkIns);
            log.info("Hóspedes finalizados da pessoa encontrados com sucesso!");
            return ResponseEntity.ok(consultaDTO);
        } catch (EntityNotFoundException e) {
            log.warn("Pessoa não encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            log.error("Erro ao buscar hóspedes finalizados da pessoa: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/pessoas/{pessoaId}/check-in/ativo")
    public ResponseEntity<CheckInDTO> buscarCheckInAtivoPorPessoa(@PathVariable Long pessoaId) {
        log.info("Buscando check-in ativo da pessoa ID: {}", pessoaId);
        try {
            var checkIn = checkInService.buscarCheckInAtivoPorPessoa(pessoaId);
            var checkInDTO = entityMapper.toCheckInDTO(checkIn);
            log.info("Check-in ativo encontrado com sucesso!");
            return ResponseEntity.ok(checkInDTO);
        } catch (EntityNotFoundException e) {
            log.warn("Pessoa não encontrada: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BusinessException e) {
            log.warn("Erro de negócio: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Erro ao buscar check-in ativo: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
