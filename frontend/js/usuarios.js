
async function iniciarUsuarios() {

  if (window.eduAlertaPaginaPronta) {
    const paginaAutorizada = await window.eduAlertaPaginaPronta;

    if (!paginaAutorizada) {
      return;
    }
  }

  const formularioCadastro = document.getElementById("formCadastroUsuario");
  const nomeUsuario = document.getElementById("nomeUsuario");
  const emailUsuario = document.getElementById("emailUsuario");

  const botaoCadastrar = document.getElementById("btnCadastrarUsuario");
  const botaoAtualizar = document.getElementById("btnAtualizarUsuarios");

  const mensagemCadastro = document.getElementById("mensagemCadastro");
  const mensagemLista = document.getElementById("mensagemLista");

  const listaUsuarios = document.getElementById("listaUsuarios");

  const cabecalhoAcoes = document.getElementById("cabecalhoAcoes");
  const areaCadastro = document.getElementById("areaCadastro");

  const usuarioLogado = window.usuarioEduAlerta;
  const ehAdm = usuarioLogado?.perfil === "ADM";

  function mostrarMensagem(elemento, mensagem, tipo) {
    elemento.textContent = mensagem;

    elemento.className = tipo === "sucesso"
      ? "alert alert-success"
      : "alert alert-danger";
  }

  function limparMensagem(elemento) {
    elemento.textContent = "";
    elemento.className = "";
  }

  function criarCelula(texto) {
    const celula = document.createElement("td");
    celula.textContent = texto ?? "";
    return celula;
  }

  function criarIndicadorStatus(ativo) {
    const indicador = document.createElement("span");

    indicador.className = ativo
      ? "badge bg-success"
      : "badge bg-danger";

    indicador.textContent = ativo
      ? "Ativo"
      : "Inativo";

    return indicador;
  }

  function criarIndicadorSenha(alterarSenhaObrigatorio) {
    const indicador = document.createElement("span");

    indicador.className = alterarSenhaObrigatorio
      ? "badge bg-warning text-dark"
      : "badge bg-success";

    indicador.textContent = alterarSenhaObrigatorio
      ? "Pendente"
      : "Alterada";

    return indicador;
  }

  function criarBotao(texto, classe, icone, aoClicar) {
    const botao = document.createElement("button");

    botao.type = "button";
    botao.className = `btn ${classe} btn-sm me-1`;

    botao.innerHTML = `<i class="bi ${icone} me-1"></i>${texto}`;

    botao.addEventListener("click", aoClicar);

    return botao;
  }

  async function alterarNome(usuario) {
    const novoNome = prompt(
      "Informe o novo nome da secretaria:",
      usuario.nome
    );

    if (novoNome === null) {
      return;
    }

    const nome = novoNome.trim();

    if (!nome) {
      mostrarMensagem(
        mensagemLista,
        "O nome não pode ficar vazio.",
        "erro"
      );

      return;
    }

    try {
      await requisicaoAPI(`/usuarios/${usuario.id}/nome`, {
        method: "PUT",

        headers: {
          "Content-Type": "application/json"
        },

        body: JSON.stringify({ nome })
      });

      mostrarMensagem(
        mensagemLista,
        "Nome atualizado com sucesso.",
        "sucesso"
      );

      await carregarUsuarios();

    } catch (erro) {
      mostrarMensagem(
        mensagemLista,
        erro.message || "Não foi possível alterar o nome.",
        "erro"
      );
    }
  }

  async function alterarStatus(usuario) {
    const novoStatus = !usuario.ativo;

    const acao = novoStatus
      ? "reativar"
      : "desativar";

    if (!confirm(`Deseja ${acao} a conta de ${usuario.nome}?`)) {
      return;
    }

    try {
      await requisicaoAPI(`/usuarios/${usuario.id}/status`, {
        method: "PUT",

        headers: {
          "Content-Type": "application/json"
        },

        body: JSON.stringify({
          ativo: novoStatus
        })
      });

      mostrarMensagem(
        mensagemLista,
        `Conta ${novoStatus ? "reativada" : "desativada"} com sucesso.`,
        "sucesso"
      );

      await carregarUsuarios();

    } catch (erro) {
      mostrarMensagem(
        mensagemLista,
        erro.message || "Não foi possível alterar o status.",
        "erro"
      );
    }
  }

  function renderizarUsuarios(usuarios) {
    listaUsuarios.innerHTML = "";

    if (!usuarios || usuarios.length === 0) {
      const linha = document.createElement("tr");
      const celula = document.createElement("td");

      celula.colSpan = ehAdm ? 6 : 5;
      celula.className = "text-center text-secondary";
      celula.textContent = "Nenhum usuário cadastrado.";

      linha.appendChild(celula);
      listaUsuarios.appendChild(linha);

      return;
    }

    usuarios.forEach(usuario => {
      const linha = document.createElement("tr");

      linha.appendChild(criarCelula(usuario.nome));
      linha.appendChild(criarCelula(usuario.email));
      linha.appendChild(criarCelula(usuario.perfil));

      const celulaStatus = document.createElement("td");

      celulaStatus.appendChild(
        criarIndicadorStatus(usuario.ativo)
      );

      linha.appendChild(celulaStatus);

      const celulaSenha = document.createElement("td");

      celulaSenha.appendChild(
        criarIndicadorSenha(usuario.alterarSenhaObrigatorio)
      );

      linha.appendChild(celulaSenha);

      if (ehAdm) {
        const celulaAcoes = document.createElement("td");

        if (
          usuario.perfil === "SECRETARIA" &&
          usuario.id !== usuarioLogado.id
        ) {
          celulaAcoes.appendChild(
            criarBotao(
              "Nome",
              "btn-outline-primary",
              "bi-pencil-square",
              () => alterarNome(usuario)
            )
          );

          celulaAcoes.appendChild(
            criarBotao(
              usuario.ativo ? "Desativar" : "Ativar",
              usuario.ativo
                ? "btn-outline-danger"
                : "btn-outline-success",
              usuario.ativo
                ? "bi-person-x"
                : "bi-person-check",
              () => alterarStatus(usuario)
            )
          );

        } else {
          celulaAcoes.textContent = "—";
        }

        linha.appendChild(celulaAcoes);
      }

      listaUsuarios.appendChild(linha);
    });
  }

  async function carregarUsuarios() {
    botaoAtualizar.disabled = true;

    limparMensagem(mensagemLista);

    listaUsuarios.innerHTML = `
      <tr>
        <td colspan="${ehAdm ? 6 : 5}" class="text-center text-secondary">
          Carregando usuários...
        </td>
      </tr>
    `;

    try {
      const usuarios = await requisicaoAPI("/usuarios");

      renderizarUsuarios(usuarios);

    } catch (erro) {
      listaUsuarios.innerHTML = "";

      mostrarMensagem(
        mensagemLista,
        erro.message || "Não foi possível carregar os usuários.",
        "erro"
      );

    } finally {
      botaoAtualizar.disabled = false;
    }
  }

  if (ehAdm) {
    areaCadastro.hidden = false;

    formularioCadastro.addEventListener("submit", async event => {
      event.preventDefault();

      limparMensagem(mensagemCadastro);

      const nome = nomeUsuario.value.trim();
      const email = emailUsuario.value.trim();

      if (!nome || !email) {
        mostrarMensagem(
          mensagemCadastro,
          "Preencha todos os campos.",
          "erro"
        );

        return;
      }

      botaoCadastrar.disabled = true;

      botaoCadastrar.innerHTML = `
        <span class="spinner-border spinner-border-sm me-2"></span>
        Cadastrando...
      `;

      try {
        await requisicaoAPI("/usuarios", {
          method: "POST",

          headers: {
            "Content-Type": "application/json"
          },

          body: JSON.stringify({
            nome,
            email
          })
        });

        formularioCadastro.reset();

        mostrarMensagem(
          mensagemCadastro,
          "Secretaria cadastrada! Os dados de acesso foram enviados por e-mail.",
          "sucesso"
        );

        await carregarUsuarios();

      } catch (erro) {
        mostrarMensagem(
          mensagemCadastro,
          erro.message || "Não foi possível cadastrar a secretaria.",
          "erro"
        );

      } finally {
        botaoCadastrar.disabled = false;

        botaoCadastrar.innerHTML =
          '<i class="bi bi-person-plus me-2"></i>Cadastrar secretaria';
      }
    });

  } else {
    areaCadastro.hidden = true;

    if (cabecalhoAcoes) {
      cabecalhoAcoes.hidden = true;
    }
  }

  botaoAtualizar.addEventListener("click", carregarUsuarios);

  await carregarUsuarios();
}

iniciarUsuarios();

