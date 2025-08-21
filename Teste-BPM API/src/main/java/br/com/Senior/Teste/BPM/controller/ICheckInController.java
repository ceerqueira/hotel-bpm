package br.com.Senior.Teste.BPM.controller;

import br.com.Senior.Teste.BPM.dto.CheckInDTO;
import br.com.Senior.Teste.BPM.dto.CheckInRequestDTO;
import br.com.Senior.Teste.BPM.dto.ConsultaHospedesDTO;
import br.com.Senior.Teste.BPM.exception.BusinessException;
import br.com.Senior.Teste.BPM.exception.EntityNotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

public interface ICheckInController {
    
    @Operation(summary = "Realiza check-in de uma pessoa", description = "Cria um novo check-in no banco de dados")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Check-in realizado com sucesso!"),
                    @ApiResponse(responseCode = "400", description = "Dados inválidos ou pessoa já possui check-in ativo"),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @PostMapping
    ResponseEntity<CheckInDTO> realizarCheckIn(@Valid @RequestBody CheckInRequestDTO request) throws EntityNotFoundException, BusinessException;

    @Operation(summary = "Lista check-ins de uma pessoa", description = "Lista check-ins de uma pessoa específica com paginação")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Check-ins listados com sucesso!"),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/pessoas/{pessoaId}")
    ResponseEntity<Page<CheckInDTO>> buscarCheckInsPorPessoa(@PathVariable Long pessoaId,
                                                              @RequestParam(defaultValue = "0") Integer pagina,
                                                              @RequestParam(defaultValue = "10") Integer tamanho) throws EntityNotFoundException;

    @Operation(summary = "Lista hóspedes ativos", description = "Lista todos os hóspedes ativos com paginação")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Hóspedes ativos listados com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/hospedes/ativos")
    ResponseEntity<Page<ConsultaHospedesDTO>> buscarHospedesAtivos(@RequestParam(defaultValue = "0") Integer pagina,
                                                                    @RequestParam(defaultValue = "10") Integer tamanho);

    @Operation(summary = "Lista hóspedes finalizados", description = "Lista todos os hóspedes finalizados com paginação")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Hóspedes finalizados listados com sucesso!"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/hospedes/finalizados")
    ResponseEntity<Page<ConsultaHospedesDTO>> buscarHospedesFinalizados(@RequestParam(defaultValue = "0") Integer pagina,
                                                                         @RequestParam(defaultValue = "10") Integer tamanho);

    @Operation(summary = "Lista hóspedes ativos de uma pessoa", description = "Lista hóspedes ativos de uma pessoa específica com paginação")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Hóspedes ativos da pessoa listados com sucesso!"),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/pessoas/{pessoaId}/hospedes/ativos")
    ResponseEntity<Page<ConsultaHospedesDTO>> buscarHospedesAtivosPorPessoa(@PathVariable Long pessoaId,
                                                                             @RequestParam(defaultValue = "0") Integer pagina,
                                                                             @RequestParam(defaultValue = "10") Integer tamanho) throws EntityNotFoundException;

    @Operation(summary = "Lista hóspedes finalizados de uma pessoa", description = "Lista hóspedes finalizados de uma pessoa específica com paginação")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Hóspedes finalizados da pessoa listados com sucesso!"),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/pessoas/{pessoaId}/hospedes/finalizados")
    ResponseEntity<Page<ConsultaHospedesDTO>> buscarHospedesFinalizadosPorPessoa(@PathVariable Long pessoaId,
                                                                                  @RequestParam(defaultValue = "0") Integer pagina,
                                                                                  @RequestParam(defaultValue = "10") Integer tamanho) throws EntityNotFoundException;

    @Operation(summary = "Busca check-in ativo de uma pessoa", description = "Busca o check-in ativo de uma pessoa específica")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Check-in ativo encontrado com sucesso!"),
                    @ApiResponse(responseCode = "400", description = "Pessoa não possui check-in ativo"),
                    @ApiResponse(responseCode = "404", description = "Pessoa não encontrada"),
                    @ApiResponse(responseCode = "403", description = "Você não tem permissão para acessar este recurso"),
                    @ApiResponse(responseCode = "500", description = "Foi gerada uma exceção")
            }
    )
    @GetMapping("/pessoas/{pessoaId}/check-in/ativo")
    ResponseEntity<CheckInDTO> buscarCheckInAtivoPorPessoa(@PathVariable Long pessoaId) throws EntityNotFoundException, BusinessException;
}
