const paginasEduAlerta = [
    ["painel.html", "Painel", "bi-grid-1x2-fill"],
    ["alunos.html", "Alunos", "bi-people-fill"],
    ["turmas.html", "Turmas", "bi-collection-fill"],
    ["matriculas.html", "Matrículas", "bi-journal-check"],
    ["frequencia.html", "Frequência", "bi-calendar-check"],
    ["alertas.html", "Alertas", "bi-exclamation-triangle-fill"],
    ["acompanhamentos.html", "Acompanhamentos", "bi-clipboard-heart"],
    ["relatorios.html", "Relatórios", "bi-bar-chart-fill"],
    ["usuarios.html", "Usuários", "bi-person-gear"]
];

function montarLayout() {
    const atual = window.location.pathname.split("/").pop();
    const cabecalho = document.getElementById("cabecalho");
    const navegacao = document.getElementById("navegacao");

    cabecalho.innerHTML = `
        <div class="app-topbar">
            <div class="container d-flex align-items-center justify-content-between gap-3">
                <div>
                    <h1><i class="bi bi-mortarboard-fill me-2"></i>EduAlerta</h1>
                    <p>Sistema de Monitoramento de Frequência Escolar</p>
                </div>
                <button id="sair" type="button" class="btn btn-light btn-sm">
                    <i class="bi bi-box-arrow-right me-1"></i>Sair
                </button>
            </div>
        </div>
    `;

    const links = paginasEduAlerta.map(([arquivo, titulo, icone]) => {
        const ativo = atual === arquivo ? " active" : "";
        const aria = atual === arquivo ? ' aria-current="page"' : "";

        return `
            <a class="navegacao-link${ativo}" href="${arquivo}"${aria}>
                <i class="bi ${icone}"></i>${titulo}
            </a>
        `;
    }).join("");

    navegacao.innerHTML = `
        <nav class="navegacao-principal app-nav container" aria-label="Navegação principal">
            <div class="navegacao-links">${links}</div>
        </nav>
    `;

    document.getElementById("sair").addEventListener("click", sair);
}

async function iniciarPagina() {
    const autorizado = await protegerPagina();

    if (autorizado) {
        montarLayout();
    }
}

iniciarPagina();