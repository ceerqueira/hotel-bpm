# Hotel Check-in System API

Sistema de check-in de hotel desenvolvido com Spring Boot que permite gerenciar hóspedes, realizar check-ins e check-outs, e consultar informações sobre estadias.

## Funcionalidades

### CRUD de Pessoas
- Cadastrar pessoas (nome, documento, telefone)
- Buscar pessoas por nome ou documento
- Atualizar informações de pessoas
- Deletar pessoas

### Check-in e Check-out
- Realizar check-in de hóspedes
- Realizar check-out com cálculo automático de valores
- Buscar check-ins por pessoa

### Consultas
- Consultar hóspedes ativos (ainda no hotel)
- Consultar hóspedes que já fizeram check-out
- Consultar histórico de estadias por pessoa
- Exibir valor total gasto no hotel

## Regras de Negócio

### Preços
- **Segunda a Sexta**: R$ 120,00 por noite
- **Fins de semana**: R$ 150,00 por noite
- **Estacionamento (Segunda a Sexta)**: R$ 15,00 por dia
- **Estacionamento (Fins de semana)**: R$ 20,00 por dia
- **Check-out após 16:30**: Cobra uma noite extra

## Endpoints da API

### Pessoas

#### Cadastrar Pessoa
```
POST /api/hotel/pessoas
Content-Type: application/json

{
  "nome": "João Silva",
  "documento": "12345678901",
  "telefone": "11999999999"
}
```

#### Buscar Pessoas
```
GET /api/hotel/pessoas?termo=joão
GET /api/hotel/pessoas?termo=12345678901
GET /api/hotel/pessoas (retorna todas)
```

#### Buscar Pessoa por ID
```
GET /api/hotel/pessoas/{id}
```

#### Atualizar Pessoa
```
PUT /api/hotel/pessoas/{id}
Content-Type: application/json

{
  "nome": "João Silva Santos",
  "documento": "12345678901",
  "telefone": "11999999999"
}
```

#### Deletar Pessoa
```
DELETE /api/hotel/pessoas/{id}
```

### Check-in e Check-out

#### Realizar Check-in
```
POST /api/hotel/check-in
Content-Type: application/json

{
  "pessoaId": 1,
  "dataEntrada": "2024-01-15T14:00:00",
  "adicionalVeiculo": true
}
```

#### Realizar Check-out
```
PUT /api/hotel/check-out/{checkInId}
Content-Type: application/json

{
  "dataSaida": "2024-01-16T12:00:00"
}
```

### Consultas

#### Hóspedes Ativos
```
GET /api/hotel/check-ins/ativos
```

#### Hóspedes que Fizeram Check-out
```
GET /api/hotel/check-ins/finalizados
```

#### Check-ins por Pessoa
```
GET /api/hotel/pessoas/{pessoaId}/check-ins
```

#### Check-ins Ativos por Pessoa
```
GET /api/hotel/pessoas/{pessoaId}/check-ins/ativos
```

#### Check-ins Finalizados por Pessoa
```
GET /api/hotel/pessoas/{pessoaId}/check-ins/finalizados
```

## Estrutura de Dados

### PessoaDTO
```json
{
  "id": 1,
  "nome": "João Silva",
  "documento": "12345678901",
  "telefone": "11999999999"
}
```

### CheckInDTO
```json
{
  "id": 1,
  "pessoa": {
    "id": 1,
    "nome": "João Silva",
    "documento": "12345678901",
    "telefone": "11999999999"
  },
  "dataEntrada": "2024-01-15T14:00:00",
  "dataSaida": "2024-01-16T12:00:00",
  "adicionalVeiculo": true,
  "valorTotal": 135.00
}
```

### ConsultaHospedesDTO
```json
{
  "id": 1,
  "pessoa": {
    "id": 1,
    "nome": "João Silva",
    "documento": "12345678901",
    "telefone": "11999999999"
  },
  "dataEntrada": "2024-01-15T14:00:00",
  "dataSaida": "2024-01-16T12:00:00",
  "adicionalVeiculo": true,
  "valorTotal": 135.00,
  "status": "CHECKOUT",
  "numeroDias": 1
}
```

## Configuração

### Banco de Dados
- MySQL 8.0+
- Database: `hotel_bpm`
- Usuário: `root`
- Senha: `root`

### Aplicação
- Java 21
- Spring Boot 3.5.4
- Maven

## Executando a Aplicação

1. Certifique-se de que o MySQL está rodando
2. Execute o comando:
```bash
./mvnw spring-boot:run
```

3. A aplicação estará disponível em: `http://localhost:8080`

## Testes

Execute os testes com:
```bash
./mvnw test
```

## Tecnologias Utilizadas

- **Spring Boot**: Framework principal
- **Spring Data JPA**: Persistência de dados
- **MySQL**: Banco de dados
- **Hibernate**: ORM e gerenciamento de schema
- **Lombok**: Redução de boilerplate
- **Maven**: Gerenciamento de dependências
