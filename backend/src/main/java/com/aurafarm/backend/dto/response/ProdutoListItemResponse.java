package com.aurafarm.backend.dto.response;

import com.aurafarm.backend.enums.Tamanho;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoListItemResponse {

    private Long id;
    private String codigo;
    private String nome;
    private Tamanho tamanho;
    private Integer quantidadeEstoque;
    private BigDecimal preco;
}
