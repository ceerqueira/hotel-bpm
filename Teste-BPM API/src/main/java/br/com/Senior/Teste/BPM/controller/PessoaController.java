package br.com.Senior.Teste.BPM.controller;

import br.com.Senior.Teste.BPM.dto.PessoaDTO;
import br.com.Senior.Teste.BPM.exception.BusinessException;
import br.com.Senior.Teste.BPM.exception.EntityNotFoundException;
import br.com.Senior.Teste.BPM.mapper.PessoaMapper;
import br.com.Senior.Teste.BPM.service.PessoaService;
import org.springframework.data.domain.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/pessoas")
@Slf4j
@Validated
@CrossOrigin(origins = "*")
public class PessoaController implements IPessoaController {
    
    private final PessoaService pessoaService;
    private final PessoaMapper pessoaMapper;

    @PostMapping
    public ResponseEntity<PessoaDTO> cadastrarPessoa(@Valid @RequestBody PessoaDTO pessoaDTO) throws BusinessException {
        log.info("Cadastrando nova pessoa...");
        var pessoa = pessoaMapper.converterEntity(pessoaDTO);
        var pessoaSalva = pessoaService.cadastrarPessoa(pessoa);
        var pessoaSalvaDTO = pessoaMapper.converterDTO(pessoaSalva);
        log.info("Pessoa cadastrada com sucesso!");
        return new ResponseEntity<>(pessoaSalvaDTO, HttpStatus.OK);
    }
    
    @GetMapping
    public ResponseEntity<Page<PessoaDTO>> buscarPessoas(
            @RequestParam(required = false) String termo,
            @RequestParam(defaultValue = "0") Integer pagina,
            @RequestParam(defaultValue = "10") Integer tamanho) {
        log.info("Buscando pessoas com termo: {}, página: {}, tamanho: {}", termo, pagina, tamanho);
        var pessoas = pessoaService.buscarPessoas(termo, pagina, tamanho);
        var pessoasDTO = pessoas.map(pessoaMapper::converterDTO);
        log.info("Busca de pessoas realizada com sucesso!");
        return new ResponseEntity<>(pessoasDTO, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PessoaDTO> buscarPessoaPorId(@PathVariable Long id) throws EntityNotFoundException {
        log.info("Buscando pessoa por ID: {}", id);
        var pessoa = pessoaService.buscarPessoaPorId(id);
        var pessoaDTO = pessoaMapper.converterDTO(pessoa);
        log.info("Pessoa encontrada com sucesso!");
        return new ResponseEntity<>(pessoaDTO, HttpStatus.OK);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PessoaDTO> atualizarPessoa(@PathVariable Long id, @Valid @RequestBody PessoaDTO pessoaDTO) throws EntityNotFoundException {
        log.info("Atualizando pessoa com ID: {}", id);
        pessoaDTO.setId(id);
        var pessoa = pessoaMapper.converterEntity(pessoaDTO);
        var pessoaSalva = pessoaService.atualizarPessoa(id, pessoa);
        var pessoaSalvaDTO = pessoaMapper.converterDTO(pessoaSalva);
        log.info("Pessoa atualizada com sucesso!");
        return new ResponseEntity<>(pessoaSalvaDTO, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable Long id) throws EntityNotFoundException {
        log.info("Deletando pessoa com ID: {}", id);
        pessoaService.deletarPessoa(id);
        log.info("Pessoa deletada com sucesso!");
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
