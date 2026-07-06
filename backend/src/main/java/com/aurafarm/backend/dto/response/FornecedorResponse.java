package com.aurafarm.backend.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FornecedorResponse {

    private Long id;
    private String nome;
    private String cnpj;
    private String email;
    private String telefone;
    private String cidade;
    private String estado;
    private String pais;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
