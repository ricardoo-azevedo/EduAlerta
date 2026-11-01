const nomeAluno = document.getElementById("nomeAluno");
const matriculaAluno = document.getElementById("matriculaAluno");
const btnCadastrar = document.getElementById("btnCadastrar");
const listaAlunos = document.getElementById("listaAlunos");
const totalAlunos = document.getElementById("totalAlunos");
const pesquisaAluno = document.getElementById("pesquisaAluno");

const alunoFrequencia = document.getElementById("alunoFrequencia");
const dataFrequencia = document.getElementById("dataFrequencia");
const btnPresente = document.getElementById("btnPresente");
const btnAusente = document.getElementById("btnAusente");
const frequenciaMedia = document.getElementById("frequenciaMedia");
const totalAlertas = document.getElementById("totalAlertas");
const listaAlertas = document.getElementById("listaAlertas");
const historicoFrequencia = document.getElementById("historicoFrequencia");
const pesquisaHistorico = document.getElementById("pesquisaHistorico");
const filtroSituacao = document.getElementById("filtroSituacao");

const alunoRelatorio = document.getElementById("alunoRelatorio");
const relatorioPresencas = document.getElementById("relatorioPresencas");
const relatorioAusencias = document.getElementById("relatorioAusencias");
const relatorioPercentual = document.getElementById("relatorioPercentual");
const relatorioMensagem = document.getElementById("relatorioMensagem");

const notificacaoEduAlerta = document.getElementById("notificacaoEduAlerta");
const notificacaoIcone = document.getElementById("notificacaoIcone");
const notificacaoMensagem = document.getElementById("notificacaoMensagem");

let alunos = JSON.parse(localStorage.getItem("alunosEduAlerta")) || [];
let frequencias = JSON.parse(localStorage.getItem("frequenciasEduAlerta")) || [];

function mostrarNotificacao(mensagem, tipo = "sucesso") {
    if (!notificacaoEduAlerta || !window.bootstrap) {
        alert(mensagem);
        return;
    }

    const configuracoes = {
        sucesso: {
            icone: "bi-check-circle-fill",
            cor: "#198754"
        },
        erro: {
            icone: "bi-x-circle-fill",
            cor: "#DC3545"
        },
        aviso: {
            icone: "bi-exclamation-circle-fill",
            cor: "#8061D7"
        }
    };

    const configuracao = configuracoes[tipo] || configuracoes.aviso;

    notificacaoMensagem.textContent = mensagem;
    notificacaoIcone.className = `bi ${configuracao.icone} me-2`;
    notificacaoIcone.style.color = configuracao.cor;

    const toast = bootstrap.Toast.getOrCreateInstance(notificacaoEduAlerta, {
        delay: 4000
    });

    toast.show();
}

function salvarAlunos() {
    localStorage.setItem("alunosEduAlerta", JSON.stringify(alunos));
}

function salvarFrequencias() {
    localStorage.setItem("frequenciasEduAlerta", JSON.stringify(frequencias));
}

function atualizarFrequenciaMedia() {
    const totalRegistros = frequencias.length;

    if (totalRegistros === 0) {
        frequenciaMedia.textContent = "0%";
        return;
    }

    const totalPresencas = frequencias.filter(function (registro) {
        return registro.status === "Presença";
    }).length;

    const percentual = (totalPresencas / totalRegistros) * 100;
    frequenciaMedia.textContent = `${percentual.toFixed(1)}%`;
}

function atualizarAlertas() {
    listaAlertas.innerHTML = "";
    let quantidadeAlertas = 0;

    alunos.forEach(function (aluno) {
        const registrosAluno = frequencias.filter(function (registro) {
            return registro.matricula === aluno.matricula;
        });

        if (registrosAluno.length === 0) {
            return;
        }

        const presencas = registrosAluno.filter(function (registro) {
            return registro.status === "Presença";
        }).length;

        const percentual = (presencas / registrosAluno.length) * 100;

        if (percentual < 75) {
            quantidadeAlertas++;

            const linha = document.createElement("tr");

            const celulaNome = document.createElement("td");
            celulaNome.textContent = aluno.nome;

            const celulaMatricula = document.createElement("td");
            celulaMatricula.textContent = aluno.matricula;

            const celulaFrequencia = document.createElement("td");
            celulaFrequencia.textContent = `${percentual.toFixed(1)}%`;

            const celulaSituacao = document.createElement("td");
            const indicador = document.createElement("span");
            indicador.className = "badge bg-danger";
            indicador.textContent = "Abaixo de 75%";
            celulaSituacao.appendChild(indicador);

            linha.appendChild(celulaNome);
            linha.appendChild(celulaMatricula);
            linha.appendChild(celulaFrequencia);
            linha.appendChild(celulaSituacao);

            listaAlertas.appendChild(linha);
        }
    });

    totalAlertas.textContent = quantidadeAlertas;

    if (quantidadeAlertas === 0) {
        const linha = document.createElement("tr");
        const celula = document.createElement("td");

        celula.colSpan = 4;
        celula.className = "text-center";
        celula.textContent = "Nenhum aluno em situação de alerta.";

        linha.appendChild(celula);
        listaAlertas.appendChild(linha);
    }
}

function atualizarHistorico() {
    historicoFrequencia.innerHTML = "";

    const pesquisa = pesquisaHistorico.value.trim().toLowerCase();
    const situacaoSelecionada = filtroSituacao.value;

    const registrosFiltrados = frequencias.filter(function (registro) {
        const aluno = alunos.find(function (item) {
            return item.matricula === registro.matricula;
        });

        const nome = aluno ? aluno.nome.toLowerCase() : "";
        const matricula = registro.matricula.toLowerCase();

        const correspondePesquisa =
            nome.includes(pesquisa) || matricula.includes(pesquisa);

        const correspondeSituacao =
            situacaoSelecionada === "Todas" ||
            registro.status === situacaoSelecionada;

        return correspondePesquisa && correspondeSituacao;
    });

    if (registrosFiltrados.length === 0) {
        const linha = document.createElement("tr");
        const celula = document.createElement("td");

        celula.colSpan = 5;
        celula.className = "text-center";
        celula.textContent = "Nenhum registro encontrado para os filtros selecionados.";

        linha.appendChild(celula);
        historicoFrequencia.appendChild(linha);
        return;
    }

    const registrosOrdenados = [...registrosFiltrados].sort(function (a, b) {
        return b.data.localeCompare(a.data);
    });

    registrosOrdenados.forEach(function (registro) {
        const aluno = alunos.find(function (item) {
            return item.matricula === registro.matricula;
        });

        const linha = document.createElement("tr");

        const celulaNome = document.createElement("td");
        celulaNome.textContent = aluno ? aluno.nome : "Aluno não encontrado";

        const celulaMatricula = document.createElement("td");
        celulaMatricula.textContent = registro.matricula;

        const celulaData = document.createElement("td");
        celulaData.textContent = registro.data.split("-").reverse().join("/");

        const celulaSituacao = document.createElement("td");
        const indicador = document.createElement("span");

        indicador.className = registro.status === "Presença"
            ? "badge bg-success"
            : "badge bg-danger";

        indicador.textContent = registro.status;
        celulaSituacao.appendChild(indicador);

        const celulaAcoes = document.createElement("td");

        const botaoEditar = document.createElement("button");
        botaoEditar.type = "button";
        botaoEditar.className = "btn btn-outline-primary btn-sm me-2";
        botaoEditar.innerHTML = '<i class="bi bi-pencil-square me-1"></i>Editar';

        botaoEditar.addEventListener("click", function () {
            const novaSituacao = registro.status === "Presença"
                ? "Ausência"
                : "Presença";

            const confirmarEdicao = confirm(
                `Deseja alterar o registro de ${registro.data.split("-").reverse().join("/")} de ${registro.status} para ${novaSituacao}?`
            );

            if (!confirmarEdicao) {
                return;
            }

            registro.status = novaSituacao;

            salvarFrequencias();
            atualizarFrequenciaMedia();
            atualizarAlertas();
            atualizarHistorico();
            atualizarRelatorio();

            mostrarNotificacao("Registro de frequência atualizado com sucesso!");
        });

        const botaoExcluir = document.createElement("button");
        botaoExcluir.type = "button";
        botaoExcluir.className = "btn btn-danger btn-sm";
        botaoExcluir.innerHTML = '<i class="bi bi-trash me-1"></i>Excluir';

        botaoExcluir.addEventListener("click", function () {
            const confirmarExclusao = confirm(
                `Deseja excluir o registro de ${registro.data.split("-").reverse().join("/")} de ${aluno ? aluno.nome : registro.matricula}?`
            );

            if (!confirmarExclusao) {
                return;
            }

            frequencias = frequencias.filter(function (item) {
                return item !== registro;
            });

            salvarFrequencias();
            atualizarFrequenciaMedia();
            atualizarAlertas();
            atualizarHistorico();
            atualizarRelatorio();

            mostrarNotificacao("Registro de frequência excluído com sucesso!");
        });

        celulaAcoes.appendChild(botaoEditar);
        celulaAcoes.appendChild(botaoExcluir);

        linha.appendChild(celulaNome);
        linha.appendChild(celulaMatricula);
        linha.appendChild(celulaData);
        linha.appendChild(celulaSituacao);
        linha.appendChild(celulaAcoes);

        historicoFrequencia.appendChild(linha);
    });
}

function atualizarListaFrequencia() {
    const matriculaSelecionada = alunoFrequencia.value;
    alunoFrequencia.innerHTML = "";

    const opcaoInicial = document.createElement("option");
    opcaoInicial.value = "";
    opcaoInicial.textContent = "Selecione um aluno";
    alunoFrequencia.appendChild(opcaoInicial);

    alunos.forEach(function (aluno) {
        const opcao = document.createElement("option");
        opcao.value = aluno.matricula;
        opcao.textContent = `${aluno.nome} - ${aluno.matricula}`;
        alunoFrequencia.appendChild(opcao);
    });

    const alunoAindaExiste = alunos.some(function (aluno) {
        return aluno.matricula === matriculaSelecionada;
    });

    if (alunoAindaExiste) {
        alunoFrequencia.value = matriculaSelecionada;
    }
}

function atualizarListaRelatorio() {
    const matriculaSelecionada = alunoRelatorio.value;
    alunoRelatorio.innerHTML = "";

    const opcaoInicial = document.createElement("option");
    opcaoInicial.value = "";
    opcaoInicial.textContent = "Selecione um aluno";
    alunoRelatorio.appendChild(opcaoInicial);

    alunos.forEach(function (aluno) {
        const opcao = document.createElement("option");
        opcao.value = aluno.matricula;
        opcao.textContent = `${aluno.nome} - ${aluno.matricula}`;
        alunoRelatorio.appendChild(opcao);
    });

    const alunoAindaExiste = alunos.some(function (aluno) {
        return aluno.matricula === matriculaSelecionada;
    });

    if (alunoAindaExiste) {
        alunoRelatorio.value = matriculaSelecionada;
    }

    atualizarRelatorio();
}

function atualizarRelatorio() {
    const matricula = alunoRelatorio.value;

    relatorioPresencas.textContent = "0";
    relatorioAusencias.textContent = "0";
    relatorioPercentual.textContent = "0%";

    if (matricula === "") {
        relatorioMensagem.textContent = "Selecione um aluno para visualizar o relatório.";
        return;
    }

    const aluno = alunos.find(function (item) {
        return item.matricula === matricula;
    });

    if (!aluno) {
        relatorioMensagem.textContent = "Aluno não encontrado.";
        return;
    }

    const registros = frequencias.filter(function (registro) {
        return registro.matricula === matricula;
    });

    if (registros.length === 0) {
        relatorioMensagem.textContent = `${aluno.nome} ainda não possui registros de frequência.`;
        return;
    }

    const presencas = registros.filter(function (registro) {
        return registro.status === "Presença";
    }).length;

    const ausencias = registros.filter(function (registro) {
        return registro.status === "Ausência";
    }).length;

    const percentual = (presencas / registros.length) * 100;

    relatorioPresencas.textContent = presencas;
    relatorioAusencias.textContent = ausencias;
    relatorioPercentual.textContent = `${percentual.toFixed(1)}%`;

    relatorioMensagem.textContent = percentual < 75
        ? `${aluno.nome} está em situação de alerta por frequência inferior a 75%.`
        : `${aluno.nome} está com frequência igual ou superior a 75%.`;
}

function atualizarTabela() {
    listaAlunos.innerHTML = "";

    const pesquisa = pesquisaAluno.value.trim().toLowerCase();

    alunos.forEach(function (aluno, indice) {
        const nomeEncontrado = aluno.nome.toLowerCase().includes(pesquisa);
        const matriculaEncontrada = aluno.matricula.toLowerCase().includes(pesquisa);

        if (!nomeEncontrado && !matriculaEncontrada) {
            return;
        }

        const linha = document.createElement("tr");

        const celulaNome = document.createElement("td");
        celulaNome.textContent = aluno.nome;

        const celulaMatricula = document.createElement("td");
        celulaMatricula.textContent = aluno.matricula;

        const celulaAcoes = document.createElement("td");

        const botaoEditar = document.createElement("button");
        botaoEditar.textContent = "Editar";
        botaoEditar.className = "btn btn-outline-primary btn-sm me-2";

        botaoEditar.addEventListener("click", function () {
            const novoNome = prompt("Digite o novo nome:", aluno.nome);

            if (novoNome === null) {
                return;
            }

            const nomeAtualizado = novoNome.trim();

            if (nomeAtualizado === "") {
                mostrarNotificacao("O nome não pode ficar vazio!", "erro");
                return;
            }

            const novaMatricula = prompt(
                "Digite a nova matrícula:",
                aluno.matricula
            );

            if (novaMatricula === null) {
                return;
            }

            const matriculaAtualizada = novaMatricula.trim();

            if (matriculaAtualizada === "") {
                mostrarNotificacao("A matrícula não pode ficar vazia!", "erro");
                return;
            }

            const matriculaExiste = alunos.some(function (outroAluno, outroIndice) {
                return outroIndice !== indice &&
                    outroAluno.matricula === matriculaAtualizada;
            });

            if (matriculaExiste) {
                mostrarNotificacao("Esta matrícula já está cadastrada!", "erro");
                return;
            }

            const matriculaAntiga = aluno.matricula;
            const relatorioSelecionado = alunoRelatorio.value === matriculaAntiga;

            aluno.nome = nomeAtualizado;
            aluno.matricula = matriculaAtualizada;

            frequencias.forEach(function (registro) {
                if (registro.matricula === matriculaAntiga) {
                    registro.matricula = matriculaAtualizada;
                }
            });

            salvarAlunos();
            salvarFrequencias();
            atualizarTabela();
            atualizarListaRelatorio();

            if (relatorioSelecionado) {
                alunoRelatorio.value = matriculaAtualizada;
                atualizarRelatorio();
            }

            atualizarAlertas();
            atualizarHistorico();

            mostrarNotificacao("Aluno atualizado com sucesso!");
        });

        const botaoExcluir = document.createElement("button");
        botaoExcluir.textContent = "Excluir";
        botaoExcluir.className = "btn btn-danger btn-sm";

        botaoExcluir.addEventListener("click", function () {
            const confirmarExclusao = confirm(
                `Tem certeza que deseja excluir o aluno ${aluno.nome}?`
            );

            if (!confirmarExclusao) {
                return;
            }

            frequencias = frequencias.filter(function (registro) {
                return registro.matricula !== aluno.matricula;
            });

            alunos.splice(indice, 1);

            salvarAlunos();
            salvarFrequencias();
            atualizarTabela();
            atualizarListaRelatorio();
            atualizarFrequenciaMedia();
            atualizarAlertas();
            atualizarHistorico();

            mostrarNotificacao("Aluno excluído com sucesso!");
        });

        celulaAcoes.appendChild(botaoEditar);
        celulaAcoes.appendChild(botaoExcluir);

        linha.appendChild(celulaNome);
        linha.appendChild(celulaMatricula);
        linha.appendChild(celulaAcoes);

        listaAlunos.appendChild(linha);
    });

    totalAlunos.textContent = alunos.length;
    atualizarListaFrequencia();
}

btnCadastrar.addEventListener("click", function () {
    const nome = nomeAluno.value.trim();
    const matricula = matriculaAluno.value.trim();

    if (nome === "" || matricula === "") {
        mostrarNotificacao("Preencha todos os campos!", "erro");
        return;
    }

    const matriculaExiste = alunos.some(function (aluno) {
        return aluno.matricula === matricula;
    });

    if (matriculaExiste) {
        mostrarNotificacao("Esta matrícula já está cadastrada!", "erro");
        return;
    }

    alunos.push({
        nome: nome,
        matricula: matricula
    });

    salvarAlunos();
    atualizarTabela();
    atualizarListaRelatorio();
    atualizarAlertas();

    nomeAluno.value = "";
    matriculaAluno.value = "";

    mostrarNotificacao("Aluno cadastrado com sucesso!");
});

pesquisaAluno.addEventListener("input", function () {
    atualizarTabela();
});

pesquisaHistorico.addEventListener("input", function () {
    atualizarHistorico();
});

filtroSituacao.addEventListener("change", function () {
    atualizarHistorico();
});

alunoRelatorio.addEventListener("change", function () {
    atualizarRelatorio();
});

function registrarFrequencia(status) {
    const matricula = alunoFrequencia.value;
    const data = dataFrequencia.value;

    if (matricula === "" || data === "") {
        mostrarNotificacao("Selecione um aluno e uma data!", "aviso");
        return;
    }

    const aluno = alunos.find(function (item) {
        return item.matricula === matricula;
    });

    if (!aluno) {
        mostrarNotificacao("Aluno não encontrado!", "erro");
        return;
    }

    const dataValida = /^\d{4}-\d{2}-\d{2}$/.test(data) &&
        !Number.isNaN(new Date(`${data}T12:00:00`).getTime()) &&
        new Date(`${data}T12:00:00`).toLocaleDateString("en-CA") === data;

    if (!dataValida) {
        mostrarNotificacao("Selecione uma data válida!", "erro");
        return;
    }

    const registroExiste = frequencias.some(function (registro) {
        return registro.matricula === matricula &&
            registro.data === data;
    });

    if (registroExiste) {
        mostrarNotificacao("Este aluno já possui um registro nesta data!", "aviso");
        return;
    }

    frequencias.push({
        matricula: matricula,
        data: data,
        status: status
    });

    salvarFrequencias();
    atualizarFrequenciaMedia();
    atualizarAlertas();
    atualizarHistorico();
    atualizarRelatorio();

    const dataFormatada = data.split("-").reverse().join("/");

    mostrarNotificacao(`${status} registrada para ${aluno.nome} em ${dataFormatada}!`);
}

btnPresente.addEventListener("click", function () {
    registrarFrequencia("Presença");
});

btnAusente.addEventListener("click", function () {
    registrarFrequencia("Ausência");
});

atualizarTabela();
atualizarListaRelatorio();
atualizarFrequenciaMedia();
atualizarAlertas();
atualizarHistorico();
