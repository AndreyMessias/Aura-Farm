package com.aurafarm.backend.dto.response;

import com.aurafarm.backend.enums.StatusPedido;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendaListItemResponse {

    private Long id;
    private LocalDateTime dataPedido;
    private Integer quantidadeTotal;
    private BigDecimal valorTotal;
    private StatusPedido status;
    private String cidadeEntrega;
    private String estadoEntrega;
}
