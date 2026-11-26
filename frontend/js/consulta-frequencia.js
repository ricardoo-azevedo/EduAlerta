(async function () {
  if (!await window.eduAlertaPaginaPronta) return;

  const $ = id => document.getElementById(id);
  const alunoSelect = $("alunoConsulta");
  const tabela = $("historicoConsulta");
  const mensagem = $("mensagemConsulta");
  const botaoConsultar = $("consultarHistorico");

  function avisar(texto, sucesso = false) {
    mensagem.replaceChildren();
    if (!texto) return;
    const div = document.createElement("div");
    div.className = `alert alert-${sucesso ? "success" : "danger"}`;
    div.textContent = texto;
    mensagem.appendChild(div);
  }

  function celula(linha, texto) {
    const td = document.createElement("td");
    td.textContent = String(texto ?? "—");
    linha.appendChild(td);
    return td;
  }

  function dataBR(valor) {
    if (!valor) return "—";
    return String(valor).split("-").reverse().join("/");
  }

  function vazio(texto) {
    tabela.replaceChildren();
    const linha = document.createElement("tr");
    const td = celula(linha, texto);
    td.colSpan = 8;
    td.className = "text-center text-secondary py-4";
    tabela.appendChild(linha);
  }

  async function carregarAlunos() {
    const selecionado = alunoSelect.value;
    try {
      const alunos = await requisicaoAPI("/alunos");
      if (!Array.isArray(alunos)) throw new Error("Resposta inesperada da API de alunos.");
      alunoSelect.replaceChildren(new Option("Selecione um aluno", ""));
      alunos.sort((a, b) => a.nome.localeCompare(b.nome, "pt-BR"));
      alunos.forEach(aluno => alunoSelect.add(new Option(aluno.nome, aluno.id)));
      if (Array.from(alunoSelect.options).some(opcao => opcao.value === selecionado)) {
        alunoSelect.value = selecionado;
      }
    } catch (erro) {
      avisar(erro.message || "Não foi possível carregar os alunos.");
    }
  }

  async function consultarAcumulado(matricula, campos, botao) {
    botao.disabled = true;
    avisar("");
    try {
      const resultado = await requisicaoAPI(`/matriculas/${matricula.id}/frequencia-acumulada`);
      campos.faltas.textContent = String(resultado.faltasNaoJustificadas ?? "—");
      campos.aulas.textContent = String(resultado.aulasRealizadas ?? "—");
      const percentual = Number(resultado.percentualFaltas);
      campos.percentual.textContent = Number.isFinite(percentual)
        ? `${percentual.toFixed(2).replace(".", ",")}%`
        : "—";
    } catch (erro) {
      avisar(erro.message || "Não foi possível consultar a frequência acumulada.");
    } finally {
      botao.disabled = false;
    }
  }

  function renderizar(matriculas) {
    tabela.replaceChildren();
    if (matriculas.length === 0) {
      vazio("Este aluno não possui matrículas registradas.");
      return;
    }
    matriculas.forEach(matricula => {
      const linha = document.createElement("tr");
      celula(linha, matricula.turmaNome);
      celula(linha, dataBR(matricula.dataInicio));
      celula(linha, dataBR(matricula.dataFim));
      celula(linha, matricula.status);
      const faltas = celula(linha, "—");
      const aulas = celula(linha, "—");
      const percentual = celula(linha, "—");
      const acao = celula(linha, "");
      const botao = document.createElement("button");
      botao.type = "button";
      botao.className = "btn btn-outline-primary btn-sm";
      botao.textContent = "Ver acumulado";
      botao.addEventListener("click", () => consultarAcumulado(matricula, { faltas, aulas, percentual }, botao));
      acao.appendChild(botao);
      tabela.appendChild(linha);
    });
  }

  async function carregarHistorico() {
    const id = alunoSelect.value;
    avisar("");
    if (!id) {
      vazio("Selecione um aluno para consultar o histórico.");
      return;
    }
    botaoConsultar.disabled = true;
    vazio("Consultando histórico...");
    try {
      const matriculas = await requisicaoAPI(`/matriculas/aluno/${encodeURIComponent(id)}`);
      if (!Array.isArray(matriculas)) throw new Error("Resposta inesperada da API de matrículas.");
      renderizar(matriculas);
    } catch (erro) {
      vazio("Não foi possível carregar o histórico.");
      avisar(erro.message || "Erro ao consultar matrículas.");
    } finally {
      botaoConsultar.disabled = false;
    }
  }

  botaoConsultar.addEventListener("click", carregarHistorico);
  alunoSelect.addEventListener("change", carregarHistorico);
  $("atualizarAlunosConsulta").addEventListener("click", async () => {
    avisar("");
    await carregarAlunos();
    await carregarHistorico();
  });
  await carregarAlunos();
})();