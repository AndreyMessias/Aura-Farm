package com.aurafarm.backend.service.impl;

import com.aurafarm.backend.dto.mapper.FornecedorMapper;
import com.aurafarm.backend.dto.request.FornecedorRequest;
import com.aurafarm.backend.dto.response.FornecedorListItemResponse;
import com.aurafarm.backend.dto.response.FornecedorResponse;
import com.aurafarm.backend.entity.Fornecedor;
import com.aurafarm.backend.entity.Usuario;
import com.aurafarm.backend.exception.BusinessException;
import com.aurafarm.backend.exception.ResourceNotFoundException;
import com.aurafarm.backend.repository.FornecedorRepository;
import com.aurafarm.backend.repository.ProdutoRepository;
import com.aurafarm.backend.repository.UsuarioRepository;
import com.aurafarm.backend.service.FornecedorService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FornecedorServiceImpl implements FornecedorService {

    private final FornecedorRepository fornecedorRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;
    private final FornecedorMapper fornecedorMapper;

    @Override
    @Transactional
    public FornecedorResponse criar(FornecedorRequest request, String emailUsuarioLogado) {
        if (fornecedorRepository.existsByCnpj(request.getCnpj())) {
            throw new BusinessException("Esse CNPJ já possui cadastro", "CNPJ_DUPLICADO");
        }
        if (fornecedorRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Esse e-mail já possui cadastro", "EMAIL_DUPLICADO");
        }

        Usuario usuarioLogado = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "email", emailUsuarioLogado));

        Fornecedor fornecedor = fornecedorMapper.toEntity(request);
        fornecedor.setUsuario(usuarioLogado);
        if (request.getPais() == null) {
            fornecedor.setPais("Brasil");
        }

        fornecedor = fornecedorRepository.save(fornecedor);
        return fornecedorMapper.toResponse(fornecedor);
    }

    @Override
    public FornecedorResponse buscarPorId(Long id) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", "id", id));
        return fornecedorMapper.toResponse(fornecedor);
    }

    @Override
    public Page<FornecedorListItemResponse> listar(Pageable pageable, String nome, String cnpj) {
        Page<Fornecedor> page;
        if (cnpj != null && !cnpj.isBlank()) {
            page = fornecedorRepository.findByCnpjContaining(cnpj, pageable);
        } else if (nome != null && !nome.isBlank()) {
            page = fornecedorRepository.findByNomeContainingIgnoreCase(nome, pageable);
        } else {
            page = fornecedorRepository.findAll(pageable);
        }
        return page.map(fornecedorMapper::toListItem);
    }

    @Override
    @Transactional
    public FornecedorResponse atualizar(Long id, FornecedorRequest request) {
        Fornecedor fornecedor = fornecedorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", "id", id));

        if (!fornecedor.getCnpj().equals(request.getCnpj()) && fornecedorRepository.existsByCnpj(request.getCnpj())) {
            throw new BusinessException("Esse CNPJ já possui cadastro", "CNPJ_DUPLICADO");
        }
        if (!fornecedor.getEmail().equals(request.getEmail()) && fornecedorRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Esse e-mail já possui cadastro", "EMAIL_DUPLICADO");
        }

        fornecedorMapper.updateEntity(request, fornecedor);

        fornecedor = fornecedorRepository.save(fornecedor);
        return fornecedorMapper.toResponse(fornecedor);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!fornecedorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Fornecedor", "id", id);
        }
        if (produtoRepository.existsByFornecedorId(id)) {
            throw new BusinessException(
                    "Não é possível excluir fornecedores com produtos no estoque, ou cadastrados a uma venda não finalizada",
                    "CONFLICT");
        }
        fornecedorRepository.deleteById(id);
    }
}
