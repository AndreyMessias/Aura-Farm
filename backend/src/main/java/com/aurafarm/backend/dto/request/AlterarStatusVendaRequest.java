package com.aurafarm.backend.dto.request;

import com.aurafarm.backend.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlterarStatusVendaRequest {

    @NotNull(message = "Status é obrigatório")
    private StatusPedido status;
}
