package com.aurafarm.backend.service.impl;

import com.aurafarm.backend.dto.mapper.ProdutoMapper;
import com.aurafarm.backend.dto.request.ProdutoRequest;
import com.aurafarm.backend.dto.response.ProdutoListItemResponse;
import com.aurafarm.backend.dto.response.ProdutoResponse;
import com.aurafarm.backend.entity.Fornecedor;
import com.aurafarm.backend.entity.Produto;
import com.aurafarm.backend.entity.Usuario;
import com.aurafarm.backend.enums.Tamanho;
import com.aurafarm.backend.exception.BusinessException;
import com.aurafarm.backend.exception.ResourceNotFoundException;
import com.aurafarm.backend.repository.FornecedorRepository;
import com.aurafarm.backend.repository.ItemVendaRepository;
import com.aurafarm.backend.repository.ProdutoRepository;
import com.aurafarm.backend.repository.UsuarioRepository;
import com.aurafarm.backend.service.ProdutoService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final FornecedorRepository fornecedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ItemVendaRepository itemVendaRepository;
    private final ProdutoMapper produtoMapper;

    @Override
    @Transactional
    public ProdutoResponse criar(ProdutoRequest request, String emailUsuarioLogado) {
        if (produtoRepository.existsByCodigo(request.getCodigo())) {
            throw new BusinessException("Código do produto já cadastrado", "CODIGO_DUPLICADO");
        }

        Fornecedor fornecedor = fornecedorRepository.findById(request.getFornecedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", "id", request.getFornecedorId()));
        Usuario usuarioLogado = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "email", emailUsuarioLogado));

        Produto produto = produtoMapper.toEntity(request);
        produto.setFornecedor(fornecedor);
        produto.setUsuario(usuarioLogado);

        produto = produtoRepository.save(produto);
        return produtoMapper.toResponse(produto);
    }

    @Override
    public ProdutoResponse buscarPorId(Long id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));
        return produtoMapper.toResponse(produto);
    }

    @Override
    public Page<ProdutoListItemResponse> listar(Pageable pageable, String codigo, String nome, Long fornecedorId,
                                                 Tamanho tamanho, String cor) {
        Specification<Produto> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (codigo != null && !codigo.isBlank()) {
                predicates.add(cb.equal(root.get("codigo"), codigo));
            }
            if (nome != null && !nome.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%"));
            }
            if (fornecedorId != null) {
                predicates.add(cb.equal(root.get("fornecedor").get("id"), fornecedorId));
            }
            if (tamanho != null) {
                predicates.add(cb.equal(root.get("tamanho"), tamanho));
            }
            if (cor != null && !cor.isBlank()) {
                predicates.add(cb.like(cb.lower(root.get("cor")), "%" + cor.toLowerCase() + "%"));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        return produtoRepository.findAll(spec, pageable).map(produtoMapper::toListItem);
    }

    @Override
    @Transactional
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto", "id", id));

        if (!produto.getCodigo().equals(request.getCodigo()) && produtoRepository.existsByCodigo(request.getCodigo())) {
            throw new BusinessException("Código do produto já cadastrado", "CODIGO_DUPLICADO");
        }

        Fornecedor fornecedor = fornecedorRepository.findById(request.getFornecedorId())
                .orElseThrow(() -> new ResourceNotFoundException("Fornecedor", "id", request.getFornecedorId()));

        produtoMapper.updateEntity(request, produto);
        produto.setFornecedor(fornecedor);

        produto = produtoRepository.save(produto);
        return produtoMapper.toResponse(produto);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!produtoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto", "id", id);
        }
        if (itemVendaRepository.existsByProdutoId(id)) {
            throw new BusinessException("Não é possível excluir produtos com vendas vinculadas", "CONFLICT");
        }
        produtoRepository.deleteById(id);
    }
}
