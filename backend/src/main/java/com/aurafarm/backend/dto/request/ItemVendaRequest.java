package com.aurafarm.backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemVendaRequest {

    @NotBlank(message = "Código do produto é obrigatório")
    private String codigoProduto;

    @NotNull(message = "Quantidade é obrigatória")
    @Min(value = 1, message = "Quantidade de produtos deve ser maior que zero")
    private Integer quantidade;
}
