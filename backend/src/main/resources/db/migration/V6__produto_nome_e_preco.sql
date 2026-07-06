-- V6__produto_nome_e_preco.sql
-- RF019 lista "nome" e "descricao" como campos distintos do produto, mas a
-- tabela so tinha "descricao". Tambem corrige a regra de preco > 0 (RF019
-- regra 4), que a constraint original permitia ser igual a zero.

ALTER TABLE produto ADD COLUMN nome VARCHAR(100) NOT NULL DEFAULT '';
ALTER TABLE produto ADD CONSTRAINT produto_preco_positivo_check CHECK (preco > 0);
