package com.aurafarm.backend.dto.request;

import com.aurafarm.backend.enums.Tamanho;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoRequest {

    @NotBlank(message = "Código é obrigatório")
    @Size(min = 11, max = 11, message = "Código deve ter 11 caracteres")
    private String codigo;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @Size(max = 100, message = "Descrição deve ter no máximo 100 caracteres")
    private String descricao;

    @NotNull(message = "Fornecedor é obrigatório")
    private Long fornecedorId;

    @NotNull(message = "Tamanho é obrigatório")
    private Tamanho tamanho;

    @Size(max = 100, message = "Cor deve ter no máximo 100 caracteres")
    private String cor;

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "Preço deve ser maior que zero")
    private BigDecimal preco;

    @NotNull(message = "Quantidade em estoque é obrigatória")
    @Min(value = 0, message = "Quantidade em estoque não pode ser negativa")
    private Integer quantidadeEstoque;
}
