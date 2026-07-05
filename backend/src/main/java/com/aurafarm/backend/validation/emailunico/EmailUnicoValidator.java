package com.aurafarm.backend.validation.emailunico;

import com.aurafarm.backend.repository.UsuarioRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailUnicoValidator implements ConstraintValidator<EmailUnico, String> {

    private final UsuarioRepository usuarioRepository;

    @Autowired
    public EmailUnicoValidator(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true;
        }
        return !usuarioRepository.existsByEmail(email);
    }
}