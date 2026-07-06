package com.aurafarm.backend.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VendaRequest {

    @NotEmpty(message = "Quantidade de produtos deve ser maior que zero")
    @Valid
    private List<ItemVendaRequest> itens;

    @NotNull(message = "Endereço é obrigatório")
    @Valid
    private EnderecoRequest endereco;
}
