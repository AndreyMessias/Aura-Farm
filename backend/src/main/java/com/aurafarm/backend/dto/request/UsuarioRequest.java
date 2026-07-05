package com.aurafarm.backend.dto.request;

import com.aurafarm.backend.enums.Cargo;
import com.aurafarm.backend.validation.cpfvalido.CpfValido;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    @NotBlank(message = "CPF é obrigatório")
    @CpfValido
    private String cpf;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotNull(message = "Cargo é obrigatório")
    private Cargo cargo;

    @Size(max = 20, message = "Telefone deve ter no máximo 20 caracteres")
    private String telefone;

    @Size(max = 100, message = "Cidade deve ter no máximo 100 caracteres")
    private String cidade;

    @Size(max = 2, message = "Estado deve ter no máximo 2 caracteres")
    private String estado;

    @Size(max = 100, message = "País deve ter no máximo 100 caracteres")
    private String pais;
}