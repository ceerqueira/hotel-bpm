package br.com.Senior.Teste.BPM.repository;

import br.com.Senior.Teste.BPM.entity.CheckIn;
import br.com.Senior.Teste.BPM.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    
    List<CheckIn> findByPessoa(Pessoa pessoa);
    
    List<CheckIn> findByDataEntradaBetween(LocalDateTime inicio, LocalDateTime fim);
    
    // Busca hóspedes ativos baseado na hora atual
    @Query("SELECT c FROM CheckIn c WHERE c.dataEntrada <= :horaAtual AND c.dataSaidaPrevista > :horaAtual")
    List<CheckIn> findHospedesAtivos(@Param("horaAtual") LocalDateTime horaAtual);
    
    // Busca hóspedes que já saíram (data de saída prevista já passou)
    @Query("SELECT c FROM CheckIn c WHERE c.dataSaidaPrevista <= :horaAtual")
    List<CheckIn> findHospedesFinalizados(@Param("horaAtual") LocalDateTime horaAtual);
    
    // Busca hóspedes ativos por pessoa
    @Query("SELECT c FROM CheckIn c WHERE c.pessoa = :pessoa AND c.dataEntrada <= :horaAtual AND c.dataSaidaPrevista > :horaAtual")
    List<CheckIn> findHospedesAtivosPorPessoa(@Param("pessoa") Pessoa pessoa, @Param("horaAtual") LocalDateTime horaAtual);
    
    // Busca hóspedes finalizados por pessoa
    @Query("SELECT c FROM CheckIn c WHERE c.pessoa = :pessoa AND c.dataSaidaPrevista <= :horaAtual")
    List<CheckIn> findHospedesFinalizadosPorPessoa(@Param("pessoa") Pessoa pessoa, @Param("horaAtual") LocalDateTime horaAtual);
}
