const formulario = document.getElementById("formLogin");
const mensagem = document.getElementById("mensagemLogin");
const botao = document.getElementById("entrar");


formulario.addEventListener("submit", async event => {

  event.preventDefault();

  mensagem.textContent = "";

  if (!clienteAuth) {

    mensagem.textContent =
      "Configure os dados do Supabase no arquivo js/config.js.";

    return;
  }

  botao.disabled = true;
  botao.textContent = "Verificando acesso...";

  try {

    const email = document.getElementById("email").value.trim();
    const senha = document.getElementById("senha").value;

    const { error } = await clienteAuth.auth.signInWithPassword({
      email,
      password: senha
    });

    if (error) {
      throw new Error("E-mail ou senha inválidos.");
    }

    const usuario = await obterUsuarioAtual();

    if (
      usuario.perfil !== "SECRETARIA" ||
      usuario.ativo !== true
    ) {

      await clienteAuth.auth.signOut();

      throw new Error(
        "Você não possui autorização para acessar o EduAlerta."
      );
    }

    if (usuario.alterarSenhaObrigatorio) {

      window.location.replace("alterar-senha.html");

      return;
    }

    window.location.replace("painel.html");

  } catch (erro) {

    mensagem.textContent =
      erro.message || "Não foi possível realizar o login.";

  } finally {

    botao.disabled = false;

    botao.innerHTML =
      '<i class="bi bi-box-arrow-in-right me-2"></i>Entrar no sistema';
  }
});
