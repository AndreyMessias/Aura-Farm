package com.aurafarm.backend.controller;

import com.aurafarm.backend.dto.request.ProdutoRequest;
import com.aurafarm.backend.dto.response.ProdutoListItemResponse;
import com.aurafarm.backend.dto.response.ProdutoResponse;
import com.aurafarm.backend.enums.Tamanho;
import com.aurafarm.backend.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/aurafarm/produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoRequest request,
                                                   Authentication authentication) {
        ProdutoResponse response = produtoService.criar(request, authentication.getName());
        return ResponseEntity.created(URI.create("/aurafarm/produtos/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable Long id) {
        ProdutoResponse response = produtoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoListItemResponse>> listar(Pageable pageable,
                                                                  @RequestParam(required = false) String codigo,
                                                                  @RequestParam(required = false) String nome,
                                                                  @RequestParam(required = false) Long fornecedorId,
                                                                  @RequestParam(required = false) Tamanho tamanho,
                                                                  @RequestParam(required = false) String cor) {
        Page<ProdutoListItemResponse> page = produtoService.listar(pageable, codigo, nome, fornecedorId, tamanho, cor);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) {
        ProdutoResponse response = produtoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        produtoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
