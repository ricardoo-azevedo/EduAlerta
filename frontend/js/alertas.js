(async function () {
  if (!await window.eduAlertaPaginaPronta) return;

  const $ = id => document.getElementById(id);
  const formulario = $("formFiltrosAlertas");
  const lista = $("listaAlertas");
  const contador = $("contadorAlertas");
  const mensagem = $("mensagemAlertas");
  const botaoBuscar = $("buscarAlertas");
  const botaoAtualizar = $("atualizarAlertas");
  const filtroAluno = $("filtroAlunoAlerta");
  const filtroPeriodo = $("filtroPeriodoAlerta");
  const filtroNivel = $("filtroNivelAlerta");
  const filtroSituacao = $("filtroSituacaoAlerta");

  function avisar(texto, sucesso = false) {
    mensagem.replaceChildren();
    if (!texto) return;
    const aviso = document.createElement("div");
    aviso.className = `alert alert-${sucesso ? "success" : "danger"} mb-4`;
    aviso.textContent = texto;
    mensagem.appendChild(aviso);
  }

  function preencher(select, registros, legenda, texto) {
    const atual = select.value;
    select.replaceChildren(new Option(legenda, ""));
    registros.forEach(item => select.add(new Option(texto(item), item.id)));
    if (Array.from(select.options).some(opcao => opcao.value === atual)) select.value = atual;
  }

  function adicionarCelula(linha, valor) {
    const celula = document.createElement("td");
    celula.textContent = valor == null ? "—" : String(valor);
    linha.appendChild(celula);
    return celula;
  }

  function dataHora(valor) {
    if (!valor) return "—";
    const partes = String(valor).split("T");
    const data = partes[0].split("-").reverse().join("/");
    return partes[1] ? `${data} ${partes[1].slice(0, 5)}` : data;
  }

  function percentual(valor) {
    const numero = Number(valor);
    return Number.isFinite(numero) ? `${numero.toFixed(2).replace(".", ",")}%` : "—";
  }

  function vazio(texto) {
    lista.replaceChildren();
    const linha = document.createElement("tr");
    const celula = adicionarCelula(linha, texto);
    celula.colSpan = 7;
    celula.className = "text-center text-secondary py-4";
    lista.appendChild(linha);
  }

  async function resolverAlerta(alerta, botao) {
    if (!confirm(`Resolver manualmente o alerta de ${alerta.alunoNome}?`)) return;
    botao.disabled = true;
    avisar("");
    try {
      await requisicaoAPI(`/alertas/${alerta.id}/resolver`, { method: "PUT" });
      await carregarAlertas();
      avisar("Alerta resolvido manualmente com sucesso.", true);
    } catch (erro) {
      avisar(erro.message || "Não foi possível resolver o alerta.");
    } finally {
      botao.disabled = false;
    }
  }

  function renderizar(alertas) {
    lista.replaceChildren();
    contador.textContent = `${alertas.length} alerta(s)`;
    if (alertas.length === 0) {
      vazio("Nenhum alerta encontrado para os filtros informados.");
      return;
    }
    alertas.forEach(alerta => {
      const linha = document.createElement("tr");
      adicionarCelula(linha, alerta.alunoNome);
      adicionarCelula(linha, alerta.periodoLetivoNome);
      adicionarCelula(linha, percentual(alerta.percentualFaltas));
      const celulaNivel = adicionarCelula(linha, "");
      const nivel = document.createElement("span");
      nivel.className = `badge ${alerta.nivel === "CRITICO" ? "bg-danger" : "bg-warning text-dark"}`;
      nivel.textContent = alerta.nivel === "CRITICO" ? "Crítico" : "Preventivo";
      celulaNivel.appendChild(nivel);
      const celulaSituacao = adicionarCelula(linha, "");
      const situacao = document.createElement("span");
      situacao.className = `badge ${alerta.situacao === "ATIVO" ? "bg-primary" : "bg-secondary"}`;
      situacao.textContent = alerta.situacao === "ATIVO" ? "Ativo" : "Resolvido";
      celulaSituacao.appendChild(situacao);
      adicionarCelula(linha, dataHora(alerta.atualizadoEm));
      const acoes = adicionarCelula(linha, "");
      if (alerta.situacao === "ATIVO") {
        const botao = document.createElement("button");
        botao.type = "button";
        botao.className = "btn btn-outline-primary btn-sm";
        botao.textContent = "Resolver";
        botao.addEventListener("click", () => resolverAlerta(alerta, botao));
        acoes.appendChild(botao);
      } else {
        acoes.textContent = "—";
      }
      if (alerta.motivo) {
        const motivo = document.createElement("tr");
        const celula = adicionarCelula(motivo, alerta.motivo);
        celula.colSpan = 7;
        celula.className = "small text-secondary pt-0";
        lista.append(linha, motivo);
      } else {
        lista.appendChild(linha);
      }
    });
  }

  async function carregarAlertas() {
    const parametros = new URLSearchParams();
    if (filtroAluno.value) parametros.set("alunoId", filtroAluno.value);
    if (filtroPeriodo.value) parametros.set("periodoLetivoId", filtroPeriodo.value);
    if (filtroNivel.value) parametros.set("nivel", filtroNivel.value);
    if (filtroSituacao.value) parametros.set("situacao", filtroSituacao.value);
    const caminho = `/alertas${parametros.size ? `?${parametros}` : ""}`;
    botaoBuscar.disabled = true;
    botaoAtualizar.disabled = true;
    contador.textContent = "Carregando...";
    vazio("Consultando alertas...");
    try {
      const alertas = await requisicaoAPI(caminho);
      if (!Array.isArray(alertas)) throw new Error("Resposta inesperada da API de alertas.");
      renderizar(alertas);
    } catch (erro) {
      contador.textContent = "Não foi possível consultar";
      vazio("Não foi possível carregar os alertas.");
      avisar(erro.message || "Falha ao consultar alertas.");
    } finally {
      botaoBuscar.disabled = false;
      botaoAtualizar.disabled = false;
    }
  }

  async function carregarFiltros() {
    const resultados = await Promise.allSettled([
      requisicaoAPI("/alunos"),
      requisicaoAPI("/periodos-letivos")
    ]);
    if (resultados[0].status === "fulfilled" && Array.isArray(resultados[0].value)) {
      preencher(filtroAluno, resultados[0].value, "Todos os alunos", item => item.nome);
    }
    if (resultados[1].status === "fulfilled" && Array.isArray(resultados[1].value)) {
      preencher(filtroPeriodo, resultados[1].value, "Todos os períodos", item => item.nome);
    }
    if (resultados.some(resultado => resultado.status === "rejected")) {
      avisar("Não foi possível atualizar todas as opções de filtro. A consulta de alertas continua disponível.");
    }
  }

  formulario.addEventListener("submit", evento => {
    evento.preventDefault();
    avisar("");
    carregarAlertas();
  });
  $("limparFiltrosAlertas").addEventListener("click", () => {
    formulario.reset();
    avisar("");
    carregarAlertas();
  });
  botaoAtualizar.addEventListener("click", async () => {
    avisar("");
    await carregarFiltros();
    await carregarAlertas();
  });

  await carregarFiltros();
  await carregarAlertas();
})();