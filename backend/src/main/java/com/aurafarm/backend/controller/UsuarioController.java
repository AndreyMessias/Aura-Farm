package com.aurafarm.backend.controller;

import com.aurafarm.backend.dto.request.UsuarioRequest;
import com.aurafarm.backend.dto.response.UsuarioListItemResponse;
import com.aurafarm.backend.dto.response.UsuarioResponse;
import com.aurafarm.backend.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/aurafarm/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> criar(@Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse response = usuarioService.criar(request);
        return ResponseEntity.created(URI.create("/aurafarm/usuarios/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> buscarPorId(@PathVariable Long id) {
        UsuarioResponse response = usuarioService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<UsuarioResponse> buscarPorCpf(@PathVariable String cpf) {
        UsuarioResponse response = usuarioService.buscarPorCpf(cpf);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<UsuarioListItemResponse>> listar(Pageable pageable,
                                                                @RequestParam(required = false) String nome) {
        Page<UsuarioListItemResponse> page = usuarioService.listar(pageable, nome);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioResponse> atualizar(@PathVariable Long id, @Valid @RequestBody UsuarioRequest request) {
        UsuarioResponse response = usuarioService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        usuarioService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}