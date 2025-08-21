package br.com.Senior.Teste.BPM.repository;

import br.com.Senior.Teste.BPM.entity.CheckIn;
import br.com.Senior.Teste.BPM.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    
    List<CheckIn> findByPessoa(Pessoa pessoa);
    
    List<CheckIn> findByDataEntradaBetween(LocalDateTime inicio, LocalDateTime fim);
    
    List<CheckIn> findByDataSaidaIsNull();
    
    List<CheckIn> findByDataSaidaIsNotNull();
    
    List<CheckIn> findByPessoaAndDataSaidaIsNull(Pessoa pessoa);
    
    List<CheckIn> findByPessoaAndDataSaidaIsNotNull(Pessoa pessoa);
}
