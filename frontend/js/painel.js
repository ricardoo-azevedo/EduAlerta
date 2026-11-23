(async function () {
  if (!await window.eduAlertaPaginaPronta) return;

  const $ = id => document.getElementById(id);
  const aviso = $("mensagemPainel");
  const atualizar = $("atualizarPainel");
  const lista = $("alertasRecentes");

  function celula(linha, texto) {
    const td = document.createElement("td");
    td.textContent = String(texto ?? "—");
    linha.appendChild(td);
  }

  function textoVazio(texto) {
    lista.replaceChildren();
    const linha = document.createElement("tr");
    const td = document.createElement("td");
    td.colSpan = 4;
    td.className = "text-center text-secondary py-4";
    td.textContent = texto;
    linha.appendChild(td);
    lista.appendChild(linha);
  }

  async function carregarPainel() {
    atualizar.disabled = true;
    aviso.replaceChildren();
    ["totalAlunos", "totalMatriculas", "totalFechamentos", "totalAlertas"].forEach(id => {
      $(id).textContent = "—";
    });
    $("descricaoAlertas").textContent = "Preventivos e críticos";
    textoVazio("Consultando dados da API...");

    try {
      const [alunos, matriculas, fechamentos, alertas] = await Promise.all([
        requisicaoAPI("/alunos"),
        requisicaoAPI("/matriculas"),
        requisicaoAPI("/fechamentos-semanais"),
        requisicaoAPI("/alertas")
      ]);

      if (![alunos, matriculas, fechamentos, alertas].every(Array.isArray)) {
        throw new Error("A API retornou dados em formato inesperado.");
      }

      const ativos = alertas.filter(item => item.situacao === "ATIVO");
      const criticos = ativos.filter(item => item.nivel === "CRITICO");
      $("totalAlunos").textContent = alunos.filter(item => item.ativo === true).length;
      $("totalMatriculas").textContent = matriculas.filter(item => item.status === "ATIVA").length;
      $("totalFechamentos").textContent = fechamentos.filter(item => item.status === "FECHADO").length;
      $("totalAlertas").textContent = ativos.length;
      $("descricaoAlertas").textContent = `${criticos.length} crítico(s) entre os ativos`;

      lista.replaceChildren();
      ativos.sort((a, b) => String(b.atualizadoEm || "").localeCompare(String(a.atualizadoEm || "")));
      if (ativos.length === 0) {
        textoVazio("Não existem alertas ativos neste momento.");
        return;
      }
      ativos.slice(0, 5).forEach(alerta => {
        const linha = document.createElement("tr");
        celula(linha, alerta.alunoNome);
        celula(linha, alerta.periodoLetivoNome);
        celula(linha, alerta.nivel === "CRITICO" ? "Crítico" : "Preventivo");
        const valor = Number(alerta.percentualFaltas);
        celula(linha, Number.isFinite(valor) ? `${valor.toFixed(2).replace(".", ",")}%` : "—");
        lista.appendChild(linha);
      });
    } catch (erro) {
      textoVazio("Não foi possível consultar os indicadores.");
      const mensagem = document.createElement("div");
      mensagem.className = "alert alert-danger";
      mensagem.textContent = erro.message || "Erro ao carregar o painel.";
      aviso.appendChild(mensagem);
    } finally {
      atualizar.disabled = false;
    }
  }

  atualizar.addEventListener("click", carregarPainel);
  await carregarPainel();
})();