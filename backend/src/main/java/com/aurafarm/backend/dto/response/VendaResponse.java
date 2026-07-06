package com.aurafarm.backend.dto.response;

import com.aurafarm.backend.enums.StatusPedido;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendaResponse {

    private Long id;
    private LocalDateTime dataPedido;
    private StatusPedido status;
    private BigDecimal valorTotal;
    private Integer quantidadeTotal;
    private EnderecoResponse endereco;
    private List<ItemVendaResponse> itens;
}
