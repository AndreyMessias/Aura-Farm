package com.aurafarm.backend.service;

import com.aurafarm.backend.dto.request.ProdutoRequest;
import com.aurafarm.backend.dto.response.ProdutoListItemResponse;
import com.aurafarm.backend.dto.response.ProdutoResponse;
import com.aurafarm.backend.enums.Tamanho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoService {

    ProdutoResponse criar(ProdutoRequest request, String emailUsuarioLogado);

    ProdutoResponse buscarPorId(Long id);

    Page<ProdutoListItemResponse> listar(Pageable pageable, String codigo, String nome, Long fornecedorId,
                                         Tamanho tamanho, String cor);

    ProdutoResponse atualizar(Long id, ProdutoRequest request);

    void deletar(Long id);
}
