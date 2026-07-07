#  Aura Farm — Sistema de Gestão de Loja

> Projeto Final — GCC188 Engenharia de Software  
> Universidade Federal de Lavras (UFLA)

---

## 1. Contexto do Problema e Solução

### Problema

Uma pequena loja de roupas chamada Aura Farm ainda depende de processos manuais para gerenciar seus produtos, fornecedores, funcionários e vendas. Isso gera dificuldades como perda de controle do estoque, erros no registro de vendas, dificuldade na gestão de fornecedores e falta de organização dos dados da loja.

### Solução

A **Aura Farm** é um sistema web de gestão interna para loja de roupas, permitindo o controle completo das operações. O sistema oferece:

- **Autenticação** – Login seguro e recuperação de senha
- **Dashboard** – Visão geral com indicadores de vendas e últimas vendas
- **Gerenciamento de Produtos** – Cadastro, consulta, edição e exclusão de produtos (nome, descrição, tamanho, cor, preço e estoque)
- **Gerenciamento de Fornecedores** – Cadastro, consulta, edição e exclusão de fornecedores
- **Gerenciamento de Funcionários** – Cadastro, consulta, edição e demissão de funcionários (apenas gerente)
- **Registro de Vendas** – Cadastro, consulta, alteração de status e cancelamento de vendas
- **Perfil de Usuário** – Visualização e edição do próprio perfil
- **Controle de permissões** – Gerente tem acesso total; Funcionário tem acesso restrito a vendas e consulta de produtos

---

## 2. Instruções para Uso

### Pré-requisitos

- **PostgreSQL 16+** rodando localmente
- **JDK 17**
- **Python 3** (ou qualquer servidor HTTP estático) para servir o frontend

### Passo a passo

**1. Crie o banco de dados** (se ainda não existir):

```bash
psql -U postgres -c "CREATE DATABASE aurafarm;"
```

As tabelas são criadas automaticamente na primeira subida do backend (migrations Flyway).

**2. Configure as variáveis de ambiente do backend**, copiando o arquivo de exemplo:

```bash
cp backend/.env.example backend/.env
```

Edite `backend/.env` com seus dados:

```
DB_URL=jdbc:postgresql://localhost:5432/aurafarm
DB_USERNAME=postgres
DB_PASSWORD=sua_senha_do_postgres

JWT_SECRET=uma_string_aleatoria_com_pelo_menos_32_caracteres
JWT_EXPIRATION=86400000

MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=seu_email@gmail.com
MAIL_PASSWORD=sua_senha_de_app_do_gmail
MAIL_FROM=seu_email@gmail.com
```

> As variáveis de e-mail (`MAIL_*`) só são necessárias para o fluxo de recuperação de senha (RF10) funcionar de ponta a ponta — o resto do sistema funciona normalmente sem elas. Se usar Gmail, é preciso gerar uma **senha de app** (não a senha normal da conta) em `myaccount.google.com/apppasswords`, com verificação em duas etapas ativada.

**3. Suba o backend:**

```bash
cd backend
set -a && source .env && set +a
./mvnw spring-boot:run
```

O backend sobe em `http://localhost:8080`.

> Se o build falhar com `release version 17 not supported`, sua máquina tem mais de uma JDK instalada e o Maven pegou a errada — force a JDK 17: `JAVA_HOME=/caminho/para/jdk-17 ./mvnw spring-boot:run`.

**4. Suba o frontend** (em outro terminal):

```bash
cd frontend
python3 -m http.server 5500
```

⚠️ Não abra os arquivos `.html` direto no navegador (duplo clique) — o navegador bloqueia as chamadas à API por CORS quando a página é aberta como `file://`. Precisa ser servida por um servidor HTTP, mesmo que local.

Acesse **http://localhost:5500/pages/login.html**

### Primeiro login

Existe um usuário Gerente pré-cadastrado (seed) para o primeiro acesso ao sistema:

| Campo | Valor |
|---|---|
| E-mail | `admin@aurafarm.com` |
| Senha | `admin123` |

### Fluxo básico de uso

1. **Login** com o usuário admin acima.
2. **Fornecedores** — cadastre ao menos um fornecedor (é pré-requisito para cadastrar produtos).
3. **Produtos** — cadastre produtos vinculados ao fornecedor criado.
4. **Funcionários** — cadastre novos funcionários/gerentes. O cadastro é feito sem senha; o próprio funcionário define a senha na tela **Primeiro Acesso** (link na tela de login), informando o e-mail que o gerente cadastrou.
5. **Vendas** — registre vendas selecionando produtos do catálogo e preenchendo o endereço de entrega completo.
6. **Dashboard** — acompanhe total de produtos, vendas por status e as últimas vendas registradas.
7. **Meu Perfil** — visualize e edite seus próprios dados (e-mail e CPF não podem ser alterados pelo próprio usuário).
8. **Esqueci minha senha** (na tela de login) — fluxo de recuperação por código de verificação enviado por e-mail, com validade de 5 minutos.

Funcionários (cargo não-Gerente) não têm acesso às telas de Fornecedores e Funcionários, e não podem cadastrar/editar/excluir Produtos nem excluir Vendas — essas ações ficam ocultas na interface e são bloqueadas também no backend.

---

## 3. Instruções para Devs

### Ferramentas necessárias

- JDK 17
- Maven — não precisa instalar, o projeto já traz o wrapper (`./mvnw`)
- PostgreSQL 16+
- Um servidor HTTP estático para o frontend em desenvolvimento (`python3 -m http.server`, extensão Live Server do VS Code, etc.)
- [Insomnia](https://insomnia.rest/) (opcional, mas recomendado) — para testar a API isoladamente

### Testando a API sem o frontend

Há uma collection pronta em `insomnia/Aura-Farm.json`, organizada em pastas por módulo (Autenticação, Usuários, Fornecedores, Produtos, Vendas, Dashboard), já com exemplos de corpo de requisição preenchidos. Basta importar no Insomnia, fazer login pelo endpoint de auth e colar o token retornado na variável de ambiente `token` da collection.

### Banco de dados e migrations

O schema não é criado por `ddl-auto` do Hibernate (que está em modo `validate`) — todas as alterações de schema vivem em `backend/src/main/resources/db/migration/`, como migrations do **Flyway**, aplicadas automaticamente toda vez que o backend sobe. Para alterar o schema, adicione um novo arquivo `V{numero}__descricao.sql` seguindo a numeração sequencial já existente (atualmente até `V6`) — nunca edite uma migration que já foi aplicada.

### Variáveis de ambiente

Só `DB_PASSWORD` e `JWT_SECRET` são obrigatórias (a aplicação não sobe sem elas). As demais (`DB_URL`, `DB_USERNAME`, `MAIL_*`, `JWT_EXPIRATION`) têm valores padrão em `application.yml`. Dica pra gerar um `JWT_SECRET` rapidamente: `openssl rand -hex 32`.

### CORS

O backend libera CORS para `http://localhost:*` e `http://127.0.0.1:*` em qualquer porta (`SecurityConfig.corsConfigurationSource()`), pensado pra desenvolvimento local. Se for hospedar o frontend em outro domínio, ajuste essa configuração antes de ir pra produção.

### Rodando os testes

```bash
cd backend
./mvnw test
```

---

## 4. Tecnologias

| Finalidade                   | Tecnologia                   | Versão                            |
| ---------------------------- | ---------------------------- | --------------------------------- |
| Frontend                     | HTML5                        | 5                                  |
| Frontend                     | CSS3                         | 3                                  |
| Frontend                     | JavaScript                   | ES6+ (Fetch API, sem frameworks)  |
| Backend                      | Java                         | 17                                 |
| Framework Backend            | Spring Boot                  | 4.1.0                              |
| Persistência de Dados        | Spring Data JPA / Hibernate  | 7.x                                |
| Banco de Dados               | PostgreSQL                   | 16                                 |
| Migrations de Banco          | Flyway                       | 12.x                               |
| Segurança                    | Spring Security + JWT (jjwt) | 0.12.x                             |
| Mapeamento DTO ↔ Entidade     | MapStruct                    | 1.5.5                              |
| Envio de E-mail              | Spring Mail (SMTP)           | —                                  |
| Gerenciador de Dependências  | Maven (via wrapper `./mvnw`) | 3.9.x                               |
| Versionamento                | Git                          | 2.x                                 |
| Testes de Unidade            | JUnit                        | 5                                   |
| Cliente de API para testes   | Insomnia                     | —                                  |
| IDE                          | IntelliJ IDEA / VS Code      | Atual                              |

---

## 5. Organização do Projeto

```
Aura-Farm/
│
├── backend/                                       # API Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/aurafarm/backend/
│   │   │   │   ├── config/                        # Security, JWT, CORS
│   │   │   │   ├── controller/                     # Endpoints REST
│   │   │   │   ├── dto/
│   │   │   │   │   ├── mapper/                     # MapStruct (entidade ↔ DTO)
│   │   │   │   │   ├── request/
│   │   │   │   │   └── response/
│   │   │   │   ├── entity/                         # Entidades JPA
│   │   │   │   ├── enums/                          # Cargo, StatusPedido, StatusProduto, Tamanho
│   │   │   │   ├── exception/                      # Exceções de negócio + handler global
│   │   │   │   ├── repository/                     # Spring Data JPA
│   │   │   │   ├── service/
│   │   │   │   │   └── impl/                       # Regras de negócio
│   │   │   │   ├── validation/                     # Validador customizado de CPF
│   │   │   │   └── BackendApplication.java
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── application.yml
│   │   │       └── db/migration/                   # Scripts Flyway (V1 a V6)
│   │   │
│   │   └── test/
│   │
│   ├── pom.xml
│   ├── .env.example                                # Modelo de variáveis de ambiente
│   └── .gitignore
│
├── frontend/                                       # HTML, CSS e JavaScript puro
│   ├── pages/                                       # 18 páginas (auth, dashboard, CRUDs)
│   │   ├── login.html / recuperar-senha.html / primeiro-acesso.html
│   │   ├── dashboard.html
│   │   ├── perfil.html / editar-perfil.html
│   │   ├── produtos.html / novo-produto.html / editar-produto.html
│   │   ├── fornecedores.html / novo-fornecedor.html / editar-fornecedor.html
│   │   ├── funcionarios.html / novo-funcionario.html / editar-funcionario.html
│   │   └── vendas.html / nova-venda.html / editar-venda.html
│   │
│   ├── css/
│   │   └── style.css
│   │
│   ├── js/
│   │   ├── api.js                                  # Cliente HTTP + sessão (token, helpers)
│   │   ├── layout.js                                # Guarda de rota, sidebar, logout
│   │   ├── auth.js                                  # Login, recuperar senha, primeiro acesso
│   │   ├── dashboard.js
│   │   ├── perfil.js
│   │   ├── produtos.js
│   │   ├── fornecedores.js
│   │   ├── funcionarios.js
│   │   └── vendas.js
│   │
│   └── img/
│
├── insomnia/                                       # Collection pronta pra testar a API
│   └── Aura-Farm.json
│
├── docs/                                           # Documentação geral e diagramas
│   └── diagramas/
│
├── requisitos/                                     # Engenharia de requisitos
│   ├── Documento de Requisitos.pdf
│   └── prototipos/
│
├── padroes-adotados/                               # Convenções do projeto
│   ├── Regras de Boas Praticas de Codificacao.md
│   ├── Regras de Uso Git.md
│   └── Regras de Verificação e Analise de Requisitos.pdf
│
├── .gitignore
│
└── README.md
```


## 6. Funcionalidades

### Fornecedor (Gerente)
- RF01 — Cadastrar Fornecedor
- RF02 — Consultar Fornecedor
- RF03 — Alterar Fornecedor
- RF04 — Excluir Fornecedor

### Funcionário (Gerente)
- RF05 — Cadastrar Funcionário
- RF06 — Consultar Funcionário
- RF07 — Editar Funcionário
- RF08 — Demitir/Remover Funcionário

### Autenticação e Perfil (Todos)
- RF09 — Realizar login
- RF10 — Recuperar senha
- RF11 — Visualizar perfil
- RF12 — Usuário alterar o próprio perfil

### Venda (Gerente e Funcionário)
- RF13 — Cadastrar Venda
- RF14 — Consultar Venda
- RF15 — Alterar Status da Venda
- RF16 — Alterar toda a Venda
- RF17 — Excluir Venda (apenas Gerente)

### Dashboard (Gerente e Funcionário)
- RF18 — Visualizar Dashboard

### Produto
- RF19 — Cadastrar Produto (Gerente)
- RF20 — Consultar Produto (Todos)
- RF21 — Alterar Produto (Gerente)
- RF22 — Excluir Produto (Gerente)

---

## 7. Equipe

| Nome | GitHub |
|---|---|
| Andrey Messias | [@AndreyMessias](https://github.com/AndreyMessias) |
| Luiz | [@luizzfellip](https://github.com/luizzfellip) |

---

## 8. Licença

Este projeto foi desenvolvido para fins acadêmicos na disciplina GCC188 — Engenharia de Software da UFLA.
