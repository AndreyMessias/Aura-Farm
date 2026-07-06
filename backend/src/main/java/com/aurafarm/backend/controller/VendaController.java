package com.aurafarm.backend.controller;

import com.aurafarm.backend.dto.request.AlterarStatusVendaRequest;
import com.aurafarm.backend.dto.request.VendaRequest;
import com.aurafarm.backend.dto.response.VendaListItemResponse;
import com.aurafarm.backend.dto.response.VendaResponse;
import com.aurafarm.backend.service.VendaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;

@RestController
@RequestMapping("/aurafarm/vendas")
@RequiredArgsConstructor
public class VendaController {

    private final VendaService vendaService;

    @PostMapping
    public ResponseEntity<VendaResponse> criar(@Valid @RequestBody VendaRequest request,
                                                 Authentication authentication) {
        VendaResponse response = vendaService.criar(request, authentication.getName());
        return ResponseEntity.created(URI.create("/aurafarm/vendas/" + response.getId())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendaResponse> buscarPorId(@PathVariable Long id) {
        VendaResponse response = vendaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<Page<VendaListItemResponse>> listar(
            Pageable pageable,
            @RequestParam(required = false) Long numero,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim) {
        Page<VendaListItemResponse> page = vendaService.listar(
                pageable,
                numero,
                dataInicio != null ? dataInicio.atStartOfDay() : null,
                dataFim != null ? dataFim.atTime(LocalTime.MAX) : null);
        return ResponseEntity.ok(page);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<VendaResponse> alterarStatus(@PathVariable Long id,
                                                          @Valid @RequestBody AlterarStatusVendaRequest request) {
        VendaResponse response = vendaService.alterarStatus(id, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendaResponse> atualizar(@PathVariable Long id, @Valid @RequestBody VendaRequest request) {
        VendaResponse response = vendaService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        vendaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
