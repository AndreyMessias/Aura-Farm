package com.aurafarm.backend.service;

public interface EmailService {

    void enviarCodigoRecuperacaoSenha(String destinatario, String nome, String codigo);
}
