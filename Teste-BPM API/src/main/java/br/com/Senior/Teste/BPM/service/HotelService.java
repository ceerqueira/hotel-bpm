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
    
    public CheckIn realizarCheckIn(Long pessoaId, LocalDateTime dataEntrada, Boolean adicionalVeiculo) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoaId);
        if (pessoaOpt.isEmpty()) {
            throw new RuntimeException("Pessoa não encontrada");
        }
        
        CheckIn checkIn = new CheckIn();
        checkIn.setPessoa(pessoaOpt.get());
        checkIn.setDataEntrada(dataEntrada);
        checkIn.setAdicionalVeiculo(adicionalVeiculo != null ? adicionalVeiculo : false);
        checkIn.setValorTotal(null); // Valor será calculado no checkout
        
        return checkInRepository.save(checkIn);
    }
    
    public CheckIn realizarCheckOut(Long checkInId, LocalDateTime dataSaida) {
        Optional<CheckIn> checkInOpt = checkInRepository.findById(checkInId);
        if (checkInOpt.isEmpty()) {
            throw new RuntimeException("Check-in não encontrado");
        }
        
        CheckIn checkIn = checkInOpt.get();
        checkIn.setDataSaida(dataSaida);
        
        // Calcula o valor total
        BigDecimal valorTotal = pricingService.calcularValorTotal(
            checkIn.getDataEntrada(), 
            dataSaida, 
            checkIn.getAdicionalVeiculo()
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
    
    public List<CheckIn> buscarCheckInsAtivos() {
        return checkInRepository.findByDataSaidaIsNull();
    }
    
    public List<CheckIn> buscarCheckInsFinalizados() {
        return checkInRepository.findByDataSaidaIsNotNull();
    }
    
    public List<CheckIn> buscarCheckInsAtivosPorPessoa(Long pessoaId) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoaId);
        if (pessoaOpt.isEmpty()) {
            throw new RuntimeException("Pessoa não encontrada");
        }
        
        return checkInRepository.findByPessoaAndDataSaidaIsNull(pessoaOpt.get());
    }
    
    public List<CheckIn> buscarCheckInsFinalizadosPorPessoa(Long pessoaId) {
        Optional<Pessoa> pessoaOpt = pessoaRepository.findById(pessoaId);
        if (pessoaOpt.isEmpty()) {
            throw new RuntimeException("Pessoa não encontrada");
        }
        
        return checkInRepository.findByPessoaAndDataSaidaIsNotNull(pessoaOpt.get());
    }
}
