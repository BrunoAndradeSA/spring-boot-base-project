# 🚀 Spring Boot Base Project

![Java](https://img.shields.io/badge/Java-21-blue)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.x-brightgreen)
![Build](https://img.shields.io/badge/build-passing-success)
![License](https://img.shields.io/badge/license-MIT-lightgrey)
![Status](https://img.shields.io/badge/status-em%20desenvolvimento-yellow)

Projeto base para desenvolvimento de APIs REST utilizando Spring Boot, com foco em boas práticas, segurança, versionamento de banco de dados e estrutura pronta para produção.

---

## 📖 Sobre o projeto

Este projeto foi criado como um boilerplate para acelerar o desenvolvimento de APIs Java com Spring Boot, já incluindo:

- Estrutura preparada para autenticação com JWT
- Integração com banco de dados PostgreSQL
- Versionamento de banco com Flyway
- Documentação automática com OpenAPI (Swagger)
- Mapeamento de objetos com MapStruct
- Redução de boilerplate com Lombok

Além disso, o projeto já está configurado para gerar artefatos `.WAR`, permitindo deploy em servidores externos como Apache Tomcat.

---

## 🛠️ Tecnologias e dependências

### 🔧 Backend
- Java 21
- Spring Boot 4.x

### 📦 Principais dependências

- Spring Web — Criação de APIs REST
- Spring Data JPA — Persistência de dados
- Spring Security — Autenticação e autorização
- Spring Boot Actuator — Monitoramento da aplicação
- Spring Boot DevTools — Hot reload em desenvolvimento

### 🗄️ Banco de dados
- PostgreSQL
- Flyway (migração/versionamento de schema)

### 🔐 Segurança
- JWT (JJWT)

### 🔄 Mapeamento e utilitários
- MapStruct — Mapeamento entre DTOs e entidades
- Lombok — Redução de código boilerplate

### 📚 Documentação
- Springdoc OpenAPI (Swagger UI)

### 🧪 Testes
- Spring Boot Starter Test

### 🎲 Dados fake
- Datafaker

---

## ▶️ Como executar o projeto

### Pré-requisitos

- Java 21 instalado
- Maven instalado
- PostgreSQL (opcional, dependendo da configuração atual)

### Clonar o projeto

```bash
git clone https://github.com/BrunoAndradeSA/spring-boot-base-project.git

cd spring-boot-base-project

mvn spring-boot:run
```

### Criar um arquivo `.env` na raíz do projeto

```env
SERVER_PORT=Porta de execução HTTP

DATASOURCE_URL=URL JDBC do seu banco de dados
DATASOURCE_USERNAME=Username do seu banco de dados
DATASOURCE_PASSWORD=Senha do seu banco de dados
DATASOURCE_DRIVER_CLASSNAME=Classname do driver de conexã́o com o banco de dados

FLYWAY_ENABLED=Habilitar migrations do Flyway (True ou False)
FLYWAY_BASELINE_ON_MIGRATE=Criar o baseline do flyway na migrations (True ou False)

API_SECRET_KEY=Secret Key para criação dos tokens JWT
API_EXPIRATION_TOKEN=Tempo de expiração do token (em minutos)
```

### Executar o projeto

```bash
mvn spring-boot:run
```

A aplicação estará disponível em:

http://localhost:8080

---

## 📦 Como gerar o .WAR

```bash
mvn clean package
```

Arquivo gerado:

```
target/spring-boot-base-project-0.0.1-SNAPSHOT.war
```

---

## 🚀 Deploy

1. Instale um servidor como o Apache Tomcat
2. Copie o arquivo `.war` para a pasta `/webapps`
3. Inicie o servidor

---

## ⚠️ Observações importantes

- Tomcat está como `provided`
- Configure o banco no `application.yml` ou `.env`
- Flyway roda automaticamente (se configurado)
- DevTools apenas em dev

---

## 📑 Documentação da API

Swagger UI:

http://localhost:8080/swagger-ui.html  
ou  
http://localhost:8080/swagger-ui/index.html

OpenAPI JSON:

http://localhost:8080/v3/api-docs

---

## 🗂️ Estrutura geral

```
src/
 ├── main/
 │   ├── java/
 │   └── resources/
 └── test/
```

---

## 🤝 Contribuição

Abra issues ou pull requests 🚀

---

## 📄 Licença

MIT