-- Seed do usuário administrador inicial (GERENTE)
-- Senha padrão: admin123 (BCrypt hashed)
INSERT INTO usuario (nome, cpf, email, senha, cargo, pais, created_at, updated_at)
VALUES ('Administrador', '00000000000', 'admin@aurafarm.com',
        '$2a$10$c33azuEWV9m.LJcDv3qKKe70Vo9shbVLDXMHNzRlHvyvXOOF8QGlq',
        'GERENTE', 'Brasil', NOW(), NOW())
ON CONFLICT (email) DO NOTHING;
