package com.aurafarm.backend.validation.cpfvalido;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CpfValidoValidatorTest {

    private CpfValidoValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CpfValidoValidator();
    }

    @Test
    @DisplayName("Deve validar CPF com dígito verificador correto")
    void deveValidarCpfCorreto() {
        assertThat(validator.isValid("39711291452", null)).isTrue();
    }

    @Test
    @DisplayName("Deve validar CPF correto mesmo formatado com pontuação")
    void deveValidarCpfCorretoComFormatacao() {
        assertThat(validator.isValid("397.112.914-52", null)).isTrue();
    }

    @Test
    @DisplayName("Deve rejeitar CPF com todos os dígitos iguais (ex.: 111.111.111-11)")
    void deveRejeitarCpfComTodosDigitosIguais() {
        assertThat(validator.isValid("11111111111", null)).isFalse();
        assertThat(validator.isValid("00000000000", null)).isFalse();
    }

    @Test
    @DisplayName("Deve rejeitar CPF com quantidade de dígitos diferente de 11")
    void deveRejeitarCpfComTamanhoErrado() {
        assertThat(validator.isValid("1234567890", null)).isFalse();
    }

    @Test
    @DisplayName("Deve rejeitar CPF com dígito verificador incorreto")
    void deveRejeitarCpfComDigitoVerificadorErrado() {
        assertThat(validator.isValid("39711291453", null)).isFalse();
    }

    @Test
    @DisplayName("Deve rejeitar CPF nulo, vazio ou em branco")
    void deveRejeitarCpfNuloOuVazio() {
        assertThat(validator.isValid(null, null)).isFalse();
        assertThat(validator.isValid("", null)).isFalse();
        assertThat(validator.isValid("   ", null)).isFalse();
    }
}
