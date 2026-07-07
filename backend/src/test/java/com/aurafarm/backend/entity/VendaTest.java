package com.aurafarm.backend.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class VendaTest {

    private Venda venda;

    @BeforeEach
    void setUp() {
        venda = Venda.builder().build();
    }

    private ItemVenda criarItem(String precoUnitario, int quantidade) {
        return ItemVenda.builder()
                .precoUnitario(new BigDecimal(precoUnitario))
                .quantidade(quantidade)
                .build();
    }

    @Test
    @DisplayName("Deve calcular o valor total multiplicando preço unitário pela quantidade")
    void deveCalcularValorTotalMultiplicandoPrecoPelaQuantidade() {
        venda.adicionarItem(criarItem("49.90", 2));

        assertThat(venda.getValorTotal()).isEqualByComparingTo("99.80");
        assertThat(venda.getQuantidadeTotal()).isEqualTo(2);
    }

    @Test
    @DisplayName("Deve somar valor total e quantidade de vários itens da venda")
    void deveSomarTotaisDeVariosItens() {
        venda.adicionarItem(criarItem("49.90", 2));
        venda.adicionarItem(criarItem("199.00", 1));

        assertThat(venda.getValorTotal()).isEqualByComparingTo("298.80");
        assertThat(venda.getQuantidadeTotal()).isEqualTo(3);
    }

    @Test
    @DisplayName("Deve recalcular os totais ao remover um item da venda")
    void deveRecalcularTotaisAoRemoverItem() {
        ItemVenda item1 = criarItem("49.90", 2);
        ItemVenda item2 = criarItem("199.00", 1);
        venda.adicionarItem(item1);
        venda.adicionarItem(item2);

        venda.removerItem(item1);

        assertThat(venda.getValorTotal()).isEqualByComparingTo("199.00");
        assertThat(venda.getQuantidadeTotal()).isEqualTo(1);
        assertThat(venda.getItens()).containsExactly(item2);
    }

    @Test
    @DisplayName("Deve iniciar com valor total e quantidade zerados quando não há itens")
    void deveIniciarComValoresZeradosSemItens() {
        assertThat(venda.getValorTotal()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(venda.getQuantidadeTotal()).isZero();
    }
}
