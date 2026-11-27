(async function() {
    if (!await window.eduAlertaPaginaPronta) return;

    const $ = id => document.getElementById(id);
    let alertas = [];
    const inicial = new URLSearchParams(location.search).get("alertaId");

    function avisar(t, s = false) {
        const e = $("mensagemNotificacoes");
        e.replaceChildren();
        if (!t) return;
        const d = document.createElement("div");
        d.className = `alert alert-${s ? "success" : "danger"}`;
        d.textContent = t;
        e.append(d)
    }

    function cel(tr, v) {
        const td = document.createElement("td");
        td.textContent = v ?? "—";
        tr.append(td);
        return td
    }

    function dh(v) {
        if (!v) return "—";
        const [d, h] = String(v).split("T");
        return d.split("-").reverse().join("/") + (h ? ` ${h.slice(0, 5)}` : "")
    }

    function atual() {
        return alertas.find(a => String(a.id) === $("selecaoAlerta").value)
    }

    async function carregarAlertas() {
        const p = new URLSearchParams();
        if ($("filtroSituacao").value) p.set("situacao", $("filtroSituacao").value);
        alertas = await requisicaoAPI(`/alertas${p.size ? `?${p}` : ""}`);
        const sel = $("selecaoAlerta"), old = sel.value;
        sel.replaceChildren(new Option("Selecione um alerta", ""));
        alertas.forEach(a => sel.add(new Option(`#${a.id} · ${a.alunoNome} · ${a.periodoLetivoNome}`, a.id)));
        if (inicial && alertas.some(a => String(a.id) === inicial)) sel.value = inicial;
        else if (alertas.some(a => String(a.id) === old)) sel.value = old;
        await carregarHistorico()
    }

    async function carregarHistorico() {
        const a = atual(), tb = $("listaNotificacoes");
        tb.replaceChildren();

        if (!a) {
            const tr = document.createElement("tr");
            cel(tr, "Selecione um alerta para consultar as notificações.").colSpan = 6;
            tb.append(tr);
            return
        }

        try {
            const itens = await requisicaoAPI(`/alertas/${a.id}/notificacoes`);

            if (!itens.length) {
                const tr = document.createElement("tr");
                cel(tr, "Nenhuma notificação registrada para este alerta.").colSpan = 6;
                tb.append(tr);
                return
            }

            itens.forEach(n => {
                const tr = document.createElement("tr");
                cel(tr, n.responsavelNome);
                cel(tr, n.emailDestino);
                const status = cel(tr, "");
                const badge = document.createElement("span");
                badge.className = `badge ${n.status === "ENVIADA" ? "bg-success" : "bg-danger"}`;
                badge.textContent = n.status;
                status.append(badge);
                cel(tr, dh(n.criadoEm));
                cel(tr, dh(n.enviadoEm));
                const td = cel(tr, "");

                if (n.status === "FALHA") {
                    const erro = document.createElement("div");
                    erro.className = "small text-danger mb-2";
                    erro.textContent = n.erroEnvio || "Falha sem detalhe retornado";
                    td.append(erro);
                    const b = document.createElement("button");
                    b.type = "button";
                    b.className = "btn btn-outline-primary btn-sm";
                    b.textContent = "Tentar novamente";

                    b.onclick = async () => {
                        b.disabled = true;

                        try {
                            const resultado = await requisicaoAPI(`/notificacoes/${n.id}/reenviar`, { method: "POST" });

                            if (resultado.status === "ENVIADA") avisar("Notificação reenviada com sucesso.", true);
                            else avisar(resultado.erroEnvio || "O reenvio falhou. Consulte a configuração SMTP.");

                            await carregarHistorico()
                        } catch (e) {
                            avisar(e.message);
                            b.disabled = false
                        }
                    };

                    td.append(b)
                } else td.textContent = "Enviado";

                tb.append(tr)
            })
        } catch (e) {
            avisar(e.message)
        }
    }

    $("selecaoAlerta").addEventListener("change", carregarHistorico);
    $("filtroSituacao").addEventListener("change", carregarAlertas);
    $("atualizarNotificacoes").onclick = carregarHistorico;

    try {
        await carregarAlertas()
    } catch (e) {
        avisar(e.message)
    }
})();
