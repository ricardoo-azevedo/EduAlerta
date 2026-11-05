
package edu.uninassau.edualerta.services.impls;

import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;

@Service
public class SupabaseAuthService {

  private final RestClient restClient = RestClient.create();

  @Value("${supabase.url:}") private String supabaseUrl;

  @Value("${supabase.secret-key:}") private String secretKey;

  @Value("${supabase.publishable-key:}") private String publishableKey;

  public void validarConfiguracaoCriacaoUsuario() { exigirConfiguracaoAdmin(); }

  public UUID criarUsuario(String nome, String email, String senha) {

    exigirConfiguracaoAdmin();

    Map<String, Object> body =
        Map.of("email", email, "password", senha, "email_confirm", true,
               "user_metadata", Map.of("nome", nome));

    JsonNode resposta =
        restClient.post()
            .uri(urlBase() + "/auth/v1/admin/users")
            .header("apikey", secretKey)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
            .contentType(MediaType.APPLICATION_JSON)
            .body(body)
            .retrieve()
            .body(JsonNode.class);

    if (resposta == null || resposta.get("id") == null) {
      throw new IllegalStateException(
          "Supabase não retornou o identificador do usuário criado");
    }

    return UUID.fromString(resposta.get("id").asText());
  }

  public void atualizarSenha(String accessToken, String novaSenha) {

    exigirConfiguracaoUrl();

    if (!StringUtils.hasText(publishableKey)) {
      throw new IllegalStateException("Configure supabase.publishable-key " +
                                      "para permitir a alteração de senha");
    }

    Map<String, String> body = Map.of("password", novaSenha);

    restClient.put()
        .uri(urlBase() + "/auth/v1/user")
        .header("apikey", publishableKey)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        .contentType(MediaType.APPLICATION_JSON)
        .body(body)
        .retrieve()
        .toBodilessEntity();
  }

  public void excluirUsuario(UUID authUserId) {

    exigirConfiguracaoAdmin();

    restClient.delete()
        .uri(urlBase() + "/auth/v1/admin/users/{id}", authUserId)
        .header("apikey", secretKey)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + secretKey)
        .retrieve()
        .toBodilessEntity();
  }

  private void exigirConfiguracaoAdmin() {

    exigirConfiguracaoUrl();

    if (!StringUtils.hasText(secretKey)) {
      throw new IllegalStateException(
          "Configure supabase.secret-key para utilizar o cadastro de usuários");
    }
  }

  private void exigirConfiguracaoUrl() {

    if (!StringUtils.hasText(supabaseUrl)) {
      throw new IllegalStateException(
          "Configure supabase.url para utilizar a integração com Supabase");
    }
  }

  private String urlBase() { return supabaseUrl.trim().replaceAll("/+$", ""); }
}
