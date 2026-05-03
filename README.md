# 💳 API PixAccess 

## 📌 Sobre o projeto

O PixAccess é uma API desenvolvida em Java com Spring Boot que simula um fluxo de pagamento via PIX.

O sistema permite que um cliente realize um pagamento e, após a confirmação, um usuário seja criado automaticamente com base nos dados fornecidos.

Este projeto foi desenvolvido com foco em aprendizado de backend, arquitetura em camadas e integração com banco de dados.

---

## 🚀 Funcionalidades

- Criar pagamento via API
- Confirmar pagamento
- Atualizar status do pagamento
- Criar usuário automaticamente após pagamento
- Gerar senha padrão baseada no CPF

---

## 🧠 Regras de negócio

- Todo pagamento inicia com status **PENDENTE**
- Após confirmação, o status muda para **PAGO**
- Ao confirmar pagamento:
  - O sistema verifica se já existe um usuário com o CPF
  - Caso não exista, cria automaticamente
- A senha padrão do usuário é composta pelos **4 últimos dígitos do CPF**
- O usuário deve alterar a senha posteriormente

---

## 🏗️ Arquitetura

O projeto segue o padrão em camadas:

- **Controller**: recebe requisições HTTP
- **Service**: contém a lógica de negócio
- **Repository**: faz acesso ao banco
- **Database**: armazenamento dos dados

---

## 🛠️ Tecnologias utilizadas

- Java 21
- Spring Boot
- Spring Data JPA
- PostgreSQL
- Maven
- Postman
