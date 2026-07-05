package com.aurafarm.backend.dto.response;

import com.aurafarm.backend.enums.Cargo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;
    private String nome;
    private String email;
    private Cargo cargo;
}