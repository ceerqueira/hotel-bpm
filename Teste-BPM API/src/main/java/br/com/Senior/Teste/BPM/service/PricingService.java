package br.com.Senior.Teste.BPM.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
public class PricingService {
    
    private static final BigDecimal PRECO_DIARIA_SEMANA = new BigDecimal("120.00");
    private static final BigDecimal PRECO_DIARIA_FIM_SEMANA = new BigDecimal("150.00");
    private static final BigDecimal ADICIONAL_ESTACIONAMENTO_SEMANA = new BigDecimal("15.00");
    private static final BigDecimal ADICIONAL_ESTACIONAMENTO_FIM_SEMANA = new BigDecimal("20.00");
    private static final LocalTime HORARIO_LIMITE_CHECKOUT = LocalTime.of(16, 30);
    
    public BigDecimal calcularValorTotal(LocalDateTime dataEntrada, LocalDateTime dataSaida, Boolean adicionalVeiculo) {
        if (dataSaida == null) {
            return BigDecimal.ZERO;
        }
        
        // Se o checkout for após 16:30, adiciona uma noite extra
        if (dataSaida.toLocalTime().isAfter(HORARIO_LIMITE_CHECKOUT)) {
            dataSaida = dataSaida.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        }
        
        BigDecimal valorTotal = BigDecimal.ZERO;
        LocalDateTime dataAtual = dataEntrada.toLocalDate().atStartOfDay();
        LocalDateTime dataFim = dataSaida.toLocalDate().atStartOfDay();
        
        while (dataAtual.isBefore(dataFim)) {
            DayOfWeek diaSemana = dataAtual.getDayOfWeek();
            
            // Verifica se é fim de semana (Sábado ou Domingo)
            boolean isFimDeSemana = diaSemana == DayOfWeek.SATURDAY || diaSemana == DayOfWeek.SUNDAY;
            
            BigDecimal precoDiaria = isFimDeSemana ? PRECO_DIARIA_FIM_SEMANA : PRECO_DIARIA_SEMANA;
            BigDecimal adicionalEstacionamento = isFimDeSemana ? ADICIONAL_ESTACIONAMENTO_FIM_SEMANA : ADICIONAL_ESTACIONAMENTO_SEMANA;
            
            valorTotal = valorTotal.add(precoDiaria);
            
            if (adicionalVeiculo != null && adicionalVeiculo) {
                valorTotal = valorTotal.add(adicionalEstacionamento);
            }
            
            dataAtual = dataAtual.plusDays(1);
        }
        
        return valorTotal;
    }
    
    public Long calcularNumeroDias(LocalDateTime dataEntrada, LocalDateTime dataSaida) {
        if (dataSaida == null) {
            return 0L;
        }
        
        // Se o checkout for após 16:30, adiciona uma noite extra
        if (dataSaida.toLocalTime().isAfter(HORARIO_LIMITE_CHECKOUT)) {
            dataSaida = dataSaida.plusDays(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        }
        
        return ChronoUnit.DAYS.between(
            dataEntrada.toLocalDate(), 
            dataSaida.toLocalDate()
        );
    }
}
