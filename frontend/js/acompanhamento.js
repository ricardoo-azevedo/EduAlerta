(async function () {
    if (!await window.eduAlertaPaginaPronta) return;

    const $ = id => document.getElementById(id);

    const mensagem = $("mensagemAcompanhamento");
    const filtroSituacao = $("filtroSituacaoAcompanhamento");
    const selecaoAlerta = $("selecaoAlerta");
    const detalhesAlerta = $("detalhesAlerta");
    const formulario = $("formAcompanhamento");
    const campos = $("camposAcompanhamento");
    const botaoSalvar = $("salvarAcompanhamento");
    const botaoAtualizar = $("atualizarAcompanhamentos");
    const lista = $("listaAcompanhamentos");
    const contador = $("contadorAcompanhamentos");

    const parametrosPagina = new URLSearchParams(
        window.location.search
    );

    const alertaInicial = parametrosPagina.get("alertaId");

    let alertasDisponiveis = [];
    let alertaSelecionado = null;
    let processando = false;
    let carregandoHistorico = false;
    let versaoConsulta = 0;

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

    function atualizarControles() {
        filtroSituacao.disabled = processando;

        selecaoAlerta.disabled =
            processando || alertasDisponiveis.length === 0;

        botaoAtualizar.disabled = processando;

        campos.disabled =
            processando ||
            carregandoHistorico ||
            !alertaSelecionado;

        botaoSalvar.disabled =
            processando ||
            carregandoHistorico ||
            !alertaSelecionado;
    }

    function definirProcessamento(valor) {
        processando = valor;
        atualizarControles();
    }

    function formatarNivel(nivel) {
        if (nivel === "CRITICO") return "Crítico";
        if (nivel === "PREVENTIVO") return "Preventivo";

        return nivel || "—";
    }

    function formatarSituacao(situacao) {
        if (situacao === "ATIVO") return "Ativo";
        if (situacao === "RESOLVIDO") return "Resolvido";

        return situacao || "—";
    }

    function formatarPercentual(valor) {
        if (valor === null || valor === undefined) {
            return "—";
        }

        const numero = Number(valor);

        if (!Number.isFinite(numero)) return "—";

        return `${numero.toFixed(2).replace(".", ",")}%`;
    }

    function formatarDataHora(valor) {
        if (!valor) return "—";

        const partes = String(valor).split("T");

        const data = partes[0]
            .split("-")
            .reverse()
            .join("/");

        if (!partes[1]) return data;

        return `${data} às ${partes[1].slice(0, 5)}`;
    }

    function adicionarCelula(linha, valor) {
        const celula = document.createElement("td");

        celula.textContent =
            valor === null || valor === undefined
                ? "—"
                : String(valor);

        celula.style.whiteSpace = "pre-wrap";
        celula.style.overflowWrap = "anywhere";

        linha.appendChild(celula);

        return celula;
    }

    function mostrarHistoricoVazio(texto) {
        lista.replaceChildren();

        const linha = document.createElement("tr");
        const celula = adicionarCelula(linha, texto);

        celula.colSpan = 5;
        celula.className = "text-center text-secondary py-4";

        lista.appendChild(linha);
    }

    function renderizarHistorico(acompanhamentos) {
        lista.replaceChildren();

        contador.textContent =
            `${acompanhamentos.length} acompanhamento(s)`;

        if (acompanhamentos.length === 0) {
            mostrarHistoricoVazio(
                "Nenhum acompanhamento registrado para este alerta."
            );

            return;
        }

        acompanhamentos.forEach(acompanhamento => {
            const linha = document.createElement("tr");

            adicionarCelula(
                linha,
                formatarDataHora(acompanhamento.dataHora)
            );

            adicionarCelula(
                linha,
                acompanhamento.usuarioNome
            );

            adicionarCelula(
                linha,
                acompanhamento.tipoIntervencao
            );

            adicionarCelula(
                linha,
                acompanhamento.descricao
            );

            adicionarCelula(
                linha,
                acompanhamento.resultado
            );

            lista.appendChild(linha);
        });
    }

    function renderizarDetalhes(alerta) {
        if (!alerta) {
            detalhesAlerta.hidden = true;
            return;
        }

        detalhesAlerta.hidden = false;

        $("alunoAcompanhamento").textContent =
            alerta.alunoNome || "—";

        $("periodoAcompanhamento").textContent =
            alerta.periodoLetivoNome || "—";

        $("percentualAcompanhamento").textContent =
            formatarPercentual(alerta.percentualFaltas);

        $("motivoAcompanhamento").textContent =
            alerta.motivo || "—";

        const nivel = $("nivelAcompanhamento");

        nivel.textContent = formatarNivel(alerta.nivel);

        nivel.className = alerta.nivel === "CRITICO"
            ? "badge bg-danger"
            : "badge bg-warning text-dark";

        const situacao = $("situacaoAcompanhamento");

        situacao.textContent =
            formatarSituacao(alerta.situacao);

        situacao.className = alerta.situacao === "ATIVO"
            ? "badge bg-primary"
            : "badge bg-secondary";
    }

    function atualizarURL(alertaId) {
        const url = new URL(window.location.href);

        if (alertaId) {
            url.searchParams.set("alertaId", alertaId);
        } else {
            url.searchParams.delete("alertaId");
        }

        window.history.replaceState(
            null,
            "",
            url.toString()
        );
    }

    function limparSelecao(limparFormulario = true) {
        versaoConsulta++;
        carregandoHistorico = false;
        alertaSelecionado = null;

        if (limparFormulario) {
            formulario.reset();
        }

        detalhesAlerta.hidden = true;

        contador.textContent = "0 acompanhamento(s)";

        mostrarHistoricoVazio(
            "Selecione um alerta para consultar seu histórico."
        );

        atualizarURL(null);
        atualizarControles();
    }

    function preencherAlertas(idSelecionado = null) {
        selecaoAlerta.replaceChildren(
            new Option("Selecione um alerta", "")
        );

        alertasDisponiveis.forEach(alerta => {
            const texto = [
                alerta.alunoNome || "Aluno não identificado",
                alerta.periodoLetivoNome || "Período não informado",
                formatarNivel(alerta.nivel),
                formatarSituacao(alerta.situacao)
            ].join(" — ");

            selecaoAlerta.add(
                new Option(texto, String(alerta.id))
            );
        });

        if (!alertasDisponiveis.length) {
            selecaoAlerta.replaceChildren(
                new Option("Nenhum alerta encontrado", "")
            );
        }

        if (idSelecionado !== null) {
            const existe = alertasDisponiveis.some(
                alerta =>
                    String(alerta.id) === String(idSelecionado)
            );

            if (existe) {
                selecaoAlerta.value = String(idSelecionado);
            }
        }

        atualizarControles();
    }

    async function carregarHistorico(alertaId) {
        const consultaAtual = ++versaoConsulta;

        carregandoHistorico = true;
        atualizarControles();

        contador.textContent = "Carregando...";

        mostrarHistoricoVazio(
            "Consultando histórico de acompanhamentos..."
        );

        try {
            const acompanhamentos = await requisicaoAPI(
                `/alertas/${encodeURIComponent(alertaId)}/acompanhamentos`
            );

            if (consultaAtual !== versaoConsulta) {
                return false;
            }

            if (!Array.isArray(acompanhamentos)) {
                throw new Error(
                    "Resposta inesperada da API de acompanhamentos."
                );
            }

            renderizarHistorico(acompanhamentos);

            return true;

        } catch (erro) {
            if (consultaAtual !== versaoConsulta) {
                return false;
            }

            contador.textContent = "Consulta indisponível";

            mostrarHistoricoVazio(
                "Não foi possível consultar o histórico."
            );

            avisar(
                erro.message ||
                "Falha ao carregar acompanhamentos."
            );

            return false;

        } finally {
            if (consultaAtual === versaoConsulta) {
                carregandoHistorico = false;
                atualizarControles();
            }
        }
    }

    async function selecionarAlerta(
        id,
        limparFormulario = true
    ) {
        const alerta = alertasDisponiveis.find(
            item => String(item.id) === String(id)
        );

        if (!alerta) {
            limparSelecao(limparFormulario);
            return false;
        }

        const selecaoAnterior = alertaSelecionado?.id;

        alertaSelecionado = alerta;

        if (
            limparFormulario &&
            String(selecaoAnterior) !== String(alerta.id)
        ) {
            formulario.reset();
        }

        selecaoAlerta.value = String(alerta.id);

        renderizarDetalhes(alerta);
        atualizarURL(alerta.id);
        atualizarControles();

        return carregarHistorico(alerta.id);
    }

    async function carregarAlertas(
        idSelecionado = null,
        limparFormulario = true
    ) {
        versaoConsulta++;
        carregandoHistorico = false;

        definirProcessamento(true);

        try {
            const parametros = new URLSearchParams();

            if (filtroSituacao.value) {
                parametros.set(
                    "situacao",
                    filtroSituacao.value
                );
            }

            const caminho = parametros.size
                ? `/alertas?${parametros}`
                : "/alertas";

            const resposta = await requisicaoAPI(caminho);

            if (!Array.isArray(resposta)) {
                throw new Error(
                    "Resposta inesperada da API de alertas."
                );
            }

            alertasDisponiveis = resposta;

            preencherAlertas(idSelecionado);

            if (selecaoAlerta.value) {
                return await selecionarAlerta(
                    selecaoAlerta.value,
                    limparFormulario
                );
            }

            limparSelecao(limparFormulario);

            if (idSelecionado !== null) {
                avisar(
                    "O alerta solicitado não foi encontrado " +
                    "nos resultados da consulta."
                );

                return false;
            }

            return true;

        } catch (erro) {
            alertasDisponiveis = [];

            preencherAlertas();

            limparSelecao(false);

            avisar(
                erro.message ||
                "Não foi possível carregar os alertas."
            );

            return false;

        } finally {
            definirProcessamento(false);
        }
    }

    function obterDadosFormulario() {
        return {
            tipoIntervencao:
                $("tipoIntervencao").value.trim(),

            descricao:
                $("descricaoIntervencao").value.trim(),

            resultado:
                $("resultadoIntervencao").value.trim()
        };
    }

    function validarDados(dados) {
        if (!dados.tipoIntervencao) {
            throw new Error(
                "Informe o tipo de intervenção."
            );
        }

        if (dados.tipoIntervencao.length > 50) {
            throw new Error(
                "O tipo de intervenção deve possuir " +
                "no máximo 50 caracteres."
            );
        }

        if (!dados.descricao) {
            throw new Error(
                "Informe a descrição da intervenção."
            );
        }

        if (dados.descricao.length > 1000) {
            throw new Error(
                "A descrição deve possuir " +
                "no máximo 1.000 caracteres."
            );
        }

        if (!dados.resultado) {
            throw new Error(
                "Informe o resultado da intervenção."
            );
        }

        if (dados.resultado.length > 1000) {
            throw new Error(
                "O resultado deve possuir " +
                "no máximo 1.000 caracteres."
            );
        }
    }

    formulario.addEventListener(
        "submit",
        async evento => {
            evento.preventDefault();

            if (processando || carregandoHistorico) return;

            avisar("");

            if (!alertaSelecionado) {
                avisar(
                    "Selecione um alerta antes de registrar " +
                    "o acompanhamento."
                );

                return;
            }

            const dados = obterDadosFormulario();

            try {
                validarDados(dados);

            } catch (erro) {
                avisar(erro.message);
                return;
            }

            const alertaId = alertaSelecionado.id;

            const confirmado = confirm(
                `Registrar acompanhamento para ` +
                `${alertaSelecionado.alunoNome}? ` +
                `O registro não altera a situação do alerta.`
            );

            if (!confirmado) return;

            definirProcessamento(true);

            botaoSalvar.textContent = "Registrando...";

            try {
                await requisicaoAPI(
                    `/alertas/${encodeURIComponent(alertaId)}/acompanhamentos`,
                    {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json"
                        },
                        body: JSON.stringify(dados)
                    }
                );

                formulario.reset();

                filtroSituacao.value = "";

                const atualizado = await carregarAlertas(
                    alertaId,
                    false
                );

                if (atualizado) {
                    avisar(
                        "Acompanhamento registrado com sucesso. " +
                        "A situação do alerta foi mantida.",
                        true
                    );
                } else {
                    avisar(
                        "O acompanhamento foi registrado, mas " +
                        "não foi possível atualizar a consulta. " +
                        "Utilize o botão Atualizar antes de " +
                        "realizar outro registro."
                    );
                }

            } catch (erro) {
                avisar(
                    erro.message ||
                    "Não foi possível registrar o acompanhamento."
                );

            } finally {
                botaoSalvar.innerHTML =
                    '<i class="bi bi-check-circle me-2"></i>' +
                    'Registrar acompanhamento';

                definirProcessamento(false);
            }
        }
    );

    selecaoAlerta.addEventListener(
        "change",
        async () => {
            if (processando) return;

            avisar("");

            if (!selecaoAlerta.value) {
                limparSelecao();
                return;
            }

            await selecionarAlerta(
                selecaoAlerta.value
            );
        }
    );

    filtroSituacao.addEventListener(
        "change",
        async () => {
            if (processando) return;

            avisar("");

            await carregarAlertas();
        }
    );

    botaoAtualizar.addEventListener(
        "click",
        async () => {
            if (processando) return;

            avisar("");

            await carregarAlertas(
                alertaSelecionado?.id || null,
                false
            );
        }
    );

    await carregarAlertas(alertaInicial);
})();