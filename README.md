# 🏨 Hotel BPM - Sistema de Check-in

Sistema de gerenciamento de check-in para hotéis desenvolvido como teste técnico.

## 🚀 Como executar

### Pré-requisitos
- Docker
- Docker Compose

### Execução
```bash
docker-compose up --build
```

### Acesso
- **Aplicação:** http://localhost
- **API Swagger:** http://localhost:8080/swagger-ui.html

## 📋 Funcionalidades

- CRUD de pessoas (hóspedes)
- Sistema de check-in com busca por nome/documento
- Consulta de hóspedes no hotel e que já saíram
- Cálculo automático de valores

## 💰 Regras de Negócio

- **Diária:** R$ 120,00 (segunda à sexta) / R$ 150,00 (finais de semana)
- **Garagem:** R$ 15,00 (segunda à sexta) / R$ 20,00 (finais de semana)
- **Check-out após 16:30h:** diária extra

## 🛠️ Tecnologias

- **Frontend:** Angular 13
- **Backend:** Spring Boot 3.5.4
- **Banco:** MySQL 8.0
- **Containerização:** Docker + Docker Compose

## 📚 API Documentation

Acesse http://localhost:8080/swagger-ui.html para estudar a API completa.

---

**Teste Técnico - Senior Sistemas**
