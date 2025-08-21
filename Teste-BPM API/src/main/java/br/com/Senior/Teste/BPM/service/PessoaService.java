package br.com.Senior.Teste.BPM.service;

import br.com.Senior.Teste.BPM.entity.Pessoa;
import br.com.Senior.Teste.BPM.exception.BusinessException;
import br.com.Senior.Teste.BPM.exception.EntityNotFoundException;
import br.com.Senior.Teste.BPM.repository.PessoaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PessoaService {
    
    private final PessoaRepository pessoaRepository;
    
        public Pessoa cadastrarPessoa(Pessoa pessoa) throws BusinessException {
        this.pessoaRepository.findByDocumento(pessoa.getDocumento());
        pessoaRepository.findByDocumento(pessoa.getDocumento())
                .ifPresent(existingPessoa -> {
                    throw new BusinessException("Já existe uma pessoa cadastrada com o documento: " + pessoa.getDocumento());
                });

        return pessoaRepository.save(pessoa);
    }
    
    public List<Pessoa> buscarPessoas(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return pessoaRepository.findAll();
        }
        return pessoaRepository.findByNomeOrDocumentoContaining(termo, termo);
    }
    
    public Pessoa buscarPessoaPorId(Long id) throws EntityNotFoundException {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + id));
    }
    
    public Pessoa atualizarPessoa(Long id, Pessoa pessoa) throws EntityNotFoundException {
        this.pessoaRepository.findById(id);
        return pessoaRepository.save(pessoa);
    }
    
    public void deletarPessoa(Long id) throws EntityNotFoundException {
        this.pessoaRepository.findById(id);
        pessoaRepository.deleteById(id);
    }

}
