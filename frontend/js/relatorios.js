(async function() {
  if (!await window.eduAlertaPaginaPronta) return;

  const $ = id => document.getElementById(id);

  const seletor = $("alunoRelatorio");
  const botaoGerar = $("gerarRelatorio");
  const botaoPdf = $("baixarPdf");
  const mensagem = $("mensagemRelatorio");

  let relatorioAtual = null;

  function escapar(valor) {
    return String(valor ?? "—").replace(/[&<>"']/g, caractere => ({
      "&": "&amp;",
      "<": "&lt;",
      ">": "&gt;",
      '"': "&quot;",
      "'": "&#39;"
    })[caractere]);
  }

  function data(valor, incluirHora = false) {
    if (!valor) return "—";

    const d = new Date(valor);

    if (Number.isNaN(d.getTime())) {
      return String(valor);
    }

    return new Intl.DateTimeFormat("pt-BR", incluirHora
      ? {
        dateStyle: "short",
        timeStyle: "short"
      }
      : {
        dateStyle: "short",
        timeZone: "UTC"
      }).format(d);
  }

  function percentual(valor) {
    const n = Number(valor ?? 0);

    return `${n.toFixed(2).replace(".", ",")}%`;
  }

  function vazio(texto) {
    return `<p class="report-empty mb-0">${escapar(texto)}</p>`;
  }

  function tabela(colunas, linhas) {
    if (!linhas.length) {
      return vazio(
        "Não há registros disponíveis para esta seção."
      );
    }

    return `
      <div class="table-responsive">
        <table class="table table-sm table-striped table-bordered report-table mb-0">

          <thead>
            <tr>
              ${colunas.map(c => `<th>${escapar(c)}</th>`).join("")}
            </tr>
          </thead>

          <tbody>
            ${linhas.map(l => `
              <tr>
                ${l.map(v => `<td>${escapar(v)}</td>`).join("")}
              </tr>
            `).join("")}
          </tbody>

        </table>
      </div>
    `;
  }

  function mostrarErro(erro) {
    mensagem.replaceChildren();

    const alerta = document.createElement("div");

    alerta.className = "alert alert-danger";

    alerta.textContent =
      erro?.message ||
      "Não foi possível gerar o relatório.";

    mensagem.appendChild(alerta);
  }

  async function carregarAlunos() {
    try {
      const alunos = await requisicaoAPI("/alunos");

      seletor.replaceChildren(
        new Option("Selecione um aluno", "")
      );

      alunos.sort((a, b) =>
        String(a.nome).localeCompare(b.nome, "pt-BR")
      );

      alunos.forEach(aluno => {
        seletor.add(new Option(
          `${aluno.nome}${aluno.ativo ? "" : " (inativo)"}`,
          aluno.id
        ));
      });

      botaoGerar.disabled = false;

    } catch (erro) {
      seletor.replaceChildren(
        new Option("Não foi possível carregar os alunos", "")
      );

      mostrarErro(erro);
    }
  }

  function renderizar(relatorio) {
    const { aluno, resumo } = relatorio;

    $("visualizacaoRelatorio").hidden = false;
    $("acoesRelatorio").hidden = false;

    $("nomeAlunoRelatorio").textContent = aluno.nome;

    $("situacaoAlunoRelatorio").textContent =
      aluno.ativo
        ? "Cadastro ativo"
        : "Cadastro inativo";

    $("geradoEmRelatorio").textContent =
      `Gerado em ${data(relatorio.geradoEm, true)}`;

    $("dataGeracao").textContent =
      `Relatório de ${aluno.nome} · ${data(relatorio.geradoEm, true)}`;

    // Indicadores gerais

    const metricas = [
      ["Matrículas", resumo.quantidadeMatriculas],
      ["Mudanças de turma", resumo.quantidadeMudancasTurma],
      ["Fechamentos", resumo.quantidadeFechamentos],
      ["Aulas realizadas", resumo.aulasRealizadas],
      ["Faltas não justificadas", resumo.faltasNaoJustificadas],
      ["Faltas justificadas", resumo.faltasJustificadas],
      ["Percentual de faltas", percentual(resumo.percentualFaltasNaoJustificadas)],
      ["Alertas", resumo.quantidadeAlertas],
      ["Intervenções", resumo.quantidadeIntervencoes],
      ["Encaminhamentos", resumo.quantidadeEncaminhamentos],
      ["E-mails com envio concluído", resumo.notificacoesEnviadas],
      ["E-mails com falha", resumo.notificacoesComFalha]
    ];

    $("metricasRelatorio").innerHTML =
      metricas.map(([rotulo, valor]) => `
        <div class="col-6 col-md-4 col-xl-3">
          <div class="report-metric">
            <span class="report-muted">${escapar(rotulo)}</span>
            <strong>${escapar(valor)}</strong>
          </div>
        </div>
      `).join("");

    $("sinteseRelatorio").textContent =
      `Foram identificadas ${resumo.quantidadeMatriculas} matrícula(s), ` +
      `${resumo.quantidadeMudancasTurma} mudança(s) de turma e ` +
      `${resumo.quantidadeAlertas} alerta(s). ` +
      `A frequência consolidada considera ${resumo.aulasRealizadas} ` +
      `aula(s) registrada(s) em fechamentos concluídos.`;

    // Histórico de matrículas

    $("matriculasRelatorio").innerHTML =
      relatorio.matriculas.length
        ? relatorio.matriculas.map((m, i) => `
            <div class="report-item">

              <p>
                <strong>
                  Matrícula ${i + 1} · ${escapar(m.turma)}
                </strong>
                (${escapar(m.anoLetivo)})
              </p>

              <p class="report-muted">
                ${data(m.dataInicio)} até ${data(m.dataFim)}
                · ${escapar(m.status)}
                · Períodos:
                ${escapar(
          (m.periodosLetivos || []).join(", ")
          || "Sem fechamento registrado"
        )}
              </p>

              <p class="mb-0">
                ${m.aulasRealizadas} aulas
                · ${m.faltasNaoJustificadas} faltas não justificadas
                · ${m.faltasJustificadas} justificadas
                · ${percentual(m.percentualFaltasNaoJustificadas)}
                de faltas não justificadas
              </p>

            </div>
          `).join("")
        : vazio("Não há matrículas cadastradas para este aluno.");

    // Mudanças de turma

    $("transferenciasRelatorio").innerHTML =
      relatorio.mudancasTurma.length
        ? relatorio.mudancasTurma.map(m => `
            <div class="report-item">

              <p>
                <strong>
                  ${escapar(m.turmaOrigem)}
                  → ${escapar(m.turmaDestino)}
                </strong>
              </p>

              <p class="report-muted">
                Último dia na turma anterior:
                ${data(m.ultimoDiaNaTurmaAnterior)}
                · Primeiro dia na nova turma:
                ${data(m.primeiroDiaNaTurmaNova)}
              </p>

              <p>${escapar(m.observacao)}</p>

            </div>
          `).join("")
        : vazio(
          "Não foram identificadas mudanças entre turmas no histórico de matrículas."
        );

    // Fechamentos e frequência

    const fechamentos = relatorio.matriculas.flatMap(m =>
      (m.fechamentos || []).map(f => ({
        turma: m.turma,
        ...f
      }))
    );

    $("frequenciaRelatorio").innerHTML = tabela(
      [
        "Turma",
        "Período letivo",
        "Semana",
        "Aulas",
        "Faltas não justificadas",
        "Faltas justificadas",
        "% faltas"
      ],

      fechamentos.map(f => [
        f.turma,
        f.periodoLetivo,
        `${data(f.dataInicioSemana)} a ${data(f.dataFimSemana)}`,
        f.aulasRealizadas,
        f.faltasNaoJustificadas,
        f.faltasJustificadas,
        percentual(f.percentualFaltasNaoJustificadas)
      ])
    );

    // Responsáveis

    $("responsaveisRelatorio").innerHTML = tabela(
      [
        "Responsável",
        "Vínculo",
        "Principal",
        "Cadastro"
      ],

      relatorio.responsaveis.map(r => [
        r.nome,
        r.parentesco || "Não informado",
        r.principal ? "Sim" : "Não",
        r.ativo ? "Ativo" : "Inativo"
      ])
    );

    const alertas = relatorio.alertas || [];

    // Notificações

    const notificacoes = alertas.flatMap(a =>
      (a.notificacoes || []).map(n => ({
        periodo: a.periodoLetivo,
        ...n
      }))
    );

    $("notificacoesRelatorio").innerHTML = tabela(
      [
        "Período",
        "Responsável",
        "Resultado registrado",
        "Registro",
        "Envio concluído em"
      ],

      notificacoes.map(n => [
        n.periodo,
        n.responsavel,
        n.status === "ENVIADA"
          ? "Enviado pelo serviço de e-mail"
          : "Falha no envio",
        data(n.registroEm, true),
        data(n.envioConcluidoEm, true)
      ])
    );

    // Intervenções e acompanhamentos

    const acompanhamentos = alertas.flatMap(a =>
      (a.acompanhamentos || []).map(x => ({
        periodo: a.periodoLetivo,
        ...x
      }))
    );

    $("acompanhamentosRelatorio").innerHTML =
      acompanhamentos.length
        ? acompanhamentos.map(a => `
            <div class="report-item">

              <p>
                <strong>
                  ${escapar(a.tipoIntervencao)}
                  · ${escapar(a.periodo)}
                </strong>
              </p>

              <p class="report-muted">
                ${data(a.dataHora, true)}
                · Registrado por:
                ${escapar(a.registradoPor)}
              </p>

              <p>
                <strong>Descrição:</strong>
                ${escapar(a.descricao)}
              </p>

              <p>
                <strong>Resultado:</strong>
                ${escapar(a.resultado)}
              </p>

            </div>
          `).join("")
        : vazio(
          "Não há intervenções registradas nos alertas do aluno."
        );

    // Encaminhamentos institucionais

    const encaminhamentos = alertas.flatMap(a =>
      (a.encaminhamentos || []).map(x => ({
        periodo: a.periodoLetivo,
        ...x
      }))
    );

    $("encaminhamentosRelatorio").innerHTML =
      encaminhamentos.length
        ? encaminhamentos.map(e => `
            <div class="report-item">

              <p>
                <strong>
                  ${escapar(e.instituicaoDestino)}
                  · ${escapar(e.situacao)}
                </strong>
              </p>

              <p class="report-muted">
                ${data(e.dataEncaminhamento)}
                · Período relacionado:
                ${escapar(e.periodo)}
              </p>

              <p>
                <strong>Finalidade:</strong>
                ${escapar(e.finalidade)}
              </p>

              <p>
                <strong>Descrição/observação:</strong>
                ${escapar(e.descricao)}
              </p>

            </div>
          `).join("")
        : vazio(
          "Não há encaminhamentos institucionais registrados para o aluno."
        );

    // Alertas

    $("alertasRelatorio").innerHTML =
      alertas.length
        ? alertas.map(a => `
            <div class="report-item">

              <p>
                <strong>
                  ${escapar(a.nivel)}
                  · ${escapar(a.situacao)}
                  · ${escapar(a.periodoLetivo)}
                </strong>
              </p>

              <p class="report-muted">
                ${percentual(a.percentualFaltas)}
                · Atualizado em
                ${data(a.atualizadoEm, true)}
              </p>

              <p>${escapar(a.motivo)}</p>

            </div>
          `).join("")
        : vazio("Não há alertas registrados para este aluno.");
  }

  // Geração do relatório pela API

  async function gerar() {

    if (!seletor.value) {
      mostrarErro(
        new Error("Selecione um aluno para gerar o relatório.")
      );

      return;
    }

    botaoGerar.disabled = true;

    botaoGerar.innerHTML =
      '<span class="spinner-border spinner-border-sm me-2"></span>Consolidando...';

    mensagem.replaceChildren();

    try {

      relatorioAtual = await requisicaoAPI(
        `/relatorios/alunos/${encodeURIComponent(seletor.value)}`
      );

      renderizar(relatorioAtual);

    } catch (erro) {

      relatorioAtual = null;

      $("visualizacaoRelatorio").hidden = true;
      $("acoesRelatorio").hidden = true;

      mostrarErro(erro);

    } finally {

      botaoGerar.disabled = false;

      botaoGerar.innerHTML =
        '<i class="bi bi-search me-1"></i>Gerar relatório';
    }
  }

  // Exportação do relatório usando a impressão nativa do navegador.

  function baixarPdf() {
    if (!relatorioAtual) return;

    const nome = String(relatorioAtual.aluno.nome || "aluno")
      .normalize("NFD")
      .replace(/[\u0300-\u036f]/g, "")
      .replace(/[^a-zA-Z0-9_-]+/g, "-")
      .replace(/^-|-$/g, "")
      .toLowerCase();

    const tituloOriginal = document.title;

    document.title = `relatorio-edualerta-${nome || "aluno"}`;

    window.print();

    document.title = tituloOriginal;
  }

  // Eventos

  botaoGerar.addEventListener("click", gerar);

  botaoPdf.addEventListener("click", baixarPdf);

  await carregarAlunos();

})();

