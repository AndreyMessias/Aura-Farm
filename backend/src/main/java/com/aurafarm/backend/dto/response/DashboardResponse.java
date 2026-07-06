package com.aurafarm.backend.dto.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardResponse {

    private long totalProdutos;
    private long vendasPendentes;
    private long vendasEnviadas;
    private long vendasEntregues;
    private List<VendaListItemResponse> ultimasVendas;
}
