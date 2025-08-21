package br.com.Senior.Teste.BPM.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PricingServiceTest {

    private PricingService pricingService;

    @BeforeEach
    void setUp() {
        pricingService = new PricingService();
    }

    @Test
    void testCalcularValorTotal_Weekday_NoParking() {
        // Segunda-feira, 1 noite, sem estacionamento
        LocalDateTime entrada = LocalDateTime.of(2024, 1, 15, 14, 0); // Segunda-feira
        LocalDateTime saida = LocalDateTime.of(2024, 1, 16, 12, 0); // Terça-feira
        
        BigDecimal valor = pricingService.calcularValorTotal(entrada, saida, false);
        
        assertEquals(new BigDecimal("120.00"), valor);
    }

    @Test
    void testCalcularValorTotal_Weekend_WithParking() {
        // Sábado, 1 noite, com estacionamento
        LocalDateTime entrada = LocalDateTime.of(2024, 1, 20, 14, 0); // Sábado
        LocalDateTime saida = LocalDateTime.of(2024, 1, 21, 12, 0); // Domingo
        
        BigDecimal valor = pricingService.calcularValorTotal(entrada, saida, true);
        
        assertEquals(new BigDecimal("170.00"), valor); // 150 + 20
    }

    @Test
    void testCalcularValorTotal_LateCheckout() {
        // Checkout após 16:30 deve adicionar uma noite extra
        LocalDateTime entrada = LocalDateTime.of(2024, 1, 15, 14, 0); // Segunda-feira
        LocalDateTime saida = LocalDateTime.of(2024, 1, 16, 17, 0); // Terça-feira após 16:30
        
        BigDecimal valor = pricingService.calcularValorTotal(entrada, saida, false);
        
        assertEquals(new BigDecimal("240.00"), valor); // 2 noites * 120
    }

    @Test
    void testCalcularValorTotal_MixedWeek() {
        // Sexta para domingo (2 noites)
        LocalDateTime entrada = LocalDateTime.of(2024, 1, 19, 14, 0); // Sexta-feira
        LocalDateTime saida = LocalDateTime.of(2024, 1, 21, 12, 0); // Domingo
        
        BigDecimal valor = pricingService.calcularValorTotal(entrada, saida, true);
        
        // Sexta (120 + 15) + Sábado (150 + 20) = 135 + 170 = 305
        assertEquals(new BigDecimal("305.00"), valor);
    }

    @Test
    void testCalcularNumeroDias() {
        LocalDateTime entrada = LocalDateTime.of(2024, 1, 15, 14, 0);
        LocalDateTime saida = LocalDateTime.of(2024, 1, 17, 12, 0);
        
        Long dias = pricingService.calcularNumeroDias(entrada, saida);
        
        assertEquals(2L, dias);
    }
}
