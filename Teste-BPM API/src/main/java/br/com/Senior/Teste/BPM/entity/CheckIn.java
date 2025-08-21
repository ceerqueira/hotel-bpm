package br.com.Senior.Teste.BPM.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "check_in")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckIn {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;
    
    @Column(name = "data_entrada", nullable = false)
    private LocalDateTime dataEntrada;
    
    @Column(name = "data_saida_prevista", nullable = false)
    private LocalDateTime dataSaidaPrevista;
    
    @Column(name = "adicional_veiculo", nullable = false)
    private Boolean adicionalVeiculo = false;
    
    @Column(name = "valor_total", precision = 10, scale = 2)
    private BigDecimal valorTotal;
}
