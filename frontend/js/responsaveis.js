(async function() {
    if (!await window.eduAlertaPaginaPronta) return;

    const $ = id => document.getElementById(id);
    const msg = $("mensagemResponsaveis"), form = $("formResponsavel"), assocForm = $("formAssociacao");
    let responsaveis = [], alunos = [], editando = null;

    function avisar(t, s = false) {
        msg.replaceChildren();
        if (!t) return;
        const d = document.createElement("div");
        d.className = `alert alert-${s ? "success" : "danger"}`;
        d.textContent = t;
        msg.append(d)
    }

    function cel(l, v) {
        const td = document.createElement("td");
        td.textContent = v ?? "—";
        l.append(td);
        return td
    }

    function preencher(sel, arr, leg, rotulo) {
        sel.replaceChildren(new Option(leg, ""));
        arr.forEach(x => sel.add(new Option(rotulo(x), x.id)))
    }

    async function carregar() {
        try {
            [responsaveis, alunos] = await Promise.all([requisicaoAPI("/responsaveis"), requisicaoAPI("/alunos")]);
            renderResponsaveis();
            preencher($("responsavelAssociacao"), responsaveis.filter(x => x.ativo), "Selecione um responsável", x => `${x.nome} (${x.email})`);
            preencher($("alunoAssociacao"), alunos, "Selecione um aluno", x => x.nome);
            if ($("alunoAssociacao").value) await carregarAssociacoes()
        } catch (e) {
            avisar(e.message)
        }
    }

    function renderResponsaveis() {
        const tb = $("listaResponsaveis");
        tb.replaceChildren();

        if (!responsaveis.length) {
            const tr = document.createElement("tr");
            cel(tr, "Nenhum responsável cadastrado.").colSpan = 6;
            tb.append(tr);
            return
        }

        responsaveis.forEach(r => {
            const tr = document.createElement("tr");
            cel(tr, r.nome);
            cel(tr, r.email);
            cel(tr, r.telefone);
            cel(tr, r.parentesco);
            cel(tr, r.ativo ? "Ativo" : "Inativo");
            const td = cel(tr, "");
            const editar = document.createElement("button");
            editar.className = "btn btn-outline-primary btn-sm me-2";
            editar.textContent = "Editar";
            editar.onclick = () => editarResponsavel(r);
            td.append(editar);
            const status = document.createElement("button");
            status.className = `btn btn-sm ${r.ativo ? "btn-outline-danger" : "btn-outline-success"}`;
            status.textContent = r.ativo ? "Desativar" : "Ativar";

            status.onclick = async () => {
                try {
                    await requisicaoAPI(`/responsaveis/${r.id}/status?ativo=${!r.ativo}`, { method: "PUT" });
                    avisar("Situação atualizada.", true);
                    await carregar()
                } catch (e) {
                    avisar(e.message)
                }
            };

            td.append(status);
            tb.append(tr)
        })
    }

    function editarResponsavel(r) {
        editando = r.id;
        $("tituloResponsavel").textContent = "Editar responsável";
        $("nomeResponsavel").value = r.nome;
        $("emailResponsavel").value = r.email;
        $("telefoneResponsavel").value = r.telefone || "";
        $("parentescoResponsavel").value = r.parentesco || "";
        $("cancelarEdicaoResponsavel").hidden = false;
        $("salvarResponsavel").textContent = "Salvar alterações";
        window.scrollTo({ top: 0, behavior: "smooth" })
    }

    function limparForm() {
        editando = null;
        form.reset();
        $("tituloResponsavel").textContent = "Cadastrar responsável";
        $("cancelarEdicaoResponsavel").hidden = true;
        $("salvarResponsavel").textContent = "Salvar responsável"
    }

    form.addEventListener("submit", async e => {
        e.preventDefault();
        const body = {
            nome: $("nomeResponsavel").value.trim(),
            email: $("emailResponsavel").value.trim(),
            telefone: $("telefoneResponsavel").value.trim() || null,
            parentesco: $("parentescoResponsavel").value.trim() || null
        };

        try {
            await requisicaoAPI(editando ? `/responsaveis/${editando}` : "/responsaveis", {
                method: editando ? "PUT" : "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify(body)
            });
            avisar(editando ? "Responsável atualizado." : "Responsável cadastrado.", true);
            limparForm();
            await carregar()
        } catch (e) {
            avisar(e.message)
        }
    });

    async function carregarAssociacoes() {
        const alunoId = $("alunoAssociacao").value, tb = $("listaAssociacoes");
        tb.replaceChildren();

        if (!alunoId) {
            const tr = document.createElement("tr");
            cel(tr, "Selecione um aluno.").colSpan = 5;
            tb.append(tr);
            return
        }

        try {
            const itens = await requisicaoAPI(`/responsaveis/alunos/${alunoId}`);

            if (!itens.length) {
                const tr = document.createElement("tr");
                cel(tr, "Nenhum responsável associado.").colSpan = 5;
                tb.append(tr);
                return
            }

            itens.forEach(a => {
                const tr = document.createElement("tr");
                cel(tr, a.responsavelNome);
                cel(tr, a.email);
                cel(tr, a.parentesco);
                cel(tr, a.responsavelPrincipal ? "Sim" : "Não");
                const td = cel(tr, "");
                const b = document.createElement("button");
                b.className = "btn btn-outline-danger btn-sm";
                b.textContent = "Desassociar";

                b.onclick = async () => {
                    if (!confirm(`Desassociar ${a.responsavelNome} deste aluno?`)) return;

                    try {
                        await requisicaoAPI(`/responsaveis/alunos/${alunoId}/${a.responsavelId}`, { method: "DELETE" });
                        avisar("Associação removida.", true);
                        await carregarAssociacoes()
                    } catch (e) {
                        avisar(e.message)
                    }
                };

                td.append(b);
                tb.append(tr)
            })
        } catch (e) {
            avisar(e.message)
        }
    }

    assocForm.addEventListener("submit", async e => {
        e.preventDefault();

        try {
            await requisicaoAPI(`/responsaveis/alunos/${$("alunoAssociacao").value}`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({
                    responsavelId: Number($("responsavelAssociacao").value),
                    responsavelPrincipal: $("responsavelPrincipal").checked
                })
            });
            avisar("Responsável associado ao aluno.", true);
            $("responsavelPrincipal").checked = false;
            await carregarAssociacoes()
        } catch (e) {
            avisar(e.message)
        }
    });

    $("alunoAssociacao").addEventListener("change", carregarAssociacoes);
    $("atualizarAssociacoes").onclick = carregarAssociacoes;
    $("atualizarResponsaveis").onclick = carregar;
    $("cancelarEdicaoResponsavel").onclick = limparForm;

    await carregar();
})();
