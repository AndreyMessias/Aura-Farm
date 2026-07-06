package com.aurafarm.backend.service;

import com.aurafarm.backend.dto.request.FornecedorRequest;
import com.aurafarm.backend.dto.response.FornecedorListItemResponse;
import com.aurafarm.backend.dto.response.FornecedorResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FornecedorService {

    FornecedorResponse criar(FornecedorRequest request, String emailUsuarioLogado);

    FornecedorResponse buscarPorId(Long id);

    Page<FornecedorListItemResponse> listar(Pageable pageable, String nome, String cnpj);

    FornecedorResponse atualizar(Long id, FornecedorRequest request);

    void deletar(Long id);
}
