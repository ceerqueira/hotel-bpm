package br.com.Senior.Teste.BPM.controller;

import br.com.Senior.Teste.BPM.dto.CheckInDTO;
import br.com.Senior.Teste.BPM.dto.CheckInRequestDTO;
import br.com.Senior.Teste.BPM.dto.CheckOutRequestDTO;
import br.com.Senior.Teste.BPM.dto.ConsultaHospedesDTO;
import br.com.Senior.Teste.BPM.dto.PessoaDTO;
import br.com.Senior.Teste.BPM.mapper.EntityMapper;
import br.com.Senior.Teste.BPM.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotel")
@CrossOrigin(origins = "*")
public class HotelController {
    
    @Autowired
    private HotelService hotelService;
    
    // CRUD para Pessoas
    @PostMapping("/pessoas")
    public ResponseEntity<PessoaDTO> cadastrarPessoa(@RequestBody PessoaDTO pessoaDTO) {
        try {
            var pessoa = EntityMapper.toPessoaEntity(pessoaDTO);
            var pessoaSalva = hotelService.cadastrarPessoa(pessoa);
            var pessoaSalvaDTO = EntityMapper.toPessoaDTO(pessoaSalva);
            return ResponseEntity.ok(pessoaSalvaDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/pessoas")
    public ResponseEntity<List<PessoaDTO>> buscarPessoas(@RequestParam(required = false) String termo) {
        try {
            var pessoas = hotelService.buscarPessoas(termo);
            var pessoasDTO = EntityMapper.toPessoaDTOList(pessoas);
            return ResponseEntity.ok(pessoasDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/pessoas/{id}")
    public ResponseEntity<PessoaDTO> buscarPessoaPorId(@PathVariable Long id) {
        try {
            var pessoas = hotelService.buscarPessoas(id.toString());
            if (pessoas.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            var pessoaDTO = EntityMapper.toPessoaDTO(pessoas.get(0));
            return ResponseEntity.ok(pessoaDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/pessoas/{id}")
    public ResponseEntity<PessoaDTO> atualizarPessoa(@PathVariable Long id, @RequestBody PessoaDTO pessoaDTO) {
        try {
            pessoaDTO.setId(id);
            var pessoa = EntityMapper.toPessoaEntity(pessoaDTO);
            var pessoaSalva = hotelService.cadastrarPessoa(pessoa);
            var pessoaSalvaDTO = EntityMapper.toPessoaDTO(pessoaSalva);
            return ResponseEntity.ok(pessoaSalvaDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/pessoas/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable Long id) {
        try {
            // Implementar lógica de deleção se necessário
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Check-in e Check-out
    @PostMapping("/check-in")
    public ResponseEntity<CheckInDTO> realizarCheckIn(@RequestBody CheckInRequestDTO request) {
        try {
            var checkIn = hotelService.realizarCheckIn(
                request.getPessoaId(), 
                request.getDataEntrada(), 
                request.getAdicionalVeiculo()
            );
            var checkInDTO = EntityMapper.toCheckInDTO(checkIn);
            return ResponseEntity.ok(checkInDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/check-out/{checkInId}")
    public ResponseEntity<CheckInDTO> realizarCheckOut(
            @PathVariable Long checkInId,
            @RequestBody CheckOutRequestDTO request) {
        try {
            var checkIn = hotelService.realizarCheckOut(checkInId, request.getDataSaida());
            var checkInDTO = EntityMapper.toCheckInDTO(checkIn);
            return ResponseEntity.ok(checkInDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    // Consultas
    @GetMapping("/pessoas/{pessoaId}/check-ins")
    public ResponseEntity<List<CheckInDTO>> buscarCheckInsPorPessoa(@PathVariable Long pessoaId) {
        try {
            var checkIns = hotelService.buscarCheckInsPorPessoa(pessoaId);
            var checkInsDTO = EntityMapper.toCheckInDTOList(checkIns);
            return ResponseEntity.ok(checkInsDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/check-ins/ativos")
    public ResponseEntity<List<ConsultaHospedesDTO>> buscarHospedesAtivos() {
        try {
            var checkIns = hotelService.buscarCheckInsAtivos();
            var consultaDTO = EntityMapper.toConsultaHospedesDTOList(checkIns);
            return ResponseEntity.ok(consultaDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/check-ins/finalizados")
    public ResponseEntity<List<ConsultaHospedesDTO>> buscarHospedesFinalizados() {
        try {
            var checkIns = hotelService.buscarCheckInsFinalizados();
            var consultaDTO = EntityMapper.toConsultaHospedesDTOList(checkIns);
            return ResponseEntity.ok(consultaDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/pessoas/{pessoaId}/check-ins/ativos")
    public ResponseEntity<List<ConsultaHospedesDTO>> buscarCheckInsAtivosPorPessoa(@PathVariable Long pessoaId) {
        try {
            var checkIns = hotelService.buscarCheckInsAtivosPorPessoa(pessoaId);
            var consultaDTO = EntityMapper.toConsultaHospedesDTOList(checkIns);
            return ResponseEntity.ok(consultaDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/pessoas/{pessoaId}/check-ins/finalizados")
    public ResponseEntity<List<ConsultaHospedesDTO>> buscarCheckInsFinalizadosPorPessoa(@PathVariable Long pessoaId) {
        try {
            var checkIns = hotelService.buscarCheckInsFinalizadosPorPessoa(pessoaId);
            var consultaDTO = EntityMapper.toConsultaHospedesDTOList(checkIns);
            return ResponseEntity.ok(consultaDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
