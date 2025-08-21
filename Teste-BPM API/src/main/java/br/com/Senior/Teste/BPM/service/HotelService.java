package br.com.Senior.Teste.BPM.service;

import br.com.Senior.Teste.BPM.entity.CheckIn;
import br.com.Senior.Teste.BPM.entity.Pessoa;
import br.com.Senior.Teste.BPM.repository.CheckInRepository;
import br.com.Senior.Teste.BPM.repository.PessoaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;

@Service
public class HotelService {
    
    @Autowired
    private PessoaRepository pessoaRepository;
    
    @Autowired
    private CheckInRepository checkInRepository;
    
    @Autowired
    private PricingService pricingService;
    
    public Pessoa cadastrarPessoa(Pessoa pessoa) {
        if (pessoaRepository.existsByDocumento(pessoa.getDocumento())) {
            throw new RuntimeException("Pessoa com documento " + pessoa.getDocumento() + " já cadastrada");
        }
        return pessoaRepository.save(pessoa);
    }
    
    public List<Pessoa> buscarPessoas(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return pessoaRepository.findAll();
        }
        
        String termoBusca = termo.trim();
        
        // Tenta buscar por documento primeiro
        List<Pessoa> resultadoPorDocumento = pessoaRepository.findByDocumentoContaining(termoBusca);
        if (!resultadoPorDocumento.isEmpty()) {
            return resultadoPorDocumento;
        }
        
        // Se não encontrar por documento, busca por nome
        return pessoaRepository.findByNomeContainingIgnoreCase(termoBusca);
    }
    
    public CheckIn realizarCheckIn(Long pessoaId, LocalDateTime dataEntrada, LocalDateTime dataSaidaPrevista, Boolean adicionalVeiculo) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoaId);
        if (pessoaOpt.isEmpty()) {
            throw new RuntimeException("Pessoa não encontrada");
        }
        
        if (dataEntrada.isAfter(dataSaidaPrevista)) {
            throw new RuntimeException("Data de entrada não pode ser posterior à data de saída prevista");
        }
        
        CheckIn checkIn = new CheckIn();
        checkIn.setPessoa(pessoaOpt.get());
        checkIn.setDataEntrada(dataEntrada);
        checkIn.setDataSaidaPrevista(dataSaidaPrevista);
        checkIn.setAdicionalVeiculo(adicionalVeiculo != null ? adicionalVeiculo : false);
        
        // Calcula o valor total baseado no período
        BigDecimal valorTotal = pricingService.calcularValorTotal(
            dataEntrada, 
            dataSaidaPrevista, 
            adicionalVeiculo != null ? adicionalVeiculo : false
        );
        checkIn.setValorTotal(valorTotal);
        
        return checkInRepository.save(checkIn);
    }
    
    public List<CheckIn> buscarCheckInsPorPessoa(Long pessoaId) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoaId);
        if (pessoaOpt.isEmpty()) {
            throw new RuntimeException("Pessoa não encontrada");
        }
        
        return checkInRepository.findByPessoa(pessoaOpt.get());
    }
    
    public List<CheckIn> buscarHospedesAtivos() {
        LocalDateTime horaAtual = LocalDateTime.now();
        return checkInRepository.findHospedesAtivos(horaAtual);
    }
    
    public List<CheckIn> buscarHospedesFinalizados() {
        LocalDateTime horaAtual = LocalDateTime.now();
        return checkInRepository.findHospedesFinalizados(horaAtual);
    }
    
    public List<CheckIn> buscarHospedesAtivosPorPessoa(Long pessoaId) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoaId);
        if (pessoaOpt.isEmpty()) {
            throw new RuntimeException("Pessoa não encontrada");
        }
        
        LocalDateTime horaAtual = LocalDateTime.now();
        return checkInRepository.findHospedesAtivosPorPessoa(pessoaOpt.get(), horaAtual);
    }
    
    public List<CheckIn> buscarHospedesFinalizadosPorPessoa(Long pessoaId) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoaId);
        if (pessoaOpt.isEmpty()) {
            throw new RuntimeException("Pessoa não encontrada");
        }
        
        LocalDateTime horaAtual = LocalDateTime.now();
        return checkInRepository.findHospedesFinalizadosPorPessoa(pessoaOpt.get(), horaAtual);
    }
}
