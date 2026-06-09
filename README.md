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

> Em breve.

---

## 3. Instruções para Devs

> Em breve.

---

## 4. Tecnologias

| Finalidade                  | Tecnologia              | Versão     |
| --------------------------- | ----------------------- | ---------- |
| Frontend                    | HTML5                   | 5          |
| Frontend                    | CSS3                    | 3          |
| Frontend                    | JavaScript              | ES6+       |
| Backend                     | Java                    | 21         |
| Framework Backend           | Spring Boot             | 3.x        |
| Persistência de Dados       | Spring Data JPA         | 3.x        |
| Banco de Dados              | MySQL                   | 8.0        |
| Gerenciador de Dependências | Maven                   | 3.x        |
| Versionamento               | Git                     | 2.x        |
| Testes de Unidade           | JUnit                   | 5          |
| Testes de Interface         | Selenium                | 4.x        |
| IDE                         | IntelliJ IDEA / VS Code | Atual      |

---

## 5. Organização do Projeto

```
Aura-Farm/
│
├── backend/                                # API Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   └── br/com/aurafarm/
│   │   │   │       ├── controller/         # Endpoints REST
│   │   │   │       ├── service/            # Regras de negócio
│   │   │   │       ├── repository/         # Persistência de dados
│   │   │   │       ├── model/              # Entidades JPA
│   │   │   │       ├── dto/                # Objetos de transferência
│   │   │   │       ├── config/             # Configurações do Spring
│   │   │   │       ├── exception/          # Tratamento de exceções
│   │   │   │       └── AuraFarmApplication.java
│   │   │   │
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── schema.sql             
│   │   │       ├── data.sql               
│   │   │       └── static/
│   │   │
│   │   └── test/
│   │       └── java/
│   │
│   ├── pom.xml
│   └── .gitignore
│
├── frontend/                               # HTML, CSS e JavaScript
│   ├── pages/
│   │   ├── index.html
│   │   ├── login.html
│   │   ├── dashboard.html
│   │   └── usuarios.html
│   │
│   ├── css/
│   │   ├── style.css
│   │   ├── login.css
│   │   └── dashboard.css
│   │
│   ├── js/
│   │   ├── api.js                         # Comunicação com o backend
│   │   ├── login.js
│   │   ├── dashboard.js
│   │   └── usuarios.js
│   │
│   ├── img/
│   │
│   └── .gitignore
│
├── docs/                                   # Documentação geral
│   ├── arquitetura.md
│   ├── api.md
│   └── diagramas/
│
├── requisitos/                             # Engenharia de requisitos
│   ├── requisitos-funcionais.md
│   ├── requisitos-nao-funcionais.md
│   ├── casos-de-uso.pdf
│   └── prototipos/
│
├── padroes-adotados/                     # Convenções do projeto
│   ├── Regras_de_Verificacao_e_Analise_de_Requisitos.pdf
│   ├── padrao-commits.md
│   ├── padrao-codigo.md
│   └── padrao-nomenclatura.md
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
