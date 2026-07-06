package com.aurafarm.backend.dto.response;

import com.aurafarm.backend.enums.StatusProduto;
import com.aurafarm.backend.enums.Tamanho;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoResponse {

    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private Long fornecedorId;
    private String fornecedorNome;
    private Tamanho tamanho;
    private String cor;
    private BigDecimal preco;
    private Integer quantidadeEstoque;
    private StatusProduto status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
