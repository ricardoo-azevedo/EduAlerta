const formularioSenha = document.getElementById("formAlterarSenha");
const mensagemSenha = document.getElementById("mensagemSenha");
const botaoAlterarSenha = document.getElementById("btnAlterarSenha");

iniciarTelaAlteracaoSenha();


async function iniciarTelaAlteracaoSenha() {

  await protegerTrocaSenha();
}


formularioSenha.addEventListener("submit", async event => {

  event.preventDefault();

  mensagemSenha.textContent = "";

  const novaSenha = document.getElementById("novaSenha").value;
  const confirmarSenha = document.getElementById("confirmarSenha").value;

  if (novaSenha.length < 8 || novaSenha.length > 72) {

    mensagemSenha.textContent =
      "A senha deve possuir entre 8 e 72 caracteres.";

    return;
  }

  if (novaSenha !== confirmarSenha) {

    mensagemSenha.textContent =
      "As senhas informadas não são iguais.";

    return;
  }

  botaoAlterarSenha.disabled = true;
  botaoAlterarSenha.textContent = "Alterando senha...";

  try {

    await requisicaoAPI("/usuarios/me/senha", {
      method: "PUT",

      headers: {
        "Content-Type": "application/json"
      },

      body: JSON.stringify({
        novaSenha: novaSenha
      })
    });

    window.location.replace("painel.html");

  } catch (erro) {

    mensagemSenha.textContent =
      erro.message || "Não foi possível alterar sua senha.";

  } finally {

    botaoAlterarSenha.disabled = false;

    botaoAlterarSenha.innerHTML =
      '<i class="bi bi-check-circle me-2"></i>Alterar senha';
  }
});
