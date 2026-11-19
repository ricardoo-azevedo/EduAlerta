
const elementosFrequencia = {
    mensagem: document.getElementById("mensagemFrequencia"),
    formFechamento: document.getElementById("formFechamento"),
    turma: document.getElementById("turmaFechamento"),
    periodo: document.getElementById("periodoFechamento"),
    inicioSemana: document.getElementById("inicioSemana"),
    filtroTurma: document.getElementById("filtroTurma"),
    filtroPeriodo: document.getElementById("filtroPeriodo"),
    atualizar: document.getElementById("atualizarFechamentos"),
    lista: document.getElementById("listaFechamentos"),
    detalhes: document.getElementById("detalhesFechamento"),
    titulo: document.getElementById("tituloFechamento"),
    resumo: document.getElementById("resumoFechamento"),
    status: document.getElementById("statusFechamento"),
    fechar: document.getElementById("fecharSemana"),
    formDia: document.getElementById("formDia"),
    dataDia: document.getElementById("dataDia"),
    quantidadeAulas: document.getElementById("quantidadeAulas"),
    salvarDia: document.getElementById("salvarDia"),
    cancelarDia: document.getElementById("cancelarEdicaoDia"),
    listaDias: document.getElementById("listaDias"),
    formFrequencia: document.getElementById("formFrequencia"),
    matricula: document.getElementById("matriculaFrequencia"),
    faltas: document.getElementById("faltas"),
    faltasJustificadas: document.getElementById("faltasJustificadas"),
    salvarFrequencia: document.getElementById("salvarFrequencia"),
    cancelarFrequencia: document.getElementById("cancelarEdicaoFrequencia"),
    listaFrequencias: document.getElementById("listaFrequencias")
};

let turmasFrequencia = [];
let periodosFrequencia = [];
let matriculasFrequencia = [];
let fechamentosFrequencia = [];
let fechamentoSelecionado = null;
let frequenciasSelecionadas = [];
let diaEmEdicao = null;
let frequenciaEmEdicao = null;

function mensagemFrequencia(texto, tipo = "success") {
    const elemento = elementosFrequencia.mensagem;
    elemento.replaceChildren();

    if (!texto) {
        return;
    }

    const alerta = document.createElement("div");
    alerta.className = `alert alert-${tipo}`;
    alerta.setAttribute("role", "alert");
    alerta.textContent = texto;
    elemento.appendChild(alerta);
}

function formatarDataFrequencia(data) {
    if (!data) {
        return "-";
    }

    const partes = data.split("-");

    if (partes.length !== 3) {
        return data;
    }

    return `${partes[2]}/${partes[1]}/${partes[0]}`;
}

function criarElemento(tag, texto, classe = "") {
    const elemento = document.createElement(tag);
    elemento.textContent = String(texto ?? "");
    if (classe) {
        elemento.className = classe;
    }
    return elemento;
}

function adicionarCelula(linha, texto) {
    const celula = criarElemento("td", texto);
    linha.appendChild(celula);
    return celula;
}

function adicionarBotao(container, texto, classe, acao) {
    const botao = criarElemento("button", texto, classe);
    botao.type = "button";
    botao.addEventListener("click", acao);
    container.appendChild(botao);
    return botao;
}

function preencherSelect(select, registros, textoInicial, obterTexto) {
    select.replaceChildren();
    select.appendChild(new Option(textoInicial, ""));

    registros.forEach(registro => {
        select.appendChild(new Option(obterTexto(registro), registro.id));
    });
}

function semanaValida(data) {
    if (!data) {
        return false;
    }

    const dia = new Date(`${data}T12:00:00`);
    return !Number.isNaN(dia.getTime()) && dia.getDay() === 1;
}

function matriculaNaSemana(matricula, fechamento) {
    if (String(matricula.turmaId) !== String(fechamento.turmaId)) {
        return false;
    }

    return matricula.dataInicio <= fechamento.dataFimSemana &&
        (!matricula.dataFim || matricula.dataFim >= fechamento.dataInicioSemana);
}

function totalAulasSemana() {
    return (fechamentoSelecionado?.dias || []).reduce(
        (total, dia) => total + Number(dia.quantidadeAulas || 0),
        0
    );
}

function matriculasDaSemana() {
    if (!fechamentoSelecionado) {
        return [];
    }

    return matriculasFrequencia.filter(matricula =>
        matriculaNaSemana(matricula, fechamentoSelecionado)
    );
}

function preencherCadastros() {
    const turmasAtivas = turmasFrequencia.filter(turma => turma.ativo);
    const periodosAtivos = periodosFrequencia.filter(periodo => periodo.ativo);

    preencherSelect(
        elementosFrequencia.turma,
        turmasAtivas,
        "Selecione a turma",
        turma => turma.nome
    );

    preencherSelect(
        elementosFrequencia.periodo,
        periodosAtivos,
        "Selecione o período",
        periodo => `${periodo.nome} - ${periodo.anoLetivo || periodo.ano || ""}`
    );

    preencherSelect(
        elementosFrequencia.filtroTurma,
        turmasFrequencia,
        "Todas as turmas",
        turma => turma.nome
    );

    preencherSelect(
        elementosFrequencia.filtroPeriodo,
        periodosFrequencia,
        "Todos os períodos",
        periodo => periodo.nome
    );
}

async function carregarDadosFrequencia() {
    const [turmas, periodos, matriculas] = await Promise.all([
        requisicaoAPI("/turmas"),
        requisicaoAPI("/periodos-letivos"),
        requisicaoAPI("/matriculas")
    ]);

    turmasFrequencia = turmas;
    periodosFrequencia = periodos;
    matriculasFrequencia = matriculas;

    preencherCadastros();
}

async function carregarFechamentos() {
    elementosFrequencia.lista.replaceChildren();

    const parametros = new URLSearchParams();

    if (elementosFrequencia.filtroTurma.value) {
        parametros.set("turmaId", elementosFrequencia.filtroTurma.value);
    }

    if (elementosFrequencia.filtroPeriodo.value) {
        parametros.set("periodoLetivoId", elementosFrequencia.filtroPeriodo.value);
    }

    const caminho = `/fechamentos-semanais${parametros.size ? `?${parametros}` : ""}`;

    fechamentosFrequencia = await requisicaoAPI(caminho);

    if (!fechamentosFrequencia.length) {
        const linha = document.createElement("tr");
        const celula = adicionarCelula(linha, "Nenhum fechamento encontrado.");
        celula.colSpan = 5;
        elementosFrequencia.lista.appendChild(linha);
        return;
    }

    fechamentosFrequencia.forEach(fechamento => {
        const linha = document.createElement("tr");

        adicionarCelula(linha, fechamento.turmaNome);
        adicionarCelula(linha, fechamento.periodoLetivoNome);
        adicionarCelula(
            linha,
            `${formatarDataFrequencia(fechamento.dataInicioSemana)} a ${formatarDataFrequencia(fechamento.dataFimSemana)}`
        );

        const celulaStatus = document.createElement("td");
        const classe = fechamento.status === "FECHADO"
            ? "badge text-bg-success"
            : "badge text-bg-secondary";

        celulaStatus.appendChild(
            criarElemento("span", fechamento.status, classe)
        );
        linha.appendChild(celulaStatus);

        const acoes = document.createElement("td");

        adicionarBotao(
            acoes,
            "Gerenciar",
            "btn btn-outline-primary btn-sm",
            () => executarOperacao(() => selecionarFechamento(fechamento.id))
        );

        linha.appendChild(acoes);
        elementosFrequencia.lista.appendChild(linha);
    });
}

async function executarOperacao(operacao, sucesso = "") {
    try {
        mensagemFrequencia("");
        await operacao();

        if (sucesso) {
            mensagemFrequencia(sucesso);
        }
    } catch (erro) {
        mensagemFrequencia(
            erro.message || "Não foi possível concluir a operação.",
            "danger"
        );
    }
}

async function criarFechamento(evento) {
    evento.preventDefault();

    const data = elementosFrequencia.inicioSemana.value;

    if (!semanaValida(data)) {
        mensagemFrequencia("A data inicial da semana deve ser uma segunda-feira.", "warning");
        return;
    }

    await executarOperacao(async () => {
        const fechamento = await requisicaoAPI("/fechamentos-semanais", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({
                turmaId: Number(elementosFrequencia.turma.value),
                periodoLetivoId: Number(elementosFrequencia.periodo.value),
                dataInicioSemana: data
            })
        });

        elementosFrequencia.formFechamento.reset();
        await carregarFechamentos();
        await selecionarFechamento(fechamento.id);
    }, "Fechamento semanal criado com sucesso.");
}

async function selecionarFechamento(id) {
    const [fechamento, frequencias] = await Promise.all([
        requisicaoAPI(`/fechamentos-semanais/${id}`),
        requisicaoAPI(`/fechamentos-semanais/${id}/frequencias`)
    ]);

    fechamentoSelecionado = fechamento;
    frequenciasSelecionadas = frequencias;

    cancelarEdicaoDia();
    cancelarEdicaoFrequencia();
    renderizarDetalhes();

    elementosFrequencia.detalhes.hidden = false;
}

function renderizarDetalhes() {
    const fechamento = fechamentoSelecionado;
    const editavel = fechamento.status === "RASCUNHO";

    elementosFrequencia.titulo.textContent =
        `Turma: ${fechamento.turmaNome}`;

    elementosFrequencia.resumo.textContent =
        `${fechamento.periodoLetivoNome} | ${formatarDataFrequencia(fechamento.dataInicioSemana)} a ${formatarDataFrequencia(fechamento.dataFimSemana)} | ${totalAulasSemana()} aulas registradas`;

    elementosFrequencia.status.textContent = fechamento.status;
    elementosFrequencia.status.className = editavel
        ? "badge text-bg-secondary"
        : "badge text-bg-success";

    elementosFrequencia.fechar.hidden = !editavel;

    elementosFrequencia.formDia.hidden = !editavel;
    elementosFrequencia.formFrequencia.hidden = !editavel;

    renderizarDias();
    renderizarMatriculas();
    renderizarFrequencias();
}

function renderizarDias() {
    elementosFrequencia.listaDias.replaceChildren();

    const dias = [...(fechamentoSelecionado.dias || [])]
        .sort((a, b) => a.data.localeCompare(b.data));

    if (!dias.length) {
        const linha = document.createElement("tr");
        const celula = adicionarCelula(linha, "Nenhum dia registrado.");
        celula.colSpan = 3;
        elementosFrequencia.listaDias.appendChild(linha);
        return;
    }

    dias.forEach(dia => {
        const linha = document.createElement("tr");

        adicionarCelula(linha, formatarDataFrequencia(dia.data));
        adicionarCelula(linha, dia.quantidadeAulas);

        const acoes = document.createElement("td");

        if (fechamentoSelecionado.status === "RASCUNHO") {
            adicionarBotao(
                acoes,
                "Editar",
                "btn btn-outline-primary btn-sm me-2",
                () => editarDia(dia)
            );

            adicionarBotao(
                acoes,
                "Excluir",
                "btn btn-outline-danger btn-sm",
                () => excluirDia(dia.id)
            );
        } else {
            acoes.textContent = "Concluído";
        }

        linha.appendChild(acoes);
        elementosFrequencia.listaDias.appendChild(linha);
    });
}

function editarDia(dia) {
    diaEmEdicao = dia.id;
    elementosFrequencia.dataDia.value = dia.data;
    elementosFrequencia.quantidadeAulas.value = dia.quantidadeAulas;
    elementosFrequencia.salvarDia.textContent = "Atualizar dia";
    elementosFrequencia.cancelarDia.hidden = false;
}

function cancelarEdicaoDia() {
    diaEmEdicao = null;
    elementosFrequencia.formDia.reset();
    elementosFrequencia.salvarDia.textContent = "Salvar dia";
    elementosFrequencia.cancelarDia.hidden = true;
}

async function salvarDia(evento) {
    evento.preventDefault();

    const data = elementosFrequencia.dataDia.value;
    const quantidadeAulas = Number(elementosFrequencia.quantidadeAulas.value);

    if (
        data < fechamentoSelecionado.dataInicioSemana ||
        data > fechamentoSelecionado.dataFimSemana
    ) {
        mensagemFrequencia("A data deve pertencer à semana selecionada.", "warning");
        return;
    }

    if (!Number.isInteger(quantidadeAulas) || quantidadeAulas < 0) {
        mensagemFrequencia("Informe uma quantidade válida de aulas.", "warning");
        return;
    }

    await executarOperacao(async () => {
        const id = fechamentoSelecionado.id;
        const editando = diaEmEdicao !== null;

        const caminho = editando
            ? `/fechamentos-semanais/${id}/dias/${diaEmEdicao}`
            : `/fechamentos-semanais/${id}/dias`;

        await requisicaoAPI(caminho, {
            method: editando ? "PUT" : "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ data, quantidadeAulas })
        });

        await selecionarFechamento(id);
    }, "Registro de aulas salvo com sucesso.");
}

async function excluirDia(id) {
    if (!confirm("Deseja excluir este registro de aulas?")) {
        return;
    }

    await executarOperacao(async () => {
        const fechamentoId = fechamentoSelecionado.id;

        await requisicaoAPI(
            `/fechamentos-semanais/${fechamentoId}/dias/${id}`,
            { method: "DELETE" }
        );

        await selecionarFechamento(fechamentoId);
    }, "Registro excluído com sucesso.");
}

function renderizarMatriculas() {
    const matriculas = matriculasDaSemana();

    preencherSelect(
        elementosFrequencia.matricula,
        matriculas,
        "Selecione a matrícula",
        matricula => `${matricula.alunoNome} - Matrícula ${matricula.id}`
    );
}

function renderizarFrequencias() {
    elementosFrequencia.listaFrequencias.replaceChildren();

    if (!frequenciasSelecionadas.length) {
        const linha = document.createElement("tr");
        const celula = adicionarCelula(linha, "Nenhuma frequência registrada.");
        celula.colSpan = 4;
        elementosFrequencia.listaFrequencias.appendChild(linha);
        return;
    }

    frequenciasSelecionadas.forEach(frequencia => {
        const linha = document.createElement("tr");

        adicionarCelula(linha, frequencia.alunoNome);
        adicionarCelula(linha, frequencia.faltas);
        adicionarCelula(linha, frequencia.faltasJustificadas);

        const acoes = document.createElement("td");

        if (fechamentoSelecionado.status === "RASCUNHO") {
            adicionarBotao(
                acoes,
                "Editar",
                "btn btn-outline-primary btn-sm me-2",
                () => editarFrequencia(frequencia)
            );

            adicionarBotao(
                acoes,
                "Excluir",
                "btn btn-outline-danger btn-sm",
                () => excluirFrequencia(frequencia.id)
            );
        } else {
            acoes.textContent = "Concluído";
        }

        linha.appendChild(acoes);
        elementosFrequencia.listaFrequencias.appendChild(linha);
    });
}

function editarFrequencia(frequencia) {
    frequenciaEmEdicao = frequencia.id;
    elementosFrequencia.matricula.value = String(frequencia.matriculaId);
    elementosFrequencia.matricula.disabled = true;
    elementosFrequencia.faltas.value = frequencia.faltas;
    elementosFrequencia.faltasJustificadas.value = frequencia.faltasJustificadas;
    elementosFrequencia.salvarFrequencia.textContent = "Atualizar";
    elementosFrequencia.cancelarFrequencia.hidden = false;
}

function cancelarEdicaoFrequencia() {
    frequenciaEmEdicao = null;
    elementosFrequencia.formFrequencia.reset();
    elementosFrequencia.matricula.disabled = false;
    elementosFrequencia.salvarFrequencia.textContent = "Salvar";
    elementosFrequencia.cancelarFrequencia.hidden = true;
}

async function salvarFrequencia(evento) {
    evento.preventDefault();

    const matriculaId = Number(elementosFrequencia.matricula.value);
    const faltas = Number(elementosFrequencia.faltas.value);
    const faltasJustificadas = Number(elementosFrequencia.faltasJustificadas.value);

    if (
        !matriculaId ||
        !Number.isInteger(faltas) ||
        !Number.isInteger(faltasJustificadas) ||
        faltas < 0 ||
        faltasJustificadas < 0
    ) {
        mensagemFrequencia("Informe uma matrícula e quantidades válidas.", "warning");
        return;
    }

    const matricula = matriculasDaSemana().find(
        item => item.id === matriculaId
    );

    if (!matricula) {
        mensagemFrequencia("A matrícula não pertence à semana selecionada.", "warning");
        return;
    }

    const aulasDisponiveis = (fechamentoSelecionado.dias || [])
        .filter(dia =>
            dia.data >= matricula.dataInicio &&
            (!matricula.dataFim || dia.data <= matricula.dataFim)
        )
        .reduce((total, dia) => total + Number(dia.quantidadeAulas), 0);

    if (faltas + faltasJustificadas > aulasDisponiveis) {
        mensagemFrequencia(
            `As faltas não podem ultrapassar as ${aulasDisponiveis} aulas realizadas durante a matrícula nesta semana.`,
            "warning"
        );
        return;
    }

    await executarOperacao(async () => {
        const fechamentoId = fechamentoSelecionado.id;
        const editando = frequenciaEmEdicao !== null;

        const caminho = editando
            ? `/fechamentos-semanais/${fechamentoId}/frequencias/${frequenciaEmEdicao}`
            : `/fechamentos-semanais/${fechamentoId}/frequencias`;

        const dados = editando
            ? { faltas, faltasJustificadas }
            : { matriculaId, faltas, faltasJustificadas };

        await requisicaoAPI(caminho, {
            method: editando ? "PUT" : "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(dados)
        });

        await selecionarFechamento(fechamentoId);
    }, "Frequência salva com sucesso.");
}

async function excluirFrequencia(id) {
    if (!confirm("Deseja excluir este registro de frequência?")) {
        return;
    }

    await executarOperacao(async () => {
        const fechamentoId = fechamentoSelecionado.id;

        await requisicaoAPI(
            `/fechamentos-semanais/${fechamentoId}/frequencias/${id}`,
            { method: "DELETE" }
        );

        await selecionarFechamento(fechamentoId);
    }, "Frequência excluída com sucesso.");
}

async function finalizarFechamento() {
    if (!fechamentoSelecionado || fechamentoSelecionado.status !== "RASCUNHO") {
        return;
    }

    const matriculas = matriculasDaSemana();

    const pendentes = matriculas.filter(matricula =>
        !frequenciasSelecionadas.some(
            frequencia => frequencia.matriculaId === matricula.id
        )
    );

    if (pendentes.length) {
        mensagemFrequencia(
            `Existem ${pendentes.length} matrícula(s) sem frequência registrada. Registre inclusive os alunos com zero faltas.`,
            "warning"
        );
        return;
    }

    if (!confirm("Deseja finalizar este fechamento? Depois disso, não será possível editar os registros.")) {
        return;
    }

    await executarOperacao(async () => {
        const id = fechamentoSelecionado.id;

        await requisicaoAPI(`/fechamentos-semanais/${id}/fechar`, {
            method: "PUT"
        });

        await carregarFechamentos();
        await selecionarFechamento(id);
    }, "Fechamento semanal finalizado com sucesso.");
}

async function iniciarFrequencias() {
    elementosFrequencia.formFechamento.addEventListener("submit", criarFechamento);
    elementosFrequencia.formDia.addEventListener("submit", salvarDia);
    elementosFrequencia.formFrequencia.addEventListener("submit", salvarFrequencia);

    elementosFrequencia.cancelarDia.addEventListener("click", cancelarEdicaoDia);
    elementosFrequencia.cancelarFrequencia.addEventListener("click", cancelarEdicaoFrequencia);
    elementosFrequencia.fechar.addEventListener("click", finalizarFechamento);

    elementosFrequencia.atualizar.addEventListener("click", () =>
        executarOperacao(carregarFechamentos)
    );

    elementosFrequencia.filtroTurma.addEventListener("change", () =>
        executarOperacao(carregarFechamentos)
    );

    elementosFrequencia.filtroPeriodo.addEventListener("change", () =>
        executarOperacao(carregarFechamentos)
    );

    await executarOperacao(async () => {
        await window.eduAlertaPaginaPronta;
        await carregarDadosFrequencia();
        await carregarFechamentos();
    });
}

iniciarFrequencias();
