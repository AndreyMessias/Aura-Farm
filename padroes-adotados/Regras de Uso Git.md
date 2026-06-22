# Padrões de Uso do Git

## Objetivo

Este documento define os padrões de utilização do Git adotados no projeto, com o objetivo de facilitar o trabalho colaborativo, manter um histórico organizado de alterações e melhorar a documentação do desenvolvimento.

---

# Estrutura do Repositório

A organização do repositório segue a seguinte estrutura:

```text
/
├── backend/
│   └── src/
│       ├── main/          # Código-fonte principal do backend
│       └── test/          # Testes do backend
│
├── frontend/             # Código-fonte da interface do sistema
│
├── docs/
│   └── diagramas/        # Diagramas e artefatos visuais do projeto
│
├── requisitos/
│   ├── prototipos/       # Protótipos e modelos de interface
│   └── Documento de Requisitos.pdf
│
├── padroes-adotados/     # Documentos contendo padrões, normas e processos adotados
│
└── README.md
```

## Diretórios

### backend/

Contém todo o código relacionado ao backend da aplicação.

#### backend/src/main/

Armazena o código-fonte principal do sistema.

#### backend/src/test/

Contém os testes relacionados ao backend.

### frontend/

Contém o código da interface do usuário e demais componentes do frontend.

### docs/

Armazena a documentação complementar do projeto.

#### docs/diagramas/

Contém diagramas de arquitetura, casos de uso, classes, sequência e demais artefatos visuais.

### requisitos/

Armazena toda a documentação relacionada à engenharia de requisitos do projeto.

#### requisitos/prototipos/

Contém protótipos de telas, fluxos e modelos de interface.

### padroes-adotados/

Contém documentos que definem padrões, processos, convenções e regras utilizadas pela equipe durante o desenvolvimento.

### README.md

Documento principal do repositório contendo descrição do projeto, instruções de instalação e informações gerais.

## Padrão de nomenclatura

### Novas funcionalidades

```text
feature/nome-da-funcionalidade
```

Exemplo:

```text
feature/login-usuario
feature/cadastro-cliente
```

### Correções de bugs

```text
fix/nome-do-bug
```

Exemplo:

```text
fix/erro-validacao-email
fix/correcao-calculo-total
```

### Atualização de documentação

```text
docs/nome-da-documentacao
```

Exemplo:

```text
docs/manual-instalacao
docs/atualizacao-readme
```

---

# Regras de Commit

As mensagens de commit devem ser curtas, objetivas e seguir um padrão consistente.

## Estrutura

```text
tipo: descrição
```

Exemplo:

```text
feat: adiciona tela de login
fix: corrige erro de autenticação
docs: atualiza documentação da API
```

## Tipos de Commit

### feat

Utilizado para novas funcionalidades.

```text
feat: implementa cadastro de usuários
```

### fix

Utilizado para correções de erros.

```text
fix: corrige validação de senha
```

### docs

Alterações em documentação.

```text
docs: adiciona guia de instalação
```

### style

Alterações de formatação sem impacto funcional.

```text
style: ajusta indentação do código
```

### refactor

Refatoração sem alterar comportamento.

```text
refactor: reorganiza estrutura da classe usuário
```

### test

Adição ou modificação de testes.

```text
test: adiciona testes para autenticação
```

### chore

Tarefas de manutenção.

```text
chore: atualiza dependências do projeto
```

## Boas Práticas

* Utilizar verbo no presente.
* Escrever mensagens objetivas.
* Não utilizar mensagens genéricas como:

```text
update
alterações
mudanças
commit
```

* Cada commit deve representar uma alteração lógica e específica.

---

# Processo de Desenvolvimento

1. Atualizar a branch principal.

```bash
git checkout main
git pull origin main
```

2. Criar uma nova branch.

```bash
git checkout -b feature/nova-funcionalidade
```

3. Realizar as alterações necessárias.

4. Registrar as alterações com commits descritivos.

```bash
git commit -m "feat: adiciona autenticação de usuários"
```

5. Enviar a branch para o repositório remoto.

```bash
git push origin feature/nova-funcionalidade
```

6. Abrir um Pull Request para integração na branch principal.

---

# Pull Requests

Antes de abrir um Pull Request, verificar:

* Código compilando corretamente.
* Testes executados com sucesso.
* Documentação atualizada quando necessário.
* Ausência de arquivos temporários ou desnecessários.

O Pull Request deve conter:

* Descrição da alteração realizada.
* Motivação da alteração.
* Possíveis impactos no sistema.

---

# Arquivo .gitignore

O arquivo `.gitignore` deve ser utilizado para impedir que arquivos temporários, compilados ou específicos do ambiente sejam enviados ao repositório.

Exemplo:

```gitignore
# Arquivos compilados
*.o
*.obj
*.exe
*.class

# Arquivos temporários
*.tmp
*.log

# Python
__pycache__/
*.pyc

# VS Code
.vscode/

# IntelliJ
.idea/

# Sistema Operacional
.DS_Store
Thumbs.db
```

---

