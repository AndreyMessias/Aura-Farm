package com.aurafarm.backend.service.impl;

import com.aurafarm.backend.dto.response.DashboardResponse;
import com.aurafarm.backend.dto.response.VendaListItemResponse;
import com.aurafarm.backend.entity.Endereco;
import com.aurafarm.backend.entity.Venda;
import com.aurafarm.backend.enums.StatusPedido;
import com.aurafarm.backend.repository.ProdutoRepository;
import com.aurafarm.backend.repository.VendaRepository;
import com.aurafarm.backend.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final ProdutoRepository produtoRepository;
    private final VendaRepository vendaRepository;

    @Override
    public DashboardResponse obterDashboard() {
        return DashboardResponse.builder()
                .totalProdutos(produtoRepository.count())
                .vendasPendentes(vendaRepository.countByStatus(StatusPedido.PENDENTE))
                .vendasEnviadas(vendaRepository.countByStatus(StatusPedido.ENVIADO))
                .vendasEntregues(vendaRepository.countByStatus(StatusPedido.ENTREGUE))
                .ultimasVendas(vendaRepository.findTop5ByOrderByDataPedidoDesc().stream()
                        .map(this::toListItem)
                        .toList())
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
