package com.aurafarm.backend.service.impl;

import com.aurafarm.backend.config.JwtTokenProvider;
import com.aurafarm.backend.dto.mapper.UsuarioMapper;
import com.aurafarm.backend.dto.request.UsuarioRequest;
import com.aurafarm.backend.dto.response.LoginResponse;
import com.aurafarm.backend.dto.response.UsuarioResponse;
import com.aurafarm.backend.entity.Usuario;
import com.aurafarm.backend.enums.Cargo;
import com.aurafarm.backend.exception.BusinessException;
import com.aurafarm.backend.repository.UsuarioRepository;
import com.aurafarm.backend.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private UsuarioMapper usuarioMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private EmailService emailService;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioRequest request;

    @BeforeEach
    void setUp() {
        request = UsuarioRequest.builder()
                .nome("João Silva")
                .cpf("39711291452")
                .email("joao@aura.com")
                .cargo(Cargo.FUNCIONARIO)
                .build();
    }

    @Test
    @DisplayName("Deve criar usuário com sucesso quando CPF e e-mail ainda não estão cadastrados")
    void deveCriarUsuarioComSucesso() {
        when(usuarioRepository.existsByCpf(request.getCpf())).thenReturn(false);
        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(false);

        Usuario usuarioMapeado = Usuario.builder().nome(request.getNome()).build();
        when(usuarioMapper.toEntity(request)).thenReturn(usuarioMapeado);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMapeado);
        when(usuarioMapper.toResponse(usuarioMapeado))
                .thenReturn(UsuarioResponse.builder().nome("João Silva").build());

        UsuarioResponse response = usuarioService.criar(request);

        assertThat(response.getNome()).isEqualTo("João Silva");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Deve rejeitar cadastro de usuário com CPF já cadastrado (RF005)")
    void deveRejeitarCadastroComCpfDuplicado() {
        when(usuarioRepository.existsByCpf(request.getCpf())).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.criar(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("CPF já cadastrado");

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve rejeitar cadastro de usuário com e-mail já cadastrado (RF005)")
    void deveRejeitarCadastroComEmailDuplicado() {
        when(usuarioRepository.existsByCpf(request.getCpf())).thenReturn(false);
        when(usuarioRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThatThrownBy(() -> usuarioService.criar(request))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Email já cadastrado");

        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve autenticar com sucesso quando e-mail e senha estão corretos (RF009)")
    void deveAutenticarComSucesso() {
        Usuario usuario = Usuario.builder()
                .email("joao@aura.com")
                .senha("hash-da-senha")
                .cargo(Cargo.FUNCIONARIO)
                .nome("João Silva")
                .build();

        when(usuarioRepository.findByEmail("joao@aura.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("minhasenha123", "hash-da-senha")).thenReturn(true);
        when(jwtTokenProvider.generateToken("joao@aura.com", Cargo.FUNCIONARIO)).thenReturn("token-fake");

        LoginResponse response = usuarioService.login("joao@aura.com", "minhasenha123");

        assertThat(response.getToken()).isEqualTo("token-fake");
        assertThat(response.getCargo()).isEqualTo(Cargo.FUNCIONARIO);
    }

    @Test
    @DisplayName("Deve rejeitar login com senha incorreta")
    void deveRejeitarLoginComSenhaIncorreta() {
        Usuario usuario = Usuario.builder()
                .email("joao@aura.com")
                .senha("hash-da-senha")
                .build();

        when(usuarioRepository.findByEmail("joao@aura.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaerrada", "hash-da-senha")).thenReturn(false);

        assertThatThrownBy(() -> usuarioService.login("joao@aura.com", "senhaerrada"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("E-mail ou senha inválido");
    }

    @Test
    @DisplayName("Deve rejeitar login de usuário em primeiro acesso (sem senha definida ainda)")
    void deveRejeitarLoginEmPrimeiroAcesso() {
        Usuario usuario = Usuario.builder()
                .email("novo@aura.com")
                .senha(null)
                .build();

        when(usuarioRepository.findByEmail("novo@aura.com")).thenReturn(Optional.of(usuario));

        assertThatThrownBy(() -> usuarioService.login("novo@aura.com", "qualquersenha"))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Primeiro acesso. Defina sua senha.");
    }
}
