package com.aurafarm.backend.dto.response;

import com.aurafarm.backend.enums.Cargo;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {

    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private Cargo cargo;
    private String telefone;
    private String cidade;
    private String estado;
    private String pais;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}