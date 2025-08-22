package br.com.Senior.Teste.BPM.repository;

import br.com.Senior.Teste.BPM.entity.CheckIn;
import br.com.Senior.Teste.BPM.entity.Pessoa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    
    Page<CheckIn> findAll(Pageable pageable);
    Page<CheckIn> findByPessoa(Pessoa pessoa, Pageable pageable);

    @Query("SELECT c FROM CheckIn c WHERE c.dataEntrada <= :horaAtual AND c.dataSaidaPrevista > :horaAtual")
    Page<CheckIn> findHospedesAtivos(@Param("horaAtual") LocalDateTime horaAtual, Pageable pageable);
    
    @Query("SELECT c FROM CheckIn c WHERE c.dataSaidaPrevista <= :horaAtual")
    Page<CheckIn> findHospedesFinalizados(@Param("horaAtual") LocalDateTime horaAtual, Pageable pageable);
    
    @Query("SELECT c FROM CheckIn c WHERE c.pessoa = :pessoa AND c.dataEntrada <= :horaAtual AND c.dataSaidaPrevista > :horaAtual")
    Page<CheckIn> findHospedesAtivosPorPessoa(@Param("pessoa") Pessoa pessoa, @Param("horaAtual") LocalDateTime horaAtual, Pageable pageable);
    
    @Query("SELECT c FROM CheckIn c WHERE c.pessoa = :pessoa AND c.dataSaidaPrevista <= :horaAtual")
    Page<CheckIn> findHospedesFinalizadosPorPessoa(@Param("pessoa") Pessoa pessoa, @Param("horaAtual") LocalDateTime horaAtual, Pageable pageable);
    
    @Query("SELECT c FROM CheckIn c WHERE c.pessoa = :pessoa AND c.dataEntrada <= :horaAtual AND c.dataSaidaPrevista > :horaAtual")
    Optional<CheckIn> findCheckInAtivoPorPessoa(@Param("pessoa") Pessoa pessoa, @Param("horaAtual") LocalDateTime horaAtual);
    
    @Query("SELECT c FROM CheckIn c WHERE c.pessoa = :pessoa AND " +
           "(c.dataEntrada < :dataSaidaPrevista AND c.dataSaidaPrevista > :dataEntrada)")
    List<CheckIn> findCheckInsComConflitoDeHorario(@Param("pessoa") Pessoa pessoa, 
                                                   @Param("dataEntrada") LocalDateTime dataEntrada, 
                                                   @Param("dataSaidaPrevista") LocalDateTime dataSaidaPrevista);
    
    @Query("SELECT c FROM CheckIn c WHERE c.pessoa = :pessoa AND c.id != :checkInId AND " +
           "(c.dataEntrada < :dataSaidaPrevista AND c.dataSaidaPrevista > :dataEntrada)")
    List<CheckIn> findCheckInsComConflitoDeHorarioExcluindoCheckIn(@Param("pessoa") Pessoa pessoa, 
                                                                   @Param("dataEntrada") LocalDateTime dataEntrada, 
                                                                   @Param("dataSaidaPrevista") LocalDateTime dataSaidaPrevista,
                                                                   @Param("checkInId") Long checkInId);
}
