package br.com.Senior.Teste.BPM.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pessoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "nome", nullable = false, length = 255)
    private String nome;
    
    @Column(name = "documento", nullable = false, length = 20, unique = true)
    private String documento;
    
    @Column(name = "telefone", length = 20)
    private String telefone;
}
