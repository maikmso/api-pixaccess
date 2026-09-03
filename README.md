# PixAccess API

API REST para gerenciamento de pagamentos PIX, desenvolvida com Spring Boot, Spring Security (JWT) e PostgreSQL.

## Tecnologias

- Java 21
- Spring Boot 4.x
- Spring Security + JWT (JJWT 0.12)
- Spring Data JPA
- PostgreSQL
- Lombok
- Maven

## Pré-requisitos

- JDK 21+
- PostgreSQL rodando localmente
- Maven 3.8+ (ou use o mvnw incluído no projeto)

## Configuração

1. Entre na pasta do projeto:
```bash
cd pixaccess
```

2. Crie o banco de dados no PostgreSQL:
```sql
CREATE DATABASE pixaccess;
```

3. Copie o arquivo de configuração de exemplo:
```bash
cp src/main/resources/application.properties.example src/main/resources/application.properties
```

4. Edite application.properties com suas credenciais:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pixaccess
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
jwt.secret=sua_chave_secreta_com_pelo_menos_32_caracteres
```

## Como rodar

Dentro da pasta pixaccess:
```bash
./mvnw spring-boot:run
```

A API estará disponível em http://localhost:8080.

## Painel de teste (sem Postman/curl)

O projeto inclui uma interface web simples em http://localhost:8080/, servida automaticamente pelo Spring Boot. Ela permite cadastrar usuário, fazer login, criar pagamento e consultar, confirmar ou cancelar pagamentos direto pelo navegador, sem precisar montar requisições manualmente. O token JWT do login fica guardado e é reaproveitado nas chamadas seguintes.

## Endpoints

### Usuários

| Método | Rota                       | Auth | Descrição           |
|--------|----------------------------|------|---------------------|
| POST   | /usuarios/cadastrar        | Não  | Cadastrar usuário   |
| POST   | /usuarios/login            | Não  | Fazer login (JWT)   |
| PUT    | /usuarios/trocar-senha     | Sim  | Trocar senha        |

#### Cadastrar usuário
```json
POST /usuarios/cadastrar
{
  "nome": "João Silva",
  "cpf": "12345678901",
  "senha": "minhasenha"
}
```

#### Login
```json
POST /usuarios/login
{
  "cpf": "12345678901",
  "senha": "minhasenha"
}
```
Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "cpf": "12345678901"
}
```

### Pagamentos

Todos os endpoints de pagamento exigem o header:
Authorization: Bearer <token>

| Método | Rota                          | Descrição                    |
|--------|-------------------------------|------------------------------|
| POST   | /pagamentos                   | Criar pagamento              |
| GET    | /pagamentos/{id}              | Buscar pagamento por ID      |
| GET    | /pagamentos/cpf/{cpf}         | Listar pagamentos por CPF    |
| PATCH  | /pagamentos/{id}/confirmar    | Confirmar pagamento          |
| PATCH  | /pagamentos/{id}/cancelar     | Cancelar pagamento           |

#### Criar pagamento
```json
POST /pagamentos
Authorization: Bearer <token>
{
  "cpfPagador": "12345678901",
  "valor": 150.00,
  "chavePix": "email@exemplo.com",
  "descricao": "Pagamento referente ao pedido #123"
}
```

#### Status possíveis
- PENDENTE: criado, aguardando confirmação
- CONFIRMADO: pagamento confirmado
- CANCELADO: pagamento cancelado

## Tratamento de erros

Todos os erros retornam JSON padronizado:
```json
{
  "status": 400,
  "mensagem": "CPF já cadastrado",
  "timestamp": "2026-08-20T10:00:00"
}
```

## Rodando os testes

Dentro da pasta pixaccess:
```bash
./mvnw test
```

## Autor

Miquéias Santos ([GitHub](https://github.com/maikmso))
