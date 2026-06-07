#  Aura Farm — Loja de Roupas Online

> Projeto Final — GCC188 Engenharia de Software  
> Universidade Federal de Lavras (UFLA)
---
## 1. Contexto do Problema e Solução

### Problema

Uma pequena loja de roupas chamada Aura Farm ainda depende de processos manuais para gerenciar seus produtos, pedidos e clientes. Isso gera dificuldades como perda de controle do estoque, erros em pedidos e falta de organização nos registros de vendas.

### Solução

A **Aura Farm** é um sistema web que permite o gerenciamento completo da loja de roupas. O sistema oferece:

- Cadastro, consulta, edição e exclusão de **produtos** (nome, descrição, tamanho, cor, preço e estoque)
- Registro e gerenciamento de **pedidos**, integrando produtos e endereços de entrega
- Controle de **endereços de entrega** por usuário
- **Login e autenticação** para acesso seguro ao sistema

---

## 2. Instruções para Uso



---

## 3. Instruções para Devs

Siga as instruções abaixo para configurar seu ambiente de desenvolvimento:

### 3.1 Clonar o projeto

```bash
git clone https://github.com/AndreyMessias/Aura-Farm.git
```

Ou baixe o ZIP e extraia na pasta desejada.

### 3.2 Configurar o banco de dados

1. Crie o banco `aura_farm` no MySQL
2. Importe o arquivo `database/aura_farm.sql`:

```bash
mysql -u root -p aura_farm < database/aura_farm.sql
```

3. Configure as credenciais no arquivo `config/database.php`:

```php
define('DB_HOST', 'localhost');
define('DB_USER', 'root');
define('DB_PASS', '');
define('DB_NAME', 'aura_farm');
```

### 3.3 Executar o projeto

1. Coloque o projeto na pasta `htdocs` do XAMPP
2. Inicie o Apache e o MySQL pelo painel do XAMPP
3. Acesse: `http://localhost/Aura-Farm`

---

## 4. Tecnologias

| Finalidade                  | Tecnologia              | Versão     |
| --------------------------- | ----------------------- | ---------- |
| Frontend                    | HTML5                   | 5          |
| Frontend                    | CSS3                    | 3          |
| Frontend                    | JavaScript              | ES6+       |
| Backend                     | Java                    | 21 |
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
├── padroes-adotados/                       # Convenções do projeto
│   ├── padrao-commits.md
│   ├── padrao-codigo.md
│   └── padrao-nomenclatura.md
│
├── .gitignore
│
└── README.md                               
```

---

## 6. Principais Funcionalidades

- RF01 — Cadastrar usuário
- RF02 — Realizar login
- RF03 — Cadastrar produto
- RF04 — Consultar produto
- RF05 — Editar produto
- RF06 — Excluir produto
- RF07 — Cadastrar endereço de entrega
- RF08 — Consultar endereço de entrega
- RF09 — Editar endereço de entrega
- RF10 — Excluir endereço de entrega
- RF11 — Realizar pedido
- RF12 — Consultar pedidos
- RF13 — Alterar status do pedido
- RF14 — Cancelar pedido

---

## 7. Equipe

| Nome | GitHub |
|---|---|
| Andrey Messias | [@AndreyMessias](https://github.com/AndreyMessias) |
| Luiz | [@luizzfellip](https://github.com/luizzfellip) |

---

## 8. Licença

Este projeto foi desenvolvido para fins acadêmicos na disciplina GCC188 — Engenharia de Software da UFLA.
