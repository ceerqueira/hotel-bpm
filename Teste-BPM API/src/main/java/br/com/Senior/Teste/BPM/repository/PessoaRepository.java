package br.com.Senior.Teste.BPM.repository;

import br.com.Senior.Teste.BPM.entity.Pessoa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    
    Optional<Pessoa> findByDocumento(String documento);
    
    boolean existsByDocumento(String documento);
    
    @Query("SELECT p FROM Pessoa p WHERE p.nome LIKE %:nome% OR p.documento LIKE %:documento%")
    List<Pessoa> findByNomeOrDocumentoContaining(@Param("nome") String nome, @Param("documento") String documento);
    
    List<Pessoa> findByNomeContainingIgnoreCase(String nome);
    
    List<Pessoa> findByDocumentoContaining(String documento);
    
    // Busca pessoa por ID e nome (para validações específicas)
    Optional<Pessoa> findByIdAndNome(Long id, String nome);
}
