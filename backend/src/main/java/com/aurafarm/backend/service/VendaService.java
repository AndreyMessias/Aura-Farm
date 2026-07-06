package com.aurafarm.backend.service;

import com.aurafarm.backend.dto.request.AlterarStatusVendaRequest;
import com.aurafarm.backend.dto.request.VendaRequest;
import com.aurafarm.backend.dto.response.VendaListItemResponse;
import com.aurafarm.backend.dto.response.VendaResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface VendaService {

    VendaResponse criar(VendaRequest request, String emailUsuarioLogado);

    VendaResponse buscarPorId(Long id);

    Page<VendaListItemResponse> listar(Pageable pageable, Long numero, LocalDateTime dataInicio, LocalDateTime dataFim);

    VendaResponse alterarStatus(Long id, AlterarStatusVendaRequest request);

    VendaResponse atualizar(Long id, VendaRequest request);

    void deletar(Long id);
}
