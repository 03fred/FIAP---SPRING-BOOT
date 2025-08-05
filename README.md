
# FIAP PosTech - Backend de Usuários

Este projeto é um backend desenvolvido em Spring Boot para gerenciamento de usuários, incluindo cadastro, autenticação, atualização e troca de senha. O sistema utiliza Docker Compose para facilitar a execução do ambiente com banco de dados MySQL. Além disso, possui controle de usuários, gerenciamento de restaurantes, inserção de menus e itens do menu, gerenciamento de perfis, controle de acessos por meio de token de autenticação e recuperação de senha via e-mail.

## 🛠️ Tecnologias

- Java 21  
- Spring Boot  
- MySQL  
- Docker + Docker Compose  
- Maven

## 🚀 Como executar o projeto

### 1. Gerar o pacote da aplicação

```bash
mvn clean package -DskipTests
```

> Obs: `-DskipTests` é opcional, use se não quiser rodar os testes agora.

### 2. Construir a imagem Docker

```bash
docker compose build
```

### 3. Subir os containers

```bash
$env:MAIL_PASSWORD = "orgg bxjb qfir enjn"; docker compose up -d
```
