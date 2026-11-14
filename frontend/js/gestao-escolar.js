
(async function () {
    const autorizado = await window.eduAlertaPaginaPronta;

    if (!autorizado) {
        return;
    }

    const $ = id => document.getElementById(id);

    const pagina = window.location.pathname.split("/").pop();

    function mensagem(texto, sucesso = false) {
        const elemento = $("mensagem");

        if (!elemento) {
            return;
        }

        elemento.textContent = texto;
        elemento.className = sucesso
            ? "alert alert-success mb-4"
            : "alert alert-danger mb-4";
    }

    function limparMensagem() {
        const elemento = $("mensagem");

        if (elemento) {
            elemento.textContent = "";
            elemento.className = "";
        }
    }

    function dataBR(valor) {
        if (!valor) {
            return "—";
        }

        const partes = valor.split("-");

        if (partes.length !== 3) {
            return valor;
        }

        return `${partes[2]}/${partes[1]}/${partes[0]}`;
    }

    function celula(valor) {
        const td = document.createElement("td");
        td.textContent = valor == null ? "—" : String(valor);
        return td;
    }

    function linhaVazia(colunas, texto) {
        const tr = document.createElement("tr");
        const td = celula(texto);
        td.colSpan = colunas;
        td.className = "text-center text-secondary py-4";
        tr.appendChild(td);
        return tr;
    }

    function botao(texto, classe, acao) {
        const elemento = document.createElement("button");
        elemento.type = "button";
        elemento.className = `btn btn-sm ${classe} me-2 mb-1`;
        elemento.textContent = texto;
        elemento.addEventListener("click", acao);
        return elemento;
    }

    function statusCelula(ativo) {
        const td = document.createElement("td");
        const span = document.createElement("span");
        span.className = ativo ? "badge bg-success" : "badge bg-secondary";
        span.textContent = ativo ? "Ativo" : "Inativo";
        td.appendChild(span);
        return td;
    }

    async function api(caminho, metodo = "GET", dados) {
        const opcoes = {
            method: metodo
        };

        if (dados !== undefined) {
            opcoes.headers = {
                "Content-Type": "application/json"
            };

            opcoes.body = JSON.stringify(dados);
        }

        return requisicaoAPI(caminho, opcoes);
    }

    async function executar(operacao, sucesso) {
        limparMensagem();

        try {
            await operacao();

            if (sucesso) {
                mensagem(sucesso, true);
            }

            return true;
        } catch (erro) {
            mensagem(erro.message || "Não foi possível concluir a operação.");
            return false;
        }
    }

    async function confirmarStatus(item, caminho, recarregar) {
        const novoStatus = !item.ativo;
        const acao = novoStatus ? "ativar" : "desativar";

        if (!confirm(`Deseja ${acao} "${item.nome}"?`)) {
            return;
        }

        await executar(async () => {
            await api(`${caminho}/${item.id}/status`, "PUT", {
                ativo: novoStatus
            });

            await recarregar();
        }, "Status atualizado com sucesso.");
    }

    function iniciarCadastroSimples(configuracao) {
        let registros = [];
        let editandoId = null;

        const form = $("formCadastro");
        const lista = $("registros");
        const salvar = $("salvar");
        const cancelar = $("cancelar");
        const pesquisa = $("pesquisa");
        const atualizar = $("atualizar");
        const titulo = $("tituloFormulario");

        function limparFormulario() {
            form.reset();
            editandoId = null;
            titulo.textContent = configuracao.tituloCadastro;
            cancelar.hidden = true;
        }

        function editar(item) {
            editandoId = item.id;

            configuracao.campos.forEach(campo => {
                $(campo).value = item[campo] ?? "";
            });

            titulo.textContent = configuracao.tituloEdicao;
            cancelar.hidden = false;
            limparMensagem();
            form.scrollIntoView({ behavior: "smooth" });
            $(configuracao.campos[0]).focus();
        }

        function dadosFormulario() {
            const dados = {};

            configuracao.campos.forEach(campo => {
                const elemento = $(campo);

                dados[campo] = elemento.type === "number"
                    ? Number(elemento.value)
                    : elemento.value.trim();
            });

            return dados;
        }

        function renderizar() {
            lista.replaceChildren();

            const termo = pesquisa.value.trim().toLocaleLowerCase("pt-BR");

            const filtrados = registros.filter(item =>
                configuracao.pesquisar(item)
                    .toLocaleLowerCase("pt-BR")
                    .includes(termo)
            );

            if (filtrados.length === 0) {
                lista.appendChild(
                    linhaVazia(4, "Nenhum registro encontrado.")
                );
                return;
            }

            filtrados.forEach(item => {
                const tr = document.createElement("tr");

                configuracao.valores(item).forEach(valor => {
                    tr.appendChild(celula(valor));
                });

                tr.appendChild(statusCelula(item.ativo));

                const acoes = document.createElement("td");

                acoes.appendChild(
                    botao("Editar", "btn-outline-primary", () => editar(item))
                );

                acoes.appendChild(
                    botao(
                        item.ativo ? "Desativar" : "Ativar",
                        item.ativo ? "btn-outline-danger" : "btn-outline-success",
                        () => confirmarStatus(item, configuracao.caminho, carregar)
                    )
                );

                tr.appendChild(acoes);
                lista.appendChild(tr);
            });
        }

        async function carregar() {
            atualizar.disabled = true;

            try {
                const resposta = await api(configuracao.caminho);

                if (!Array.isArray(resposta)) {
                    throw new Error("A API não retornou uma lista válida.");
                }

                registros = resposta;
                renderizar();
            } catch (erro) {
                registros = [];
                lista.replaceChildren(
                    linhaVazia(4, "Não foi possível carregar os registros.")
                );
                mensagem(erro.message);
            } finally {
                atualizar.disabled = false;
            }
        }

        form.addEventListener("submit", async evento => {
            evento.preventDefault();

            const dados = dadosFormulario();
            const id = editandoId;

            salvar.disabled = true;

            try {
                const sucesso = await executar(async () => {
                    await api(
                        id === null
                            ? configuracao.caminho
                            : `${configuracao.caminho}/${id}`,
                        id === null ? "POST" : "PUT",
                        dados
                    );

                    limparFormulario();
                    await carregar();
                }, id === null
                    ? "Cadastro realizado com sucesso."
                    : "Registro atualizado com sucesso."
                );

                if (!sucesso) {
                    return;
                }
            } finally {
                salvar.disabled = false;
            }
        });

        cancelar.addEventListener("click", limparFormulario);
        pesquisa.addEventListener("input", renderizar);
        atualizar.addEventListener("click", carregar);

        carregar();
    }

    if (pagina === "alunos.html") {
        iniciarCadastroSimples({
            caminho: "/alunos",
            campos: ["nome", "dataNascimento"],
            tituloCadastro: "Cadastrar aluno",
            tituloEdicao: "Editar aluno",
            pesquisar: item => item.nome,
            valores: item => [
                item.nome,
                dataBR(item.dataNascimento)
            ]
        });

        return;
    }

    if (pagina === "turmas.html") {
        iniciarCadastroSimples({
            caminho: "/turmas",
            campos: ["nome", "anoLetivo"],
            tituloCadastro: "Cadastrar turma",
            tituloEdicao: "Editar turma",
            pesquisar: item => `${item.nome} ${item.anoLetivo}`,
            valores: item => [
                item.nome,
                item.anoLetivo
            ]
        });

        return;
    }

    if (pagina !== "matriculas.html") {
        return;
    }

    let alunos = [];
    let turmas = [];
    let matriculas = [];
    let periodos = [];
    let periodoEditando = null;

    const formMatricula = $("formMatricula");
    const formPeriodo = $("formPeriodo");

    function preencherSelect(id, itens, nomePadrao) {
        const select = $(id);
        const selecionado = select.value;

        select.replaceChildren();

        const opcaoInicial = document.createElement("option");
        opcaoInicial.value = "";
        opcaoInicial.textContent = nomePadrao;
        select.appendChild(opcaoInicial);

        itens.forEach(item => {
            const opcao = document.createElement("option");
            opcao.value = String(item.id);
            opcao.textContent = item.nome;
            select.appendChild(opcao);
        });

        if (Array.from(select.options).some(opcao => opcao.value === selecionado)) {
            select.value = selecionado;
        }
    }

    async function carregarOpcoes() {
        const [respostaAlunos, respostaTurmas] = await Promise.all([
            api("/alunos"),
            api("/turmas")
        ]);

        if (!Array.isArray(respostaAlunos) || !Array.isArray(respostaTurmas)) {
            throw new Error("Não foi possível consultar alunos e turmas.");
        }

        alunos = respostaAlunos;
        turmas = respostaTurmas;

        preencherSelect(
            "alunoId",
            alunos.filter(aluno => aluno.ativo),
            "Selecione o aluno"
        );

        preencherSelect(
            "turmaId",
            turmas.filter(turma => turma.ativo),
            "Selecione a turma"
        );
    }

    function renderizarMatriculas() {
        const lista = $("listaMatriculas");
        lista.replaceChildren();

        const termo = $("pesquisaMatricula").value
            .trim()
            .toLocaleLowerCase("pt-BR");

        const filtradas = matriculas.filter(item =>
            `${item.alunoNome} ${item.turmaNome}`
                .toLocaleLowerCase("pt-BR")
                .includes(termo)
        );

        if (filtradas.length === 0) {
            lista.appendChild(
                linhaVazia(6, "Nenhuma matrícula encontrada.")
            );
            return;
        }

        filtradas.forEach(item => {
            const tr = document.createElement("tr");

            [
                item.alunoNome,
                item.turmaNome,
                dataBR(item.dataInicio),
                dataBR(item.dataFim),
                item.status
            ].forEach(valor => {
                tr.appendChild(celula(valor));
            });

            const acoes = document.createElement("td");

            if (item.status === "ATIVA") {
                acoes.appendChild(
                    botao("Transferir", "btn-outline-primary", () => transferir(item))
                );

                acoes.appendChild(
                    botao("Encerrar", "btn-outline-danger", () => encerrar(item))
                );
            } else {
                acoes.textContent = "Encerrada";
            }

            tr.appendChild(acoes);
            lista.appendChild(tr);
        });
    }

    async function carregarMatriculas() {
        const resposta = await api("/matriculas");

        if (!Array.isArray(resposta)) {
            throw new Error("A API não retornou uma lista de matrículas.");
        }

        matriculas = resposta;
        renderizarMatriculas();
    }

    async function encerrar(item) {
        const dataFim = prompt(
            `Informe a data de encerramento da matrícula de ${item.alunoNome} (AAAA-MM-DD):`
        );

        if (dataFim === null) {
            return;
        }

        if (!/^\d{4}-\d{2}-\d{2}$/.test(dataFim)) {
            mensagem("Informe uma data no formato AAAA-MM-DD.");
            return;
        }

        await executar(async () => {
            await api(`/matriculas/${item.id}/encerramento`, "PUT", {
                dataFim
            });

            await carregarMatriculas();
        }, "Matrícula encerrada com sucesso.");
    }

    async function transferir(item) {
        const disponiveis = turmas.filter(turma =>
            turma.ativo && turma.id !== item.turmaId
        );

        if (disponiveis.length === 0) {
            mensagem("Não há outra turma ativa disponível para transferência.");
            return;
        }

        const opcoes = disponiveis
            .map(turma => `${turma.id} - ${turma.nome}`)
            .join("\n");

        const entrada = prompt(
            `Informe o ID da turma de destino:\n\n${opcoes}`
        );

        if (entrada === null) {
            return;
        }

        const turmaId = Number(entrada);

        if (!Number.isSafeInteger(turmaId) ||
            !disponiveis.some(turma => turma.id === turmaId)) {
            mensagem("Selecione um ID de turma válido.");
            return;
        }

        const dataTransferencia = prompt(
            "Informe a data da transferência (AAAA-MM-DD):"
        );

        if (dataTransferencia === null) {
            return;
        }

        if (!/^\d{4}-\d{2}-\d{2}$/.test(dataTransferencia)) {
            mensagem("Informe uma data no formato AAAA-MM-DD.");
            return;
        }

        await executar(async () => {
            await api(`/matriculas/${item.id}/transferencia`, "POST", {
                turmaId,
                dataTransferencia
            });

            await carregarMatriculas();
        }, "Transferência realizada com sucesso.");
    }

    formMatricula.addEventListener("submit", async evento => {
        evento.preventDefault();

        const alunoId = Number($("alunoId").value);
        const turmaId = Number($("turmaId").value);
        const dataInicio = $("dataInicio").value;

        if (!alunoId || !turmaId || !dataInicio) {
            mensagem("Preencha todos os campos da matrícula.");
            return;
        }

        const salvar = $("salvarMatricula");
        salvar.disabled = true;

        try {
            await executar(async () => {
                await api("/matriculas", "POST", {
                    alunoId,
                    turmaId,
                    dataInicio
                });

                formMatricula.reset();
                await carregarMatriculas();
            }, "Matrícula cadastrada com sucesso.");
        } finally {
            salvar.disabled = false;
        }
    });

    function limparFormularioPeriodo() {
        formPeriodo.reset();
        periodoEditando = null;
        $("tituloPeriodo").textContent = "Cadastrar período letivo";
        $("cancelarPeriodo").hidden = true;
    }

    function editarPeriodo(item) {
        periodoEditando = item.id;

        $("nomePeriodo").value = item.nome;
        $("anoPeriodo").value = item.anoLetivo;
        $("semestrePeriodo").value = item.numeroSemestre;
        $("inicioPeriodo").value = item.dataInicio;
        $("fimPeriodo").value = item.dataFim;

        $("tituloPeriodo").textContent = "Editar período letivo";
        $("cancelarPeriodo").hidden = false;

        formPeriodo.scrollIntoView({ behavior: "smooth" });
    }

    function renderizarPeriodos() {
        const lista = $("listaPeriodos");
        lista.replaceChildren();

        if (periodos.length === 0) {
            lista.appendChild(
                linhaVazia(7, "Nenhum período letivo cadastrado.")
            );
            return;
        }

        periodos.forEach(item => {
            const tr = document.createElement("tr");

            [
                item.nome,
                item.anoLetivo,
                `${item.numeroSemestre}º`,
                dataBR(item.dataInicio),
                dataBR(item.dataFim)
            ].forEach(valor => {
                tr.appendChild(celula(valor));
            });

            tr.appendChild(statusCelula(item.ativo));

            const acoes = document.createElement("td");

            acoes.appendChild(
                botao("Editar", "btn-outline-primary", () => editarPeriodo(item))
            );

            acoes.appendChild(
                botao(
                    item.ativo ? "Desativar" : "Ativar",
                    item.ativo ? "btn-outline-danger" : "btn-outline-success",
                    () => confirmarStatus(item, "/periodos-letivos", carregarPeriodos)
                )
            );

            tr.appendChild(acoes);
            lista.appendChild(tr);
        });
    }

    async function carregarPeriodos() {
        const resposta = await api("/periodos-letivos");

        if (!Array.isArray(resposta)) {
            throw new Error("A API não retornou uma lista de períodos letivos.");
        }

        periodos = resposta;
        renderizarPeriodos();
    }

    formPeriodo.addEventListener("submit", async evento => {
        evento.preventDefault();

        const dados = {
            nome: $("nomePeriodo").value.trim(),
            anoLetivo: Number($("anoPeriodo").value),
            numeroSemestre: Number($("semestrePeriodo").value),
            dataInicio: $("inicioPeriodo").value,
            dataFim: $("fimPeriodo").value
        };

        if (dados.dataFim < dados.dataInicio) {
            mensagem("A data de término não pode ser anterior à data de início.");
            return;
        }

        const id = periodoEditando;
        const salvar = $("salvarPeriodo");
        salvar.disabled = true;

        try {
            await executar(async () => {
                await api(
                    id === null
                        ? "/periodos-letivos"
                        : `/periodos-letivos/${id}`,
                    id === null ? "POST" : "PUT",
                    dados
                );

                limparFormularioPeriodo();
                await carregarPeriodos();
            }, id === null
                ? "Período letivo cadastrado com sucesso."
                : "Período letivo atualizado com sucesso."
            );
        } finally {
            salvar.disabled = false;
        }
    });

    $("cancelarPeriodo").addEventListener("click", limparFormularioPeriodo);

    $("pesquisaMatricula").addEventListener("input", renderizarMatriculas);

    $("atualizarMatriculas").addEventListener("click", async () => {
        await executar(async () => {
            await Promise.all([
                carregarOpcoes(),
                carregarMatriculas()
            ]);
        });
    });

    document.querySelectorAll("#abasGestao .nav-link").forEach(aba => {
        aba.addEventListener("click", () => {
            document.querySelectorAll("#abasGestao .nav-link").forEach(item => {
                item.classList.remove("active");
            });

            aba.classList.add("active");

            $("secaoMatriculas").hidden =
                aba.dataset.bsTarget !== "#secaoMatriculas";

            $("secaoPeriodos").hidden =
                aba.dataset.bsTarget !== "#secaoPeriodos";

            limparMensagem();
        });
    });

    await executar(async () => {
        await Promise.all([
            carregarOpcoes(),
            carregarMatriculas(),
            carregarPeriodos()
        ]);
    });
})();
