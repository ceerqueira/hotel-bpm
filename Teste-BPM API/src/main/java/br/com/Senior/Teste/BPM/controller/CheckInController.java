package br.com.Senior.Teste.BPM.controller;

import br.com.Senior.Teste.BPM.controller.dto.CheckInDTO;
import br.com.Senior.Teste.BPM.controller.dto.CheckInRequestDTO;
import br.com.Senior.Teste.BPM.controller.dto.CheckInUpdateDTO;
import br.com.Senior.Teste.BPM.controller.dto.ConsultaHospedesDTO;
import br.com.Senior.Teste.BPM.entity.CheckIn;
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
        CheckIn checkIn = checkInService.realizarCheckIn(
            request.getPessoaId(), 
            request.getDataEntrada(),
            request.getDataSaidaPrevista(),
            request.getAdicionalVeiculo()
        );
        CheckInDTO checkInDTO = checkInMapper.converterDTO(checkIn);
        log.info("Check-in realizado com sucesso!");
        return new ResponseEntity<>(checkInDTO, HttpStatus.OK);
    }
    
    @GetMapping("/pessoas/{pessoaId}")
    public ResponseEntity<Page<CheckInDTO>> buscarCheckInsPorPessoa(
            @PathVariable Long pessoaId,
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) throws EntityNotFoundException {
        log.info("Buscando check-ins da pessoa ID: {}, página: {}, tamanho: {}", pessoaId, pagina, tamanho);
        Page<CheckIn> checkIns = checkInService.buscarCheckInsPorPessoa(pessoaId, pagina, tamanho);
        Page<CheckInDTO> checkInsDTO = checkIns.map(checkInMapper::converterDTO);
        log.info("Check-ins encontrados com sucesso!");
        return new ResponseEntity<>(checkInsDTO, HttpStatus.OK);
    }
    
    @GetMapping("/hospedes/ativos")
    public ResponseEntity<Page<ConsultaHospedesDTO>> buscarHospedesAtivos(
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) {
        log.info("Buscando hóspedes ativos, página: {}, tamanho: {}", pagina, tamanho);
        Page<CheckIn> checkIns = checkInService.buscarHospedesAtivos(pagina, tamanho);
        Page<ConsultaHospedesDTO> consultaDTO = checkIns.map(checkInMapper::converterConsultaHospedesDTO);
        log.info("Hóspedes ativos encontrados com sucesso!");
        return new ResponseEntity<>(consultaDTO, HttpStatus.OK);
    }
    
    @GetMapping("/hospedes/finalizados")
    public ResponseEntity<Page<ConsultaHospedesDTO>> buscarHospedesFinalizados(
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) {
        log.info("Buscando hóspedes finalizados, página: {}, tamanho: {}", pagina, tamanho);
        Page<CheckIn> checkIns = checkInService.buscarHospedesFinalizados(pagina, tamanho);
        Page<ConsultaHospedesDTO> consultaDTO = checkIns.map(checkInMapper::converterConsultaHospedesDTO);
        log.info("Hóspedes finalizados encontrados com sucesso!");
        return new ResponseEntity<>(consultaDTO, HttpStatus.OK);
    }
    
    @GetMapping("/pessoas/{pessoaId}/hospedes/ativos")
    public ResponseEntity<Page<ConsultaHospedesDTO>> buscarHospedesAtivosPorPessoa(
            @PathVariable Long pessoaId,
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) throws EntityNotFoundException {
        log.info("Buscando hóspedes ativos da pessoa ID: {}, página: {}, tamanho: {}", pessoaId, pagina, tamanho);
        Page<CheckIn> checkIns = checkInService.buscarHospedesAtivosPorPessoa(pessoaId, pagina, tamanho);
        Page<ConsultaHospedesDTO> consultaDTO = checkIns.map(checkInMapper::converterConsultaHospedesDTO);
        log.info("Hóspedes ativos da pessoa encontrados com sucesso!");
        return new ResponseEntity<>(consultaDTO, HttpStatus.OK);
    }
    
    @GetMapping("/pessoas/{pessoaId}/hospedes/finalizados")
    public ResponseEntity<Page<ConsultaHospedesDTO>> buscarHospedesFinalizadosPorPessoa(
            @PathVariable Long pessoaId,
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) throws EntityNotFoundException {
        log.info("Buscando hóspedes finalizados da pessoa ID: {}, página: {}, tamanho: {}", pessoaId, pagina, tamanho);
        Page<CheckIn> checkIns = checkInService.buscarHospedesFinalizadosPorPessoa(pessoaId, pagina, tamanho);
        Page<ConsultaHospedesDTO> consultaDTO = checkIns.map(checkInMapper::converterConsultaHospedesDTO);
        log.info("Hóspedes finalizados da pessoa encontrados com sucesso!");
        return new ResponseEntity<>(consultaDTO, HttpStatus.OK);
    }
    
    @GetMapping("/pessoas/{pessoaId}/check-in/ativo")
    public ResponseEntity<CheckInDTO> buscarCheckInAtivoPorPessoa(@PathVariable Long pessoaId) throws EntityNotFoundException, BusinessException {
        log.info("Buscando check-in ativo da pessoa ID: {}", pessoaId);
        CheckIn checkIn = checkInService.buscarCheckInAtivoPorPessoa(pessoaId);
        CheckInDTO checkInDTO = checkInMapper.converterDTO(checkIn);
        log.info("Check-in ativo encontrado com sucesso!");
        return new ResponseEntity<>(checkInDTO, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<CheckInDTO> buscarCheckInPorId(@PathVariable Long id) throws EntityNotFoundException {
        log.info("Buscando check-in por ID: {}", id);
        CheckIn checkIn = checkInService.buscarCheckInPorId(id);
        CheckInDTO checkInDTO = checkInMapper.converterDTO(checkIn);
        log.info("Check-in encontrado com sucesso!");
        return new ResponseEntity<>(checkInDTO, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<CheckInDTO> atualizarCheckIn(@PathVariable Long id, @Valid @RequestBody CheckInUpdateDTO checkInUpdateDTO) throws EntityNotFoundException {
        log.info("Atualizando check-in com ID: {}", id);
        CheckIn checkInAtualizado = CheckIn.builder()
                .dataEntrada(checkInUpdateDTO.getDataEntrada())
                .dataSaidaPrevista(checkInUpdateDTO.getDataSaidaPrevista())
                .adicionalVeiculo(checkInUpdateDTO.getAdicionalVeiculo())
                .build();
        CheckIn checkIn = checkInService.atualizarCheckIn(id, checkInAtualizado);
        CheckInDTO checkInDTO = checkInMapper.converterDTO(checkIn);
        log.info("Check-in atualizado com sucesso!");
        return new ResponseEntity<>(checkInDTO, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCheckIn(@PathVariable Long id) throws EntityNotFoundException {
        log.info("Deletando check-in com ID: {}", id);
        checkInService.deletarCheckIn(id);
        log.info("Check-in deletado com sucesso!");
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping
    public ResponseEntity<Page<CheckInDTO>> listarTodosCheckIns(
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) {
        log.info("Listando todos os check-ins, página: {}, tamanho: {}", pagina, tamanho);
        Page<CheckIn> checkIns = checkInService.listarTodosCheckIns(pagina, tamanho);
        Page<CheckInDTO> checkInsDTO = checkIns.map(checkInMapper::converterDTO);
        log.info("Check-ins listados com sucesso!");
        return new ResponseEntity<>(checkInsDTO, HttpStatus.OK);
    }
}
