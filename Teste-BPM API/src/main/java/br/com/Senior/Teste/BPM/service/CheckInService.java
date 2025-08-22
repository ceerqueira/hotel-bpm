package br.com.Senior.Teste.BPM.service;

import br.com.Senior.Teste.BPM.entity.CheckIn;
import br.com.Senior.Teste.BPM.entity.Pessoa;
import br.com.Senior.Teste.BPM.exception.BusinessException;
import br.com.Senior.Teste.BPM.exception.EntityNotFoundException;
import br.com.Senior.Teste.BPM.repository.CheckInRepository;
import br.com.Senior.Teste.BPM.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckInService {
    
    private final CheckInRepository checkInRepository;
    private final PessoaRepository pessoaRepository;
    private final PricingService pricingService;
    
    public CheckIn realizarCheckIn(Long pessoaId, LocalDateTime dataEntrada, 
                                  LocalDateTime dataSaidaPrevista, Boolean adicionalVeiculo) throws EntityNotFoundException {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + pessoaId));

        if (dataSaidaPrevista.isBefore(dataEntrada)) {
            throw new BusinessException("A data de saída prevista não pode ser anterior à data de entrada");
        }

        checkInRepository.findCheckInsComConflitoDeHorario(pessoa, dataEntrada, dataSaidaPrevista)
                .stream()
                .findFirst()
                .ifPresent(checkInConflitante -> {
                    throw new BusinessException("Conflito de horário detectado. A pessoa já possui um check-in no período de " + 
                        checkInConflitante.getDataEntrada() + " até " + checkInConflitante.getDataSaidaPrevista() + 
                        ". ID do check-in conflitante: " + checkInConflitante.getId());
                });

        CheckIn checkIn = CheckIn.builder()
                .pessoa(pessoa)
                .dataEntrada(dataEntrada)
                .dataSaidaPrevista(dataSaidaPrevista)
                .adicionalVeiculo(adicionalVeiculo)
                .valorTotal(pricingService.calcularValorTotal(dataEntrada, dataSaidaPrevista, adicionalVeiculo))
                .build();
        
        return checkInRepository.save(checkIn);
    }
    
    public Page<CheckIn> buscarCheckInsPorPessoa(Long pessoaId, Integer pagina, Integer tamanho) throws EntityNotFoundException {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + pessoaId));
        return checkInRepository.findByPessoa(pessoa, PageRequest.of(pagina, tamanho));
    }
    
    public Page<CheckIn> buscarHospedesAtivos(Integer pagina, Integer tamanho) {
        return checkInRepository.findHospedesAtivos(LocalDateTime.now(), PageRequest.of(pagina, tamanho));
    }
    
    public Page<CheckIn> buscarHospedesFinalizados(Integer pagina, Integer tamanho) {
        return checkInRepository.findHospedesFinalizados(LocalDateTime.now(), PageRequest.of(pagina, tamanho));
    }
    
    public Page<CheckIn> buscarHospedesAtivosPorPessoa(Long pessoaId, Integer pagina, Integer tamanho) throws EntityNotFoundException {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + pessoaId));
        return checkInRepository.findHospedesAtivosPorPessoa(pessoa, LocalDateTime.now(), PageRequest.of(pagina, tamanho));
    }
    
    public Page<CheckIn> buscarHospedesFinalizadosPorPessoa(Long pessoaId, Integer pagina, Integer tamanho) throws EntityNotFoundException {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + pessoaId));
        return checkInRepository.findHospedesFinalizadosPorPessoa(pessoa, LocalDateTime.now(), PageRequest.of(pagina, tamanho));
    }
    
    public CheckIn buscarCheckInPorId(Long id) throws EntityNotFoundException {
        return checkInRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Check-in não encontrado com ID: " + id));
    }

    public CheckIn atualizarCheckIn(Long id, CheckIn checkInAtualizado) throws EntityNotFoundException {
        CheckIn checkIn = checkInRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Check-in não encontrado com ID: " + id));

        if (checkInAtualizado.getDataSaidaPrevista().isBefore(checkInAtualizado.getDataEntrada())) {
            throw new BusinessException("A data de saída prevista não pode ser anterior à data de entrada");
        }

        checkInRepository.findCheckInsComConflitoDeHorarioExcluindoCheckIn(
            checkIn.getPessoa(), 
            checkInAtualizado.getDataEntrada(), 
            checkInAtualizado.getDataSaidaPrevista(),
            checkIn.getId()
        )
        .stream()
        .findFirst()
        .ifPresent(checkInConflitante -> {
            throw new BusinessException("Conflito de horário detectado. A pessoa já possui um check-in no período de " + 
                checkInConflitante.getDataEntrada() + " até " + checkInConflitante.getDataSaidaPrevista() + 
                ". ID do check-in conflitante: " + checkInConflitante.getId());
        });

        checkIn.setDataEntrada(checkInAtualizado.getDataEntrada());
        checkIn.setDataSaidaPrevista(checkInAtualizado.getDataSaidaPrevista());
        checkIn.setAdicionalVeiculo(checkInAtualizado.getAdicionalVeiculo());

        // Recalcula o valor total
        BigDecimal valorTotal = pricingService.calcularValorTotal(
            checkIn.getDataEntrada(),
            checkIn.getDataSaidaPrevista(),
            checkIn.getAdicionalVeiculo()
        );
        checkIn.setValorTotal(valorTotal);

        return checkInRepository.save(checkIn);
    }
    
    public void deletarCheckIn(Long id) throws EntityNotFoundException {
        checkInRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Check-in não encontrado com ID: " + id));
        
        checkInRepository.deleteById(id);
    }
    
    public Page<CheckIn> listarTodosCheckIns(Integer pagina, Integer tamanho) {
        return checkInRepository.findAll(PageRequest.of(pagina, tamanho));
    }
    
    public CheckIn buscarCheckInAtivoPorPessoa(Long pessoaId) throws EntityNotFoundException, BusinessException {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + pessoaId));
        
        return checkInRepository.findCheckInAtivoPorPessoa(pessoa, LocalDateTime.now())
                .orElseThrow(() -> new BusinessException("Pessoa não possui check-in ativo"));
    }
}
