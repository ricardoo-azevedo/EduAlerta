
package edu.uninassau.edualerta.services.impls;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

@Service
public class EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username:}") private String remetente;

  @Value("${spring.mail.password:}") private String senha;

  public EmailService(JavaMailSender mailSender) {
    this.mailSender = mailSender;
  }

  public void validarConfiguracao() {

    if (!StringUtils.hasText(remetente)) {
      throw new IllegalStateException(
          "Configure spring.mail.username para enviar e-mails");
    }

    if (!StringUtils.hasText(senha)) {
      throw new IllegalStateException(
          "Configure spring.mail.password para enviar e-mails");
    }
  }

  public void enviarSenhaTemporaria(String nome, String email,
                                    String senhaTemporaria) {

    validarConfiguracao();

    String nomeSeguro = HtmlUtils.htmlEscape(nome);
    String senhaSegura = HtmlUtils.htmlEscape(senhaTemporaria);

    String html =
        "<h2>Bem-vindo ao EduAlerta!</h2>"
        + "<p>Olá, " + nomeSeguro + ".</p>"
        + "<p>Sua conta foi cadastrada no EduAlerta.</p>"
        + "<p>Sua senha temporária:</p>"
        + "<p style=\"font-size: 20px;\"><strong>" + senhaSegura +
        "</strong></p>"
        + "<p>Entre na plataforma utilizando seu e-mail e essa senha.</p>"
        + "<p>Por segurança, você deverá cadastrar uma nova senha "
        + "no primeiro acesso.</p>"
        + "<p>Não compartilhe essa senha com ninguém.</p>";

    String texto =
        "Bem-vindo ao EduAlerta!\n\n"
        + "Olá, " + nome + ".\n\n"
        + "Sua conta foi cadastrada no EduAlerta.\n\n"
        + "Sua senha temporária é: " + senhaTemporaria + "\n\n"
        + "Entre na plataforma e altere sua senha no primeiro acesso.\n\n"
        + "Não compartilhe essa senha com ninguém.";

    try {
      MimeMessage mensagem = mailSender.createMimeMessage();

      MimeMessageHelper helper = new MimeMessageHelper(mensagem, true, "UTF-8");

      helper.setFrom(remetente);
      helper.setTo(email);
      helper.setSubject("Acesso ao EduAlerta");
      helper.setText(texto, html);

      mailSender.send(mensagem);

    } catch (MessagingException e) {
      throw new IllegalStateException(
          "Não foi possível preparar o e-mail de acesso", e);
    } catch (MailException e) {
      throw new IllegalStateException(
          "Não foi possível enviar o e-mail de acesso", e);
    }
  }
}
