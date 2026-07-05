package com.aurafarm.backend.validation.cpfunico;

import com.aurafarm.backend.repository.UsuarioRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CpfUnicoValidator implements ConstraintValidator<CpfUnico, String> {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public CpfUnicoValidator(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null || cpf.isBlank()) {
            return true;
        }
        String cleanCpf = cpf.replaceAll("[^0-9]", "");
        return !usuarioRepository.existsByCpf(cleanCpf);
    }
}