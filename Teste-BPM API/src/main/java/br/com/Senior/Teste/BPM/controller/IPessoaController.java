package br.com.Senior.Teste.BPM.controller;

import br.com.Senior.Teste.BPM.dto.PessoaDTO;
import br.com.Senior.Teste.BPM.exception.BusinessException;
import br.com.Senior.Teste.BPM.exception.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

public interface IPessoaController {
    
    @Operation(summary = "Cadastra uma nova pessoa", description = "Cria uma nova pessoa no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Pessoa cadastrada com sucesso!"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou pessoa já existe"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    ResponseEntity<PessoaDTO> cadastrarPessoa(@Valid @RequestBody PessoaDTO pessoaDTO) throws BusinessException;

    @Operation(summary = "Lista pessoas com paginação", description = "Lista pessoas no banco de dados com paginação")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Pessoas listadas com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping
    ResponseEntity<Page<PessoaDTO>> buscarPessoas(@RequestParam(required = false) String termo,
                                                   @RequestParam(defaultValue = "0") Integer pagina,
                                                   @RequestParam(defaultValue = "10") Integer tamanho);

    @Operation(summary = "Busca pessoa por ID", description = "Busca uma pessoa específica por ID no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Pessoa encontrada com sucesso!"),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/{id}")
    ResponseEntity<PessoaDTO> buscarPessoaPorId(@PathVariable Long id) throws EntityNotFoundException;

    @Operation(summary = "Atualiza pessoa por ID", description = "Atualiza uma pessoa existente no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Pessoa atualizada com sucesso!"),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PutMapping("/{id}")
    ResponseEntity<PessoaDTO> atualizarPessoa(@PathVariable Long id, @Valid @RequestBody PessoaDTO pessoaDTO) throws EntityNotFoundException;

    @Operation(summary = "Deleta pessoa por ID", description = "Remove uma pessoa do banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Pessoa deletada com sucesso!"),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @DeleteMapping("/{id}")
    ResponseEntity<Void> deletarPessoa(@PathVariable Long id) throws EntityNotFoundException;
}
