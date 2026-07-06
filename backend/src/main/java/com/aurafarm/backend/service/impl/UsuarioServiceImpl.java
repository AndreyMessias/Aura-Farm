package com.aurafarm.backend.service.impl;

import com.aurafarm.backend.config.JwtTokenProvider;
import com.aurafarm.backend.dto.mapper.UsuarioMapper;
import com.aurafarm.backend.dto.request.RedefinirSenhaRequest;
import com.aurafarm.backend.dto.request.SolicitarRecuperacaoSenhaRequest;
import com.aurafarm.backend.dto.request.UsuarioProfileRequest;
import com.aurafarm.backend.dto.request.UsuarioRequest;
import com.aurafarm.backend.dto.request.VerificarCodigoRecuperacaoRequest;
import com.aurafarm.backend.dto.response.AuthResponse;
import com.aurafarm.backend.dto.response.LoginResponse;
import com.aurafarm.backend.dto.response.UsuarioListItemResponse;
import com.aurafarm.backend.dto.response.UsuarioResponse;
import com.aurafarm.backend.entity.Usuario;
import com.aurafarm.backend.exception.BusinessException;
import com.aurafarm.backend.exception.ResourceNotFoundException;
import com.aurafarm.backend.repository.UsuarioRepository;
import com.aurafarm.backend.service.EmailService;
import com.aurafarm.backend.service.UsuarioService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UsuarioServiceImpl implements UsuarioService {

    private static final int CODIGO_VALIDADE_MINUTOS = 5;
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailService emailService;

    @Override
    @Transactional
    public UsuarioResponse criar(UsuarioRequest request) {
        if (usuarioRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("CPF já cadastrado", "CPF_DUPLICADO");
        }
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já cadastrado", "EMAIL_DUPLICADO");
        }

        Usuario usuario = usuarioMapper.toEntity(request);
        usuario.setSenha(null);
        if (request.getPais() == null) {
            usuario.setPais("Brasil");
        }

        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    public UsuarioResponse buscarPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "id", id));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    public UsuarioResponse buscarPorCpf(String cpf) {
        String cleanCpf = cpf.replaceAll("[^0-9]", "");
        Usuario usuario = usuarioRepository.findByCpf(cleanCpf)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "cpf", cpf));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    public Page<UsuarioListItemResponse> listar(Pageable pageable, String nome) {
        Page<Usuario> page;
        if (nome != null && !nome.isBlank()) {
            page = usuarioRepository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            page = usuarioRepository.findAll(pageable);
        }
        return page.map(usuarioMapper::toListItem);
    }

    @Override
    @Transactional
    public UsuarioResponse atualizar(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "id", id));

        if (!usuario.getCpf().equals(request.getCpf()) && usuarioRepository.existsByCpf(request.getCpf())) {
            throw new BusinessException("CPF já cadastrado", "CPF_DUPLICADO");
        }
        if (!usuario.getEmail().equals(request.getEmail()) && usuarioRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email já cadastrado", "EMAIL_DUPLICADO");
        }

        usuarioMapper.updateEntity(request, usuario);

        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário", "id", id);
        }
        usuarioRepository.deleteById(id);
    }

    @Override
    public AuthResponse validarEmailParaPrimeiroAcesso(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "email", email));

        if (!usuario.isPrimeiroAcesso()) {
            throw new BusinessException("Senha já definida para este usuário", "SENHA_JA_DEFINIDA");
        }

        return AuthResponse.builder()
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .mensagem("Email válido. Defina sua senha.")
                .build();
    }

    @Override
    @Transactional
    public UsuarioResponse definirSenhaPrimeiroAcesso(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "email", email));

        if (!usuario.isPrimeiroAcesso()) {
            throw new BusinessException("Senha já definida para este usuário", "SENHA_JA_DEFINIDA");
        }

        usuario.setSenha(passwordEncoder.encode(senha));
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    public LoginResponse login(String email, String senha) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("E-mail ou senha inválido", "CREDENCIAIS_INVALIDAS"));

        if (usuario.isPrimeiroAcesso()) {
            throw new BusinessException("Primeiro acesso. Defina sua senha.", "PRIMEIRO_ACESSO");
        }

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new BusinessException("E-mail ou senha inválido", "CREDENCIAIS_INVALIDAS");
        }

        String token = jwtTokenProvider.generateToken(usuario.getEmail(), usuario.getCargo());
        return LoginResponse.builder()
                .token(token)
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .cargo(usuario.getCargo())
                .build();
    }

    @Override
    public UsuarioResponse me(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "email", email));
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional
    public UsuarioResponse atualizarMeuPerfil(String email, UsuarioProfileRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "email", email));

        // E-mail e CPF são bloqueados para edição pelo próprio usuário (RF012).
        usuario.setNome(request.getNome());
        usuario.setTelefone(request.getTelefone());
        usuario.setCidade(request.getCidade());
        usuario.setEstado(request.getEstado());
        usuario.setPais(request.getPais() != null ? request.getPais() : "Brasil");

        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponse(usuario);
    }

    @Override
    @Transactional
    public AuthResponse solicitarRecuperacaoSenha(SolicitarRecuperacaoSenhaRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException("E-mail inválido", "EMAIL_INVALIDO"));

        String codigo = gerarCodigo();
        usuario.setResetCodigo(passwordEncoder.encode(codigo));
        usuario.setResetCodigoExpiraEm(LocalDateTime.now().plusMinutes(CODIGO_VALIDADE_MINUTOS));
        usuarioRepository.save(usuario);

        emailService.enviarCodigoRecuperacaoSenha(usuario.getEmail(), usuario.getNome(), codigo);

        return AuthResponse.builder()
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .mensagem("Código de verificação enviado para o e-mail cadastrado.")
                .build();
    }

    @Override
    public AuthResponse verificarCodigoRecuperacao(VerificarCodigoRecuperacaoRequest request) {
        Usuario usuario = buscarUsuarioComCodigoValido(request.getEmail(), request.getCodigo());

        return AuthResponse.builder()
                .email(usuario.getEmail())
                .nome(usuario.getNome())
                .mensagem("Código válido. Defina a nova senha.")
                .build();
    }

    @Override
    @Transactional
    public UsuarioResponse redefinirSenha(RedefinirSenhaRequest request) {
        if (!request.getNovaSenha().equals(request.getConfirmacaoNovaSenha())) {
            throw new BusinessException("As senhas não coincidem", "SENHAS_NAO_COINCIDEM");
        }

        Usuario usuario = buscarUsuarioComCodigoValido(request.getEmail(), request.getCodigo());

        usuario.setSenha(passwordEncoder.encode(request.getNovaSenha()));
        usuario.setResetCodigo(null);
        usuario.setResetCodigoExpiraEm(null);
        usuario = usuarioRepository.save(usuario);

        return usuarioMapper.toResponse(usuario);
    }

    private Usuario buscarUsuarioComCodigoValido(String email, String codigo) {
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException("E-mail inválido", "EMAIL_INVALIDO"));

        if (usuario.getResetCodigo() == null || usuario.getResetCodigoExpiraEm() == null
                || usuario.getResetCodigoExpiraEm().isBefore(LocalDateTime.now())) {
            throw new BusinessException("Código expirado. Solicite um novo código.", "CODIGO_EXPIRADO");
        }

        if (!passwordEncoder.matches(codigo, usuario.getResetCodigo())) {
            throw new BusinessException("Código inválido", "CODIGO_INVALIDO");
        }

        return usuario;
    }

    private String gerarCodigo() {
        return String.format("%06d", RANDOM.nextInt(1_000_000));
    }
}
