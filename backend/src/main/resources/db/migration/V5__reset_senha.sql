-- V5__reset_senha.sql - Suporte a recuperacao de senha (RF010)

ALTER TABLE usuario ADD COLUMN reset_codigo VARCHAR(255);
ALTER TABLE usuario ADD COLUMN reset_codigo_expira_em TIMESTAMP;
