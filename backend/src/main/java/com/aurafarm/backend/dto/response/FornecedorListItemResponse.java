package com.aurafarm.backend.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorListItemResponse {

    private Long id;
    private String nome;
    private String email;
    private String cnpj;
}
