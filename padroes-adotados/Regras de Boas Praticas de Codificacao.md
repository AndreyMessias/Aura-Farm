# Boas Práticas de Codificação

Para garantir a qualidade, manutenção e evolução do sistema Aura Farm, a equipe adotará as seguintes práticas de desenvolvimento durante todo o ciclo de implementação.

## 1. Padronização de Nomenclatura

Todos os identificadores deverão possuir nomes claros e significativos, permitindo que qualquer membro da equipe compreenda facilmente sua finalidade.

### Regras

* Classes: PascalCase (`ProdutoService`, `FornecedorController`);
* Métodos e variáveis: camelCase (`buscarProduto()`, `quantidadeEstoque`);
* Constantes: UPPER_SNAKE_CASE (`MAX_TENTATIVAS_LOGIN`);
* Evitar abreviações sem significado claro.

## 2. Documentação e Comentários

Comentários serão utilizados apenas quando necessários para explicar regras de negócio complexas ou decisões técnicas importantes.

### Regras

* Evitar comentários redundantes;
* Priorizar código autoexplicativo;
* Documentar APIs, funções públicas e trechos críticos;
* Manter comentários sempre atualizados.

## 3. Aplicação dos Princípios SOLID

A arquitetura do sistema seguirá os princípios SOLID para aumentar a modularidade e facilitar a manutenção.

### Regras

* Cada classe deve possuir apenas uma responsabilidade;
* Componentes devem ser extensíveis sem necessidade de modificar código existente;
* Dependências devem ocorrer por abstrações sempre que possível;
* Evitar acoplamento excessivo entre módulos.

## 4. Métodos Pequenos e Coesos

Cada método deve executar apenas uma responsabilidade específica.

### Regras

* Evitar métodos extensos;
* Dividir lógicas complexas em funções menores;
* Utilizar nomes que descrevam claramente a ação realizada;
* Reduzir a quantidade de parâmetros sempre que possível.

## 5. Tratamento Adequado de Erros

Todas as operações críticas deverão possuir tratamento adequado de exceções e validações.

### Regras

* Nunca ignorar erros silenciosamente;
* Exibir mensagens compreensíveis ao usuário;
* Registrar erros relevantes para fins de manutenção;
* Validar entradas antes de processá-las.

## 6. Testes e Validação de Funcionalidades

As funcionalidades implementadas deverão ser testadas antes de serem integradas ao sistema principal.

### Regras

* Realizar testes unitários sempre que possível;
* Validar fluxos principais do sistema;
* Corrigir falhas identificadas antes da integração;
* Garantir que alterações não afetem funcionalidades já existentes.

## 7. Padronização de Formatação

Todo o código deverá seguir um padrão único de formatação para melhorar a legibilidade.

### Regras

* Utilizar identação de 4 espaços;
* Manter espaçamento consistente;
* Utilizar chaves em estruturas condicionais e de repetição;
* Limitar blocos excessivamente aninhados.

## 8. Evitar Duplicação de Código (Princípio DRY)

Trechos de código reutilizáveis deverão ser extraídos para funções, componentes ou serviços específicos.

### Regras

* Não repetir regras de negócio;
* Reutilizar componentes do frontend;
* Centralizar chamadas de API em serviços;
* Refatorar códigos repetidos sempre que identificados.
