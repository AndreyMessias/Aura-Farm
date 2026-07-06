package com.aurafarm.backend.controller;

import com.aurafarm.backend.dto.request.DefinirSenhaRequest;
import com.aurafarm.backend.dto.request.LoginRequest;
import com.aurafarm.backend.dto.request.RedefinirSenhaRequest;
import com.aurafarm.backend.dto.request.SolicitarRecuperacaoSenhaRequest;
import com.aurafarm.backend.dto.request.ValidarEmailRequest;
import com.aurafarm.backend.dto.request.VerificarCodigoRecuperacaoRequest;
import com.aurafarm.backend.dto.response.AuthResponse;
import com.aurafarm.backend.dto.response.LoginResponse;
import com.aurafarm.backend.dto.response.UsuarioResponse;
import com.aurafarm.backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/aurafarm/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = usuarioService.login(request.getEmail(), request.getSenha());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/validar-email")
    public ResponseEntity<AuthResponse> validarEmail(@Valid @RequestBody ValidarEmailRequest request) {
        AuthResponse response = usuarioService.validarEmailParaPrimeiroAcesso(request.getEmail());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/definir-senha")
    public ResponseEntity<UsuarioResponse> definirSenha(@Valid @RequestBody DefinirSenhaRequest request) {
        if (!request.getSenha().equals(request.getConfirmacaoSenha())) {
            return ResponseEntity.badRequest().build();
        }
        UsuarioResponse response = usuarioService.definirSenhaPrimeiroAcesso(request.getEmail(), request.getSenha());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recuperar-senha/solicitar")
    public ResponseEntity<AuthResponse> solicitarRecuperacaoSenha(@Valid @RequestBody SolicitarRecuperacaoSenhaRequest request) {
        AuthResponse response = usuarioService.solicitarRecuperacaoSenha(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recuperar-senha/verificar")
    public ResponseEntity<AuthResponse> verificarCodigoRecuperacao(@Valid @RequestBody VerificarCodigoRecuperacaoRequest request) {
        AuthResponse response = usuarioService.verificarCodigoRecuperacao(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/recuperar-senha/redefinir")
    public ResponseEntity<UsuarioResponse> redefinirSenha(@Valid @RequestBody RedefinirSenhaRequest request) {
        UsuarioResponse response = usuarioService.redefinirSenha(request);
        return ResponseEntity.ok(response);
    }
}