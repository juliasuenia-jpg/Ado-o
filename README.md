# Plataforma de Adoção de Animais

Projeto final de **Júlia Suênia, Letícia Moura e Mariana Beatriz**.
Plataforma web que conecta ONGs de proteção animal e protetores independentes a possíveis adotantes,
cobrindo todo o fluxo: busca, triagem, agendamento da visita e acompanhamento da solicitação.

**Stack:** Java 25 · Spring Boot 4.2 (Web MVC, Data JPA, Security) · Thymeleaf · **MySQL 8**

## Requisitos do projeto × funcionalidades

| Requisito | Funcionalidade | Onde está |
|---|---|---|
| Controle de acesso e autenticação | Cadastro e login com 3 perfis: visitante sem login, **Adotante** e **Administrador** (ONG/gestor) | `SecurityConfig`, `AuthController` |
| Persistência / CRUD completo | Gestão do perfil do animal: criar, listar, editar e remover, com foto e ficha médica | `AdminController`, `AnimalService`, `admin/animais.html` |
| Mapeamento de consultas / filtros | Busca avançada por espécie, porte, idade, sexo, cidade e convivência com outros pets | `AnimalService.buscar`, `animais/lista.html` |
| Regras de negócio e processos | Questionário de responsabilidade antes de liberar o agendamento da visita, com nota automática de compatibilidade | `AdocaoController`, `AdocaoService` |
| Acompanhamento de status | Dashboard da ONG altera o status (Pendente, Em análise, Aprovado, Recusado); o adotante acompanha em "Minhas solicitações" | `AdminController`, `adocao/minhas.html` |

## Como executar

1. Tenha **JDK 25** e **MySQL 8** instalados e o MySQL rodando.
2. Informe o usuário/senha do MySQL (padrão: `root` / `root`). Pode ser por variável de ambiente:
   ```bash
   export DB_USER=root
   export DB_PASSWORD=sua_senha
   ```
   ou editando `src/main/resources/application.properties`.
3. Rode o projeto:
   ```bash
   ./mvnw spring-boot:run        # Windows: mvnw.cmd spring-boot:run
   ```
4. Abra http://localhost:8080

O banco `adocao` e as tabelas são criados automaticamente na primeira execução
(`createDatabaseIfNotExist=true` + `ddl-auto=update`). O script `banco/schema.sql` documenta o modelo
e pode ser usado se preferirem criar o banco manualmente.

### Acesso inicial

| Perfil | E-mail | Senha |
|---|---|---|
| Administrador | `admin@adocao.com` | `admin123` |

Adotantes se cadastram em **Criar conta**. Na primeira execução também são cadastrados 6 animais de exemplo
(desligue com `app.seed=false`). **Troque a senha do administrador antes de publicar o sistema.**

## Fluxo da adoção

1. Visitante navega e filtra os animais, sem precisar de login.
2. Adotante entra e abre **Quero adotar**.
3. Responde ao questionário de responsabilidade (moradia, rotina, outros animais, motivação, termo) e sugere a data da visita.
4. A solicitação nasce **Pendente**, com compatibilidade de 0 a 100 calculada pelo sistema (só apoio à triagem).
5. A ONG analisa no painel e define **Em análise**, **Aprovado** ou **Recusado**, com um retorno escrito ao adotante.
6. Ao **aprovar**, o animal passa a **Adotado** e os outros pedidos em aberto para ele são recusados automaticamente.

## Estrutura

```
src/main/java/sistema/adocao
├── config/       segurança, usuários, upload, dados iniciais
├── controller/   Home, Auth, Animal (público), Adocao (adotante), Admin
├── dto/          formulários e filtros
├── model/        Usuario, Animal, Solicitacao e enums
├── repository/   Spring Data JPA
└── service/      regras de negócio (busca, triagem, status, fotos)
src/main/resources
├── templates/    páginas Thymeleaf
└── static/css/   estilo
banco/schema.sql  modelo de dados MySQL
```

As fotos enviadas ficam na pasta `uploads/` (configurável em `app.upload-dir`).

## Testes

`./mvnw test` executa os testes da regra de triagem, que não precisam do MySQL.
