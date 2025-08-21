package br.com.Senior.Teste.BPM.service;

import br.com.Senior.Teste.BPM.entity.CheckIn;
import br.com.Senior.Teste.BPM.entity.Pessoa;
import br.com.Senior.Teste.BPM.exception.BusinessException;
import br.com.Senior.Teste.BPM.exception.EntityNotFoundException;
import br.com.Senior.Teste.BPM.repository.CheckInRepository;
import br.com.Senior.Teste.BPM.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
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
                                  LocalDateTime dataSaidaPrevista, Boolean adicionalVeiculo) throws EntityNotFoundException, BusinessException {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + pessoaId));

        if (dataEntrada.isAfter(dataSaidaPrevista)) {
            throw new BusinessException("Data de entrada não pode ser posterior à data de saída prevista");
        }
        
        if (dataEntrada.isBefore(LocalDateTime.now())) {
            throw new BusinessException("Data de entrada não pode ser anterior à data atual");
        }

        checkInRepository.findCheckInAtivoPorPessoa(pessoa, LocalDateTime.now())
                .ifPresent(checkInAtivo -> {
                    throw new BusinessException("Pessoa já possui um check-in ativo. ID do check-in: " + checkInAtivo.getId());
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
    
    public List<CheckIn> buscarCheckInsPorPessoa(Long pessoaId) throws EntityNotFoundException {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + pessoaId));
        return checkInRepository.findByPessoa(pessoa);
    }
    
    public List<CheckIn> buscarHospedesAtivos() {
        return checkInRepository.findHospedesAtivos(LocalDateTime.now());
    }
    
    public List<CheckIn> buscarHospedesFinalizados() {
        return checkInRepository.findHospedesFinalizados(LocalDateTime.now());
    }
    
    public List<CheckIn> buscarHospedesAtivosPorPessoa(Long pessoaId) throws EntityNotFoundException {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + pessoaId));
        return checkInRepository.findHospedesAtivosPorPessoa(pessoa, LocalDateTime.now());
    }
    
    public List<CheckIn> buscarHospedesFinalizadosPorPessoa(Long pessoaId) throws EntityNotFoundException {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + pessoaId));
        return checkInRepository.findHospedesFinalizadosPorPessoa(pessoa,  LocalDateTime.now());
    }
    
    public CheckIn buscarCheckInPorId(Long id) throws EntityNotFoundException {
        return checkInRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Check-in não encontrado com ID: " + id));
    }

    public CheckIn atualizarCheckIn(Long id, CheckIn checkInAtualizado) throws EntityNotFoundException {
        CheckIn checkIn = checkInRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Check-in não encontrado com ID: " + id));


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
    
    public List<CheckIn> listarTodosCheckIns() {
        return checkInRepository.findAll();
    }
    
    public CheckIn buscarCheckInAtivoPorPessoa(Long pessoaId) throws EntityNotFoundException, BusinessException {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + pessoaId));
        
        return checkInRepository.findCheckInAtivoPorPessoa(pessoa, LocalDateTime.now())
                .orElseThrow(() -> new BusinessException("Pessoa não possui check-in ativo"));
    }
}
