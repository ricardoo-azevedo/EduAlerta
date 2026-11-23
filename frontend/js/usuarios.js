(async function () {
  if (!await window.eduAlertaPaginaPronta) return;

  const $ = id => document.getElementById(id);
  const usuarioAtual = window.usuarioEduAlerta;
  const ehAdm = usuarioAtual.perfil === "ADM";
  const areaCadastro = $("areaCadastro");
  const lista = $("listaUsuarios");
  const mensagemLista = $("mensagemLista");
  const mensagemCadastro = $("mensagemCadastro");
  const botaoAtualizar = $("btnAtualizarUsuarios");
  const botaoCadastrar = $("btnCadastrarUsuario");
  const campoBusca = $("buscaUsuario");

  function avisar(elemento, texto, sucesso = false) {
    elemento.replaceChildren();
    if (!texto) return;
    const div = document.createElement("div");
    div.className = `alert alert-${sucesso ? "success" : "danger"}`;
    div.textContent = texto;
    elemento.appendChild(div);
  }

  function celula(linha, texto) {
    const td = document.createElement("td");
    td.textContent = String(texto ?? "—");
    linha.appendChild(td);
    return td;
  }

  function indicador(linha, texto, classe) {
    const td = celula(linha, "");
    const span = document.createElement("span");
    span.className = `badge ${classe}`;
    span.textContent = texto;
    td.appendChild(span);
  }

  function botaoAcao(container, texto, classe, callback) {
    const botao = document.createElement("button");
    botao.type = "button";
    botao.className = `btn btn-sm ${classe} me-1 mb-1`;
    botao.textContent = texto;
    botao.addEventListener("click", callback);
    container.appendChild(botao);
  }

  function mensagemVazia(texto) {
    lista.replaceChildren();
    const linha = document.createElement("tr");
    const td = celula(linha, texto);
    td.colSpan = ehAdm ? 6 : 5;
    td.className = "text-center text-secondary py-4";
    lista.appendChild(linha);
  }

  async function alterarNome(usuario) {
    const entrada = prompt("Informe o novo nome da secretaria:", usuario.nome);
    if (entrada === null) return;
    const nome = entrada.trim();
    if (!nome) {
      avisar(mensagemLista, "O nome não pode ficar vazio.");
      return;
    }
    try {
      await requisicaoAPI(`/usuarios/${usuario.id}/nome`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nome })
      });
      await carregarUsuarios();
      avisar(mensagemLista, "Nome atualizado com sucesso.", true);
    } catch (erro) {
      avisar(mensagemLista, erro.message || "Não foi possível alterar o nome.");
    }
  }

  async function alterarStatus(usuario) {
    const ativo = !usuario.ativo;
    if (!confirm(`Deseja ${ativo ? "reativar" : "desativar"} a conta de ${usuario.nome}?`)) return;
    try {
      await requisicaoAPI(`/usuarios/${usuario.id}/status`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ ativo })
      });
      await carregarUsuarios();
      avisar(mensagemLista, `Conta ${ativo ? "reativada" : "desativada"} com sucesso.`, true);
    } catch (erro) {
      avisar(mensagemLista, erro.message || "Não foi possível alterar o status.");
    }
  }

  function renderizar(usuarios) {
    lista.replaceChildren();
    if (usuarios.length === 0) {
      mensagemVazia("Nenhum usuário encontrado.");
      return;
    }
    usuarios.forEach(usuario => {
      const linha = document.createElement("tr");
      celula(linha, usuario.nome);
      celula(linha, usuario.email);
      celula(linha, usuario.perfil);
      indicador(linha, usuario.ativo ? "Ativo" : "Inativo", usuario.ativo ? "bg-success" : "bg-danger");
      indicador(linha, usuario.alterarSenhaObrigatorio ? "Pendente" : "Alterada",
        usuario.alterarSenhaObrigatorio ? "bg-warning text-dark" : "bg-success");
      if (ehAdm) {
        const acoes = celula(linha, "");
        if (usuario.perfil === "SECRETARIA" && usuario.id !== usuarioAtual.id) {
          botaoAcao(acoes, "Nome", "btn-outline-primary", () => alterarNome(usuario));
          botaoAcao(acoes, usuario.ativo ? "Desativar" : "Ativar",
            usuario.ativo ? "btn-outline-danger" : "btn-outline-success", () => alterarStatus(usuario));
        } else {
          acoes.textContent = "—";
        }
      }
      lista.appendChild(linha);
    });
  }

  async function carregarUsuarios() {
    const nome = campoBusca.value.trim();
    const caminho = nome ? `/usuarios/buscar?nome=${encodeURIComponent(nome)}` : "/usuarios";
    botaoAtualizar.disabled = true;
    avisar(mensagemLista, "");
    mensagemVazia("Consultando usuários...");
    try {
      const usuarios = await requisicaoAPI(caminho);
      if (!Array.isArray(usuarios)) throw new Error("Resposta inesperada da API de usuários.");
      renderizar(usuarios);
    } catch (erro) {
      mensagemVazia("Não foi possível consultar os usuários.");
      avisar(mensagemLista, erro.message || "Falha ao carregar usuários.");
    } finally {
      botaoAtualizar.disabled = false;
    }
  }

  areaCadastro.hidden = !ehAdm;
  if (!ehAdm) $("cabecalhoAcoes").hidden = true;

  if (ehAdm) {
    $("formCadastroUsuario").addEventListener("submit", async evento => {
      evento.preventDefault();
      const nome = $("nomeUsuario").value.trim();
      const email = $("emailUsuario").value.trim();
      if (!nome || !email) {
        avisar(mensagemCadastro, "Preencha nome e e-mail.");
        return;
      }
      botaoCadastrar.disabled = true;
      avisar(mensagemCadastro, "");
      try {
        await requisicaoAPI("/usuarios", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ nome, email })
        });
        $("formCadastroUsuario").reset();
        await carregarUsuarios();
        avisar(mensagemCadastro, "Secretaria cadastrada. A senha temporária foi enviada por e-mail.", true);
      } catch (erro) {
        avisar(mensagemCadastro, erro.message || "Não foi possível cadastrar a secretaria.");
      } finally {
        botaoCadastrar.disabled = false;
      }
    });
  }

  $("formBuscaUsuario").addEventListener("submit", evento => {
    evento.preventDefault();
    carregarUsuarios();
  });
  $("limparBuscaUsuario").addEventListener("click", () => {
    campoBusca.value = "";
    carregarUsuarios();
  });
  botaoAtualizar.addEventListener("click", carregarUsuarios);
  await carregarUsuarios();
})();