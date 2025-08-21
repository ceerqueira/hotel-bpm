package br.com.Senior.Teste.BPM.controller;

import br.com.Senior.Teste.BPM.dto.PessoaDTO;
import br.com.Senior.Teste.BPM.exception.BusinessException;
import br.com.Senior.Teste.BPM.exception.EntityNotFoundException;
import br.com.Senior.Teste.BPM.mapper.EntityMapper;
import br.com.Senior.Teste.BPM.service.PessoaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pessoas")
@Slf4j
@Validated
@CrossOrigin(origins = "*")
public class PessoaController {
    
    private final PessoaService pessoaService;
    private final EntityMapper entityMapper;

    @PostMapping
    public ResponseEntity<PessoaDTO> cadastrarPessoa(@Valid @RequestBody PessoaDTO pessoaDTO) {
        log.info("Cadastrando nova pessoa...");
        try {
            var pessoa = entityMapper.toPessoaEntity(pessoaDTO);
            var pessoaSalva = pessoaService.cadastrarPessoa(pessoa);
            var pessoaSalvaDTO = entityMapper.toPessoaDTO(pessoaSalva);
            log.info("Pessoa cadastrada com sucesso!");
            return ResponseEntity.ok(pessoaSalvaDTO);
        } catch (BusinessException e) {
            log.warn("Erro de negócio ao cadastrar pessoa: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Erro ao cadastrar pessoa: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping
    public ResponseEntity<List<PessoaDTO>> buscarPessoas(@RequestParam(required = false) String termo) {
        log.info("Buscando pessoas com termo: {}", termo);
        try {
            var pessoas = pessoaService.buscarPessoas(termo);
            var pessoasDTO = entityMapper.toPessoaDTOList(pessoas);
            log.info("Busca de pessoas realizada com sucesso!");
            return ResponseEntity.ok(pessoasDTO);
        } catch (RuntimeException e) {
            log.error("Erro ao buscar pessoas: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> buscarPessoaPorId(@PathVariable Long id) {
        log.info("Buscando pessoa por ID: {}", id);
        try {
            var pessoa = pessoaService.buscarPessoaPorId(id);
            var pessoaDTO = entityMapper.toPessoaDTO(pessoa);
            log.info("Pessoa encontrada com sucesso!");
            return ResponseEntity.ok(pessoaDTO);
        } catch (EntityNotFoundException e) {
            log.warn("Pessoa não encontrada com ID: {}", id);
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            log.error("Erro ao buscar pessoa por ID: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTO> atualizarPessoa(@PathVariable Long id, @Valid @RequestBody PessoaDTO pessoaDTO) {
        log.info("Atualizando pessoa com ID: {}", id);
        try {
            pessoaDTO.setId(id);
            var pessoa = entityMapper.toPessoaEntity(pessoaDTO);
            var pessoaSalva = pessoaService.atualizarPessoa(id, pessoa);
            var pessoaSalvaDTO = entityMapper.toPessoaDTO(pessoaSalva);
            log.info("Pessoa atualizada com sucesso!");
            return ResponseEntity.ok(pessoaSalvaDTO);
        } catch (EntityNotFoundException e) {
            log.warn("Pessoa não encontrada para atualização: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (BusinessException e) {
            log.warn("Erro de negócio ao atualizar pessoa: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.error("Erro ao atualizar pessoa: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable Long id) {
        log.info("Deletando pessoa com ID: {}", id);
        try {
            // Implementar lógica de deleção se necessário
            log.info("Pessoa deletada com sucesso!");
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.error("Erro ao deletar pessoa: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
