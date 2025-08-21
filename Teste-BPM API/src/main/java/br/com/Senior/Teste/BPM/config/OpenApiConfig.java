package br.com.Senior.Teste.BPM.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Check-in - Senior Sistemas")
                        .version("1.0.0")
                        .description("API para gerenciar check-ins e consultas de hóspedes em hotéis")
                        .contact(new Contact()
                                .name("Senior Sistemas")
                                .email("contato@senior.com.br")))
                .servers(List.of(
                        new Server().url("http://localhost:8080").description("Servidor Local"),
                        new Server().url("https://api-hotel.senior.com.br").description("Servidor de Produção")
                ));
    }
}
