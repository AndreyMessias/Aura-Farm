package com.aurafarm.backend.service.impl;

import com.aurafarm.backend.entity.Venda;
import com.aurafarm.backend.enums.StatusPedido;
import com.aurafarm.backend.exception.BusinessException;
import com.aurafarm.backend.exception.ResourceNotFoundException;
import com.aurafarm.backend.repository.ProdutoRepository;
import com.aurafarm.backend.repository.UsuarioRepository;
import com.aurafarm.backend.repository.VendaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VendaServiceImplTest {

    @Mock
    private VendaRepository vendaRepository;
    @Mock
    private ProdutoRepository produtoRepository;
    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private VendaServiceImpl vendaService;

    @Test
    @DisplayName("Deve excluir venda com status PENDENTE (RF017)")
    void deveExcluirVendaPendente() {
        Venda venda = Venda.builder().status(StatusPedido.PENDENTE).build();
        when(vendaRepository.findById(1L)).thenReturn(Optional.of(venda));

        vendaService.deletar(1L);

        verify(vendaRepository).delete(venda);
    }

    @Test
    @DisplayName("Deve excluir venda com status CANCELADO (RF017)")
    void deveExcluirVendaCancelada() {
        Venda venda = Venda.builder().status(StatusPedido.CANCELADO).build();
        when(vendaRepository.findById(2L)).thenReturn(Optional.of(venda));

        vendaService.deletar(2L);

        verify(vendaRepository).delete(venda);
    }

    @Test
    @DisplayName("Deve rejeitar exclusão de venda já ENVIADA (RF017)")
    void deveRejeitarExclusaoDeVendaEnviada() {
        Venda venda = Venda.builder().status(StatusPedido.ENVIADO).build();
        when(vendaRepository.findById(3L)).thenReturn(Optional.of(venda));

        assertThatThrownBy(() -> vendaService.deletar(3L))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Vendas enviadas ou entregues não podem ser excluídos");

        verify(vendaRepository, never()).delete(any(Venda.class));
    }

    @Test
    @DisplayName("Deve rejeitar exclusão de venda já ENTREGUE (RF017)")
    void deveRejeitarExclusaoDeVendaEntregue() {
        Venda venda = Venda.builder().status(StatusPedido.ENTREGUE).build();
        when(vendaRepository.findById(4L)).thenReturn(Optional.of(venda));

        assertThatThrownBy(() -> vendaService.deletar(4L))
                .isInstanceOf(BusinessException.class);

        verify(vendaRepository, never()).delete(any(Venda.class));
    }

    @Test
    @DisplayName("Deve lançar erro ao tentar excluir venda inexistente")
    void deveLancarErroAoExcluirVendaInexistente() {
        when(vendaRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vendaService.deletar(999L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(vendaRepository, never()).delete(any(Venda.class));
    }
}
