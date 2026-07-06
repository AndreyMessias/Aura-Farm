package com.aurafarm.backend.service.impl;

import com.aurafarm.backend.exception.BusinessException;
import com.aurafarm.backend.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final String remetente;

    public EmailServiceImpl(JavaMailSender mailSender, @Value("${mail.from}") String remetente) {
        this.mailSender = mailSender;
        this.remetente = remetente;
    }

    @Override
    public void enviarCodigoRecuperacaoSenha(String destinatario, String nome, String codigo) {
        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(destinatario);
        mensagem.setSubject("Aura Farm - Código de recuperação de senha");
        mensagem.setText("""
                Olá, %s.

                Use o código abaixo para redefinir sua senha no Aura Farm.
                Ele é válido por 5 minutos.

                Código: %s

                Se você não solicitou essa recuperação, ignore este e-mail.
                """.formatted(nome, codigo));

        try {
            mailSender.send(mensagem);
        } catch (MailException e) {
            log.error("Falha ao enviar e-mail de recuperação de senha para {}", destinatario, e);
            throw new BusinessException("Não foi possível enviar o e-mail de recuperação de senha", "ERRO_ENVIO_EMAIL");
        }
    }
}
