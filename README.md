#  Aura Farm — Loja de Roupas Online

> Projeto Final — GCC188 Engenharia de Software  
> Universidade Federal de Lavras (UFLA)

---

## 1. Contexto do Problema e Solução

### Problema

Pequenas lojas de roupas ainda dependem de processos manuais para gerenciar seus produtos, pedidos e clientes. Isso gera dificuldades como perda de controle do estoque, erros em pedidos e falta de organização nos registros de vendas.

### Solução

A **Aura Farm** é um sistema web que permite o gerenciamento completo de uma loja de roupas. O sistema oferece:

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

| Finalidade | Tecnologia | Versão |
|---|---|---|
| Frontend | HTML5 | 5 |
| Frontend | CSS3 | 3 |
| Frontend | JavaScript | ES6+ |
| Backend | PHP | 8.2 |
| Banco de Dados | MySQL | 8.0 |
| Servidor Local | XAMPP | 8.2 |
| Versionamento | Git | 2.x |
| Testes de Unidade | PHPUnit | 10.x |
| Testes de Interface | Selenium | 4.x |
| IDE | VS Code | 1.x |

---

## 5. Organização do Projeto

```
Aura-Farm/
├── config/              # Configurações do sistema (banco de dados, constantes)
├── database/            # Script SQL para criação e população do banco
├── docs/                # Documentação do projeto
├── Padrões Adotados/    # Regras e critérios de desenvolvimento
├── Requisitos/          # Documento de requisitos, casos de uso e protótipos
├── src/                 # Código-fonte principal
│   ├── controllers/     # Controladores (lógica de negócio)
│   ├── models/          # Modelos (comunicação com o banco de dados)
│   └── views/           # Telas e interfaces do sistema
├── public/              # Arquivos públicos (CSS, JS, imagens)
│   ├── css/
│   ├── js/
│   └── img/
├── tests/               # Testes unitários e de validação
│   ├── unit/            # Testes PHPUnit
│   └── selenium/        # Scripts Selenium
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
