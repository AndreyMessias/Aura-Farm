-- V2__tables.sql - Todas as tabelas (ordem respeitando FKs)

-- 1. USUARIO (sem FK de entrada)
CREATE TABLE usuario (
    id_usuario BIGSERIAL PRIMARY KEY,
    cpf VARCHAR(11) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    senha VARCHAR(255) NOT NULL,
    cargo tipo_usuario NOT NULL DEFAULT 'FUNCIONARIO',
    telefone VARCHAR(20),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    pais VARCHAR(100) DEFAULT 'Brasil',
    usuario_cadastrou_id BIGINT REFERENCES usuario(id_usuario),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. FORNECEDOR (FK -> usuario)
CREATE TABLE fornecedor (
    id_fornecedor BIGSERIAL PRIMARY KEY,
    cnpj VARCHAR(14) NOT NULL UNIQUE,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    telefone VARCHAR(20),
    cidade VARCHAR(100),
    estado VARCHAR(2),
    pais VARCHAR(100) DEFAULT 'Brasil',
    usuario_id BIGINT NOT NULL REFERENCES usuario(id_usuario),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 3. PRODUTO (FK -> fornecedor, usuario)
CREATE TABLE produto (
    id_produto BIGSERIAL PRIMARY KEY,
    codigo VARCHAR(100) NOT NULL UNIQUE,
    descricao VARCHAR(100) NOT NULL,
    cor VARCHAR(100),
    tamanho tamanho_produto NOT NULL,
    preco NUMERIC(12,2) NOT NULL CHECK (preco >= 0),
    quantidade_estoque INTEGER NOT NULL DEFAULT 0 CHECK (quantidade_estoque >= 0),
    status status_produto NOT NULL DEFAULT 'ATIVO',
    fornecedor_id BIGINT NOT NULL REFERENCES fornecedor(id_fornecedor),
    usuario_id BIGINT NOT NULL REFERENCES usuario(id_usuario),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. ENDERECO (sem FK de entrada)
CREATE TABLE endereco (
    id_endereco BIGSERIAL PRIMARY KEY,
    rua VARCHAR(100) NOT NULL,
    numero VARCHAR(20) NOT NULL,
    bairro VARCHAR(100) NOT NULL,
    cidade VARCHAR(100) NOT NULL,
    estado VARCHAR(2) NOT NULL,
    cep VARCHAR(8) NOT NULL,
    complemento VARCHAR(100),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. VENDA (FK -> usuario, endereco)
CREATE TABLE venda (
    id_venda BIGSERIAL PRIMARY KEY,
    data_pedido TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    status status_pedido NOT NULL DEFAULT 'PENDENTE',
    valor_total NUMERIC(12,2) NOT NULL DEFAULT 0 CHECK (valor_total >= 0),
    quantidade_total INTEGER NOT NULL DEFAULT 0 CHECK (quantidade_total >= 0),
    usuario_id BIGINT NOT NULL REFERENCES usuario(id_usuario),
    endereco_id BIGINT NOT NULL UNIQUE REFERENCES endereco(id_endereco),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 6. ITEM_VENDA (FK -> venda, produto)
CREATE TABLE item_venda (
    id_itemvenda BIGSERIAL PRIMARY KEY,
    quantidade INTEGER NOT NULL CHECK (quantidade > 0),
    preco_unitario NUMERIC(12,2) NOT NULL CHECK (preco_unitario >= 0),
    venda_id BIGINT NOT NULL REFERENCES venda(id_venda) ON DELETE CASCADE,
    produto_id BIGINT NOT NULL REFERENCES produto(id_produto),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Índices
CREATE INDEX idx_fornecedor_usuario ON fornecedor(usuario_id);
CREATE INDEX idx_produto_fornecedor ON produto(fornecedor_id);
CREATE INDEX idx_produto_usuario ON produto(usuario_id);
CREATE INDEX idx_produto_codigo ON produto(codigo);
CREATE INDEX idx_venda_usuario ON venda(usuario_id);
CREATE INDEX idx_venda_data_pedido ON venda(data_pedido);
CREATE INDEX idx_venda_status ON venda(status);
CREATE INDEX idx_item_venda_venda ON item_venda(venda_id);
CREATE INDEX idx_item_venda_produto ON item_venda(produto_id);
CREATE INDEX idx_usuario_cadastrou ON usuario(usuario_cadastrou_id);