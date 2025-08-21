package br.com.Senior.Teste.BPM.controller;

import br.com.Senior.Teste.BPM.dto.CheckInDTO;
import br.com.Senior.Teste.BPM.dto.CheckInRequestDTO;
import br.com.Senior.Teste.BPM.dto.ConsultaHospedesDTO;
import br.com.Senior.Teste.BPM.exception.BusinessException;
import br.com.Senior.Teste.BPM.exception.EntityNotFoundException;
import br.com.Senior.Teste.BPM.mapper.CheckInMapper;
import br.com.Senior.Teste.BPM.service.CheckInService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/check-in")
@Slf4j
@Validated
@CrossOrigin(origins = "*")
public class CheckInController implements ICheckInController {
    
    private final CheckInService checkInService;
    private final CheckInMapper checkInMapper;

    @PostMapping
    public ResponseEntity<CheckInDTO> realizarCheckIn(@Valid @RequestBody CheckInRequestDTO request) throws EntityNotFoundException, BusinessException {
        log.info("Realizando check-in para pessoa ID: {}", request.getPessoaId());
        var checkIn = checkInService.realizarCheckIn(
            request.getPessoaId(), 
            request.getDataEntrada(),
            request.getDataSaidaPrevista(),
            request.getAdicionalVeiculo()
        );
        var checkInDTO = checkInMapper.converterDTO(checkIn);
        log.info("Check-in realizado com sucesso!");
        return new ResponseEntity<>(checkInDTO, HttpStatus.OK);
    }
    
    @GetMapping("/pessoas/{pessoaId}")
    public ResponseEntity<Page<CheckInDTO>> buscarCheckInsPorPessoa(
            @PathVariable Long pessoaId,
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) throws EntityNotFoundException {
        log.info("Buscando check-ins da pessoa ID: {}, página: {}, tamanho: {}", pessoaId, pagina, tamanho);
        var checkIns = checkInService.buscarCheckInsPorPessoa(pessoaId, pagina, tamanho);
        var checkInsDTO = checkIns.map(checkInMapper::converterDTO);
        log.info("Check-ins encontrados com sucesso!");
        return new ResponseEntity<>(checkInsDTO, HttpStatus.OK);
    }
    
    @GetMapping("/hospedes/ativos")
    public ResponseEntity<Page<ConsultaHospedesDTO>> buscarHospedesAtivos(
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) {
        log.info("Buscando hóspedes ativos, página: {}, tamanho: {}", pagina, tamanho);
        var checkIns = checkInService.buscarHospedesAtivos(pagina, tamanho);
        var consultaDTO = checkIns.map(checkInMapper::converterConsultaHospedesDTO);
        log.info("Hóspedes ativos encontrados com sucesso!");
        return new ResponseEntity<>(consultaDTO, HttpStatus.OK);
    }
    
    @GetMapping("/hospedes/finalizados")
    public ResponseEntity<Page<ConsultaHospedesDTO>> buscarHospedesFinalizados(
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) {
        log.info("Buscando hóspedes finalizados, página: {}, tamanho: {}", pagina, tamanho);
        var checkIns = checkInService.buscarHospedesFinalizados(pagina, tamanho);
        var consultaDTO = checkIns.map(checkInMapper::converterConsultaHospedesDTO);
        log.info("Hóspedes finalizados encontrados com sucesso!");
        return new ResponseEntity<>(consultaDTO, HttpStatus.OK);
    }
    
    @GetMapping("/pessoas/{pessoaId}/hospedes/ativos")
    public ResponseEntity<Page<ConsultaHospedesDTO>> buscarHospedesAtivosPorPessoa(
            @PathVariable Long pessoaId,
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) throws EntityNotFoundException {
        log.info("Buscando hóspedes ativos da pessoa ID: {}, página: {}, tamanho: {}", pessoaId, pagina, tamanho);
        var checkIns = checkInService.buscarHospedesAtivosPorPessoa(pessoaId, pagina, tamanho);
        var consultaDTO = checkIns.map(checkInMapper::converterConsultaHospedesDTO);
        log.info("Hóspedes ativos da pessoa encontrados com sucesso!");
        return new ResponseEntity<>(consultaDTO, HttpStatus.OK);
    }
    
    @GetMapping("/pessoas/{pessoaId}/hospedes/finalizados")
    public ResponseEntity<Page<ConsultaHospedesDTO>> buscarHospedesFinalizadosPorPessoa(
            @PathVariable Long pessoaId,
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) throws EntityNotFoundException {
        log.info("Buscando hóspedes finalizados da pessoa ID: {}, página: {}, tamanho: {}", pessoaId, pagina, tamanho);
        var checkIns = checkInService.buscarHospedesFinalizadosPorPessoa(pessoaId, pagina, tamanho);
        var consultaDTO = checkIns.map(checkInMapper::converterConsultaHospedesDTO);
        log.info("Hóspedes finalizados da pessoa encontrados com sucesso!");
        return new ResponseEntity<>(consultaDTO, HttpStatus.OK);
    }
    
    @GetMapping("/pessoas/{pessoaId}/check-in/ativo")
    public ResponseEntity<CheckInDTO> buscarCheckInAtivoPorPessoa(@PathVariable Long pessoaId) throws EntityNotFoundException, BusinessException {
        log.info("Buscando check-in ativo da pessoa ID: {}", pessoaId);
        var checkIn = checkInService.buscarCheckInAtivoPorPessoa(pessoaId);
        var checkInDTO = checkInMapper.converterDTO(checkIn);
        log.info("Check-in ativo encontrado com sucesso!");
        return new ResponseEntity<>(checkInDTO, HttpStatus.OK);
    }
}
