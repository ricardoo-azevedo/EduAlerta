(async function () {
  if (!await window.eduAlertaPaginaPronta) return;

  const $ = id => document.getElementById(id);
  const mensagem = $("mensagemConta");
  const botaoSalvar = $("salvarNomeConta");
  const botaoDesativar = $("desativarMinhaConta");

  function avisar(texto, sucesso = false) {
    mensagem.replaceChildren();
    if (!texto) return;
    const div = document.createElement("div");
    div.className = `alert alert-${sucesso ? "success" : "danger"}`;
    div.textContent = texto;
    mensagem.appendChild(div);
  }

  function mostrar(usuario) {
    $("emailConta").textContent = usuario.email || "—";
    $("perfilConta").textContent = usuario.perfil === "ADM" ? "Administrador" : "Secretaria";
    $("statusConta").textContent = usuario.ativo ? "Ativa" : "Inativa";
    $("nomeConta").value = usuario.nome || "";
    window.usuarioEduAlerta = usuario;
    montarLayout();
  }

  async function carregarConta() {
    try {
      const usuario = await requisicaoAPI("/usuarios/me");
      mostrar(usuario);
    } catch (erro) {
      avisar(erro.message || "Não foi possível carregar sua conta.");
    }
  }

  $("formNomeConta").addEventListener("submit", async evento => {
    evento.preventDefault();
    const nome = $("nomeConta").value.trim();
    if (!nome) {
      avisar("Informe um nome válido.");
      return;
    }
    botaoSalvar.disabled = true;
    avisar("");
    try {
      const usuario = await requisicaoAPI("/usuarios/me", {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nome })
      });
      mostrar(usuario);
      avisar("Seu nome foi atualizado.", true);
    } catch (erro) {
      avisar(erro.message || "Não foi possível atualizar seu nome.");
    } finally {
      botaoSalvar.disabled = false;
    }
  });

  botaoDesativar.addEventListener("click", async () => {
    if (!confirm("Deseja realmente desativar sua própria conta? Você perderá o acesso ao sistema.")) return;
    botaoDesativar.disabled = true;
    avisar("");
    try {
      await requisicaoAPI("/usuarios/me", { method: "DELETE" });
      await encerrarSessaoERedirecionar();
    } catch (erro) {
      avisar(erro.message || "Não foi possível desativar sua conta.");
      botaoDesativar.disabled = false;
    }
  });

  await carregarConta();
})();