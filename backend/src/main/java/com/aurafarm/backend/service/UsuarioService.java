package com.aurafarm.backend.service;

import com.aurafarm.backend.dto.request.UsuarioProfileRequest;
import com.aurafarm.backend.dto.request.UsuarioRequest;
import com.aurafarm.backend.dto.response.AuthResponse;
import com.aurafarm.backend.dto.response.LoginResponse;
import com.aurafarm.backend.dto.response.UsuarioListItemResponse;
import com.aurafarm.backend.dto.response.UsuarioResponse;
import com.aurafarm.backend.dto.request.RedefinirSenhaRequest;
import com.aurafarm.backend.dto.request.SolicitarRecuperacaoSenhaRequest;
import com.aurafarm.backend.dto.request.VerificarCodigoRecuperacaoRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UsuarioService {

    UsuarioResponse criar(UsuarioRequest request);

    UsuarioResponse buscarPorId(Long id);

    UsuarioResponse buscarPorCpf(String cpf);

    Page<UsuarioListItemResponse> listar(Pageable pageable, String nome);

    UsuarioResponse atualizar(Long id, UsuarioRequest request);

    void deletar(Long id);

    AuthResponse validarEmailParaPrimeiroAcesso(String email);

    UsuarioResponse definirSenhaPrimeiroAcesso(String email, String senha);

    LoginResponse login(String email, String senha);

    UsuarioResponse me(String email);

    UsuarioResponse atualizarMeuPerfil(String email, UsuarioProfileRequest request);

    AuthResponse solicitarRecuperacaoSenha(SolicitarRecuperacaoSenhaRequest request);

    AuthResponse verificarCodigoRecuperacao(VerificarCodigoRecuperacaoRequest request);

    UsuarioResponse redefinirSenha(RedefinirSenhaRequest request);
}
