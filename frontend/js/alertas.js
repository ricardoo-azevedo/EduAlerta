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

    const botaoLimpar = $("limparFiltrosAlertas");

    let carregando = false;

    function avisar(texto, sucesso = false) {
        mensagem.replaceChildren();

        if (!texto) return;

        const aviso = document.createElement("div");

        aviso.className = `alert alert-${
            sucesso ? "success" : "danger"
        } mb-4`;

        aviso.textContent = texto;

        mensagem.appendChild(aviso);
    }

    function preencher(select, registros, legenda, texto) {
        const atual = select.value;

        select.replaceChildren(
            new Option(legenda, "")
        );

        registros.forEach(item => {
            select.add(
                new Option(texto(item), item.id)
            );
        });

        const existe = Array.from(select.options).some(
            opcao => opcao.value === atual
        );

        if (existe) {
            select.value = atual;
        }
    }

    function adicionarCelula(linha, valor) {
        const celula = document.createElement("td");

        celula.textContent = valor == null
            ? "—"
            : String(valor);

        linha.appendChild(celula);

        return celula;
    }

    function dataHora(valor) {
        if (!valor) return "—";

        const partes = String(valor).split("T");

        const data = partes[0]
            .split("-")
            .reverse()
            .join("/");

        return partes[1]
            ? `${data} ${partes[1].slice(0, 5)}`
            : data;
    }

    function percentual(valor) {
        if (valor === null || valor === undefined) {
            return "—";
        }

        const numero = Number(valor);

        return Number.isFinite(numero)
            ? `${numero.toFixed(2).replace(".", ",")}%`
            : "—";
    }

    function vazio(texto) {
        lista.replaceChildren();

        const linha = document.createElement("tr");

        const celula = adicionarCelula(
            linha,
            texto
        );

        celula.colSpan = 7;

        celula.className =
            "text-center text-secondary py-4";

        lista.appendChild(linha);
    }

    function definirCarregamento(valor) {
        carregando = valor;

        botaoBuscar.disabled = valor;
        botaoAtualizar.disabled = valor;
        botaoLimpar.disabled = valor;

        filtroAluno.disabled = valor;
        filtroPeriodo.disabled = valor;
        filtroNivel.disabled = valor;
        filtroSituacao.disabled = valor;

        lista.querySelectorAll("button").forEach(
            botao => {
                botao.disabled = valor;
            }
        );
    }

    function criarLinkAcompanhamento(alerta) {
        const link = document.createElement("a");

        const parametros = new URLSearchParams({
            alertaId: String(alerta.id)
        });

        link.href = `acompanhamento.html?${parametros}`;

        link.className =
            "btn btn-outline-primary btn-sm";

        link.innerHTML =
            '<i class="bi bi-clipboard-heart me-1"></i>' +
            'Acompanhamentos';

        link.setAttribute(
            "aria-label",
            `Consultar acompanhamentos de ${alerta.alunoNome}`
        );

        return link;
    }

    async function resolverAlerta(alerta, botao) {
        if (carregando) return;

        const confirmado = confirm(
            `Resolver manualmente o alerta de ${alerta.alunoNome}?`
        );

        if (!confirmado) return;

        botao.disabled = true;

        definirCarregamento(true);

        avisar("");

        try {
            await requisicaoAPI(
                `/alertas/${alerta.id}/resolver`,
                {
                    method: "PUT"
                }
            );

            const atualizado = await carregarAlertas();

            if (atualizado) {
                avisar(
                    "Alerta resolvido manualmente com sucesso.",
                    true
                );
            } else {
                avisar(
                    "O alerta foi resolvido, mas não foi " +
                    "possível atualizar a listagem."
                );
            }

        } catch (erro) {
            avisar(
                erro.message ||
                "Não foi possível resolver o alerta."
            );

        } finally {
            botao.disabled = false;

            definirCarregamento(false);
        }
    }

    function renderizar(alertas) {
        lista.replaceChildren();

        contador.textContent =
            `${alertas.length} alerta(s)`;

        if (alertas.length === 0) {
            vazio(
                "Nenhum alerta encontrado para os filtros informados."
            );

            return;
        }

        alertas.forEach(alerta => {
            const linha = document.createElement("tr");

            adicionarCelula(
                linha,
                alerta.alunoNome
            );

            adicionarCelula(
                linha,
                alerta.periodoLetivoNome
            );

            adicionarCelula(
                linha,
                percentual(alerta.percentualFaltas)
            );

            const celulaNivel = adicionarCelula(
                linha,
                ""
            );

            const nivel = document.createElement("span");

            nivel.className = alerta.nivel === "CRITICO"
                ? "badge bg-danger"
                : "badge bg-warning text-dark";

            nivel.textContent = alerta.nivel === "CRITICO"
                ? "Crítico"
                : "Preventivo";

            celulaNivel.appendChild(nivel);

            const celulaSituacao = adicionarCelula(
                linha,
                ""
            );

            const situacao = document.createElement("span");

            situacao.className = alerta.situacao === "ATIVO"
                ? "badge bg-primary"
                : "badge bg-secondary";

            situacao.textContent = alerta.situacao === "ATIVO"
                ? "Ativo"
                : "Resolvido";

            celulaSituacao.appendChild(situacao);

            adicionarCelula(
                linha,
                dataHora(alerta.atualizadoEm)
            );

            const acoes = adicionarCelula(
                linha,
                ""
            );

            const grupoAcoes = document.createElement("div");

            grupoAcoes.className =
                "d-flex flex-wrap gap-2";

            grupoAcoes.appendChild(
                criarLinkAcompanhamento(alerta)
            );

            if (alerta.situacao === "ATIVO") {
                const botao = document.createElement("button");

                botao.type = "button";

                botao.className =
                    "btn btn-outline-primary btn-sm";

                botao.textContent = "Resolver";

                botao.addEventListener(
                    "click",
                    () => resolverAlerta(alerta, botao)
                );

                grupoAcoes.appendChild(botao);
            }

            acoes.appendChild(grupoAcoes);

            if (alerta.motivo) {
                const motivo = document.createElement("tr");

                const celula = adicionarCelula(
                    motivo,
                    alerta.motivo
                );

                celula.colSpan = 7;

                celula.className =
                    "small text-secondary pt-0";

                lista.append(linha, motivo);

            } else {
                lista.appendChild(linha);
            }
        });
    }

    async function carregarAlertas() {
        const parametros = new URLSearchParams();

        if (filtroAluno.value) {
            parametros.set(
                "alunoId",
                filtroAluno.value
            );
        }

        if (filtroPeriodo.value) {
            parametros.set(
                "periodoLetivoId",
                filtroPeriodo.value
            );
        }

        if (filtroNivel.value) {
            parametros.set(
                "nivel",
                filtroNivel.value
            );
        }

        if (filtroSituacao.value) {
            parametros.set(
                "situacao",
                filtroSituacao.value
            );
        }

        const caminho = parametros.size
            ? `/alertas?${parametros}`
            : "/alertas";

        definirCarregamento(true);

        contador.textContent = "Carregando...";

        vazio("Consultando alertas...");

        try {
            const alertas = await requisicaoAPI(caminho);

            if (!Array.isArray(alertas)) {
                throw new Error(
                    "Resposta inesperada da API de alertas."
                );
            }

            renderizar(alertas);

            return true;

        } catch (erro) {
            contador.textContent =
                "Não foi possível consultar";

            vazio(
                "Não foi possível carregar os alertas."
            );

            avisar(
                erro.message ||
                "Falha ao consultar alertas."
            );

            return false;

        } finally {
            definirCarregamento(false);
        }
    }

    async function carregarFiltros() {
        const resultados = await Promise.allSettled([
            requisicaoAPI("/alunos"),
            requisicaoAPI("/periodos-letivos")
        ]);

        if (
            resultados[0].status === "fulfilled" &&
            Array.isArray(resultados[0].value)
        ) {
            preencher(
                filtroAluno,
                resultados[0].value,
                "Todos os alunos",
                item => item.nome
            );
        }

        if (
            resultados[1].status === "fulfilled" &&
            Array.isArray(resultados[1].value)
        ) {
            preencher(
                filtroPeriodo,
                resultados[1].value,
                "Todos os períodos",
                item => item.nome
            );
        }

        const falha = resultados.some(
            resultado =>
                resultado.status === "rejected" ||
                !Array.isArray(resultado.value)
        );

        if (falha) {
            avisar(
                "Não foi possível atualizar todas as opções " +
                "de filtro. A consulta de alertas continua disponível."
            );
        }
    }

    formulario.addEventListener(
        "submit",
        async evento => {
            evento.preventDefault();

            if (carregando) return;

            avisar("");

            await carregarAlertas();
        }
    );

    botaoLimpar.addEventListener(
        "click",
        async () => {
            if (carregando) return;

            formulario.reset();

            avisar("");

            await carregarAlertas();
        }
    );

    botaoAtualizar.addEventListener(
        "click",
        async () => {
            if (carregando) return;

            avisar("");

            definirCarregamento(true);

            try {
                await carregarFiltros();
                await carregarAlertas();

            } finally {
                definirCarregamento(false);
            }
        }
    );

    definirCarregamento(true);

    try {
        await carregarFiltros();
        await carregarAlertas();

    } finally {
        definirCarregamento(false);
    }
})();