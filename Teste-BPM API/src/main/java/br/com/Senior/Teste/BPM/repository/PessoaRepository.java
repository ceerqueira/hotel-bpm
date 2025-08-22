package br.com.Senior.Teste.BPM.repository;

import br.com.Senior.Teste.BPM.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    
    Optional<Pessoa> findByDocumento(String documento);

    @Query("SELECT p FROM Pessoa p WHERE p.nome LIKE %:nome% OR p.documento LIKE %:documento%")
    Page<Pessoa> findByNomeOrDocumentoContaining(@Param("nome") String nome, @Param("documento") String documento, Pageable pageable);
}
