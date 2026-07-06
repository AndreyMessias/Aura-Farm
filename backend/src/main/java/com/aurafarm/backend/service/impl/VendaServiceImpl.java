package com.aurafarm.backend.service.impl;

import com.aurafarm.backend.dto.request.AlterarStatusVendaRequest;
import com.aurafarm.backend.dto.request.EnderecoRequest;
import com.aurafarm.backend.dto.request.ItemVendaRequest;
import com.aurafarm.backend.dto.request.VendaRequest;
import com.aurafarm.backend.dto.response.EnderecoResponse;
import com.aurafarm.backend.dto.response.ItemVendaResponse;
import com.aurafarm.backend.dto.response.VendaListItemResponse;
import com.aurafarm.backend.dto.response.VendaResponse;
import com.aurafarm.backend.entity.Endereco;
import com.aurafarm.backend.entity.ItemVenda;
import com.aurafarm.backend.entity.Produto;
import com.aurafarm.backend.entity.Usuario;
import com.aurafarm.backend.entity.Venda;
import com.aurafarm.backend.enums.StatusPedido;
import com.aurafarm.backend.exception.BusinessException;
import com.aurafarm.backend.exception.ResourceNotFoundException;
import com.aurafarm.backend.repository.ProdutoRepository;
import com.aurafarm.backend.repository.UsuarioRepository;
import com.aurafarm.backend.repository.VendaRepository;
import com.aurafarm.backend.service.VendaService;
import jakarta.persistence.criteria.Predicate;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class VendaServiceImpl implements VendaService {

    private final VendaRepository vendaRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional
    public VendaResponse criar(VendaRequest request, String emailUsuarioLogado) {
        Usuario usuarioLogado = usuarioRepository.findByEmail(emailUsuarioLogado)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário", "email", emailUsuarioLogado));

        Venda venda = Venda.builder()
                .usuario(usuarioLogado)
                .endereco(construirEndereco(request.getEndereco()))
                .build();

        adicionarItens(venda, request.getItens());

        venda = vendaRepository.save(venda);
        return toResponse(venda);
    }

    @Override
    public VendaResponse buscarPorId(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda", "id", id));
        return toResponse(venda);
    }

    @Override
    public Page<VendaListItemResponse> listar(Pageable pageable, Long numero, LocalDateTime dataInicio, LocalDateTime dataFim) {
        if (dataInicio != null && dataFim != null && !dataInicio.isBefore(dataFim)) {
            throw new BusinessException("Datas inicial e limite inválidas", "DATAS_INVALIDAS");
        }

        Specification<Venda> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (numero != null) {
                predicates.add(cb.equal(root.get("id"), numero));
            }
            if (dataInicio != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dataPedido"), dataInicio));
            }
            if (dataFim != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dataPedido"), dataFim));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<Venda> page = vendaRepository.findAll(spec, pageable);
        if (numero != null && page.isEmpty()) {
            throw new ResourceNotFoundException("Venda", "id", numero);
        }
        return page.map(this::toListItem);
    }

    @Override
    @Transactional
    public VendaResponse alterarStatus(Long id, AlterarStatusVendaRequest request) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda", "id", id));

        venda.setStatus(request.getStatus());
        venda = vendaRepository.save(venda);
        return toResponse(venda);
    }

    @Override
    @Transactional
    public VendaResponse atualizar(Long id, VendaRequest request) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda", "id", id));

        atualizarEndereco(venda.getEndereco(), request.getEndereco());

        new ArrayList<>(venda.getItens()).forEach(venda::removerItem);
        adicionarItens(venda, request.getItens());

        venda = vendaRepository.save(venda);
        return toResponse(venda);
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        Venda venda = vendaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venda", "id", id));

        if (venda.getStatus() != StatusPedido.PENDENTE && venda.getStatus() != StatusPedido.CANCELADO) {
            throw new BusinessException("Vendas enviadas ou entregues não podem ser excluídos", "CONFLICT");
        }

        vendaRepository.delete(venda);
    }

    private void adicionarItens(Venda venda, List<ItemVendaRequest> itensRequest) {
        for (ItemVendaRequest itemRequest : itensRequest) {
            Produto produto = produtoRepository.findByCodigo(itemRequest.getCodigoProduto())
                    .orElseThrow(() -> new BusinessException("Código do produto não encontrado", "PRODUTO_NAO_ENCONTRADO"));

            ItemVenda item = ItemVenda.builder()
                    .produto(produto)
                    .quantidade(itemRequest.getQuantidade())
                    .precoUnitario(produto.getPreco())
                    .build();
            venda.adicionarItem(item);
        }
    }

    private Endereco construirEndereco(EnderecoRequest request) {
        return Endereco.builder()
                .rua(request.getRua())
                .numero(request.getNumero())
                .bairro(request.getBairro())
                .cidade(request.getCidade())
                .estado(request.getEstado())
                .cep(request.getCep())
                .complemento(request.getComplemento())
                .build();
    }

    private void atualizarEndereco(Endereco endereco, EnderecoRequest request) {
        endereco.setRua(request.getRua());
        endereco.setNumero(request.getNumero());
        endereco.setBairro(request.getBairro());
        endereco.setCidade(request.getCidade());
        endereco.setEstado(request.getEstado());
        endereco.setCep(request.getCep());
        endereco.setComplemento(request.getComplemento());
    }

    private VendaResponse toResponse(Venda venda) {
        return VendaResponse.builder()
                .id(venda.getId())
                .dataPedido(venda.getDataPedido())
                .status(venda.getStatus())
                .valorTotal(venda.getValorTotal())
                .quantidadeTotal(venda.getQuantidadeTotal())
                .endereco(toEnderecoResponse(venda.getEndereco()))
                .itens(venda.getItens().stream().map(this::toItemResponse).toList())
                .build();
    }

    private ItemVendaResponse toItemResponse(ItemVenda item) {
        return ItemVendaResponse.builder()
                .produtoId(item.getProduto().getId())
                .codigoProduto(item.getProduto().getCodigo())
                .nomeProduto(item.getProduto().getNome())
                .quantidade(item.getQuantidade())
                .precoUnitario(item.getPrecoUnitario())
                .subtotal(item.getSubtotal())
                .build();
    }

    private EnderecoResponse toEnderecoResponse(Endereco endereco) {
        return EnderecoResponse.builder()
                .id(endereco.getId())
                .rua(endereco.getRua())
                .numero(endereco.getNumero())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado())
                .cep(endereco.getCep())
                .complemento(endereco.getComplemento())
                .build();
    }

    private VendaListItemResponse toListItem(Venda venda) {
        Endereco endereco = venda.getEndereco();
        return VendaListItemResponse.builder()
                .id(venda.getId())
                .dataPedido(venda.getDataPedido())
                .quantidadeTotal(venda.getQuantidadeTotal())
                .valorTotal(venda.getValorTotal())
                .status(venda.getStatus())
                .cidadeEntrega(endereco != null ? endereco.getCidade() : null)
                .estadoEntrega(endereco != null ? endereco.getEstado() : null)
                .build();
    }
}
