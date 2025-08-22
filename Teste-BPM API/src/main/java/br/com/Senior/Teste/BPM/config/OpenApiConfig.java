package br.com.Senior.Teste.BPM.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Check-in - Teste Técnico - Senior Sistemas")
                        .version("1.0.0")
                        .description("API para gerenciar check-ins e consultas de hóspedes em hotéis"));
    }
}
