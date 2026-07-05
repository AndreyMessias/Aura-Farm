package com.aurafarm.backend.validation.cpfunico;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CpfUnicoValidator.class)
@Documented
public @interface CpfUnico {
    String message() default "CPF já cadastrado";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}