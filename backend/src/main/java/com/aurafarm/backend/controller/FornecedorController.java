package com.aurafarm.backend.controller;

import com.aurafarm.backend.dto.request.FornecedorRequest;
import com.aurafarm.backend.dto.response.FornecedorListItemResponse;
import com.aurafarm.backend.dto.response.FornecedorResponse;
import com.aurafarm.backend.service.FornecedorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/aurafarm/fornecedores")
@RequiredArgsConstructor
public class FornecedorController {

    private final FornecedorService fornecedorService;

    @PostMapping
    public ResponseEntity<FornecedorResponse> criar(@Valid @RequestBody FornecedorRequest request,
                                                      Authentication authentication) {
        FornecedorResponse response = fornecedorService.criar(request, authentication.getName());
        return ResponseEntity.created(URI.create("/aurafarm/fornecedores/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FornecedorResponse> buscarPorId(@PathVariable Long id) {
        FornecedorResponse response = fornecedorService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<FornecedorListItemResponse>> listar(Pageable pageable,
                                                                     @RequestParam(required = false) String nome,
                                                                     @RequestParam(required = false) String cnpj) {
        Page<FornecedorListItemResponse> page = fornecedorService.listar(pageable, nome, cnpj);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FornecedorResponse> atualizar(@PathVariable Long id, @Valid @RequestBody FornecedorRequest request) {
        FornecedorResponse response = fornecedorService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        fornecedorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
