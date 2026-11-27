(async function() {
  if (!await window.eduAlertaPaginaPronta) return;

  const $ = id => document.getElementById(id);
  let alertas = [];
  const inicio = new URLSearchParams(location.search).get("alertaId");

  function avisar(t, s = false) {
    const e = $("mensagemEncaminhamentos");
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

  function data(v) {
    return v ? String(v).split("-").reverse().join("/") : "—"
  }

  function alertaAtual() {
    return alertas.find(a => String(a.id) === $("selecaoAlerta").value)
  }

  async function carregarAlertas() {
    const s = $("filtroSituacaoAlerta").value;
    const p = new URLSearchParams();
    if (s) p.set("situacao", s);
    alertas = await requisicaoAPI(`/alertas${p.size ? `?${p}` : ""}`);
    const sel = $("selecaoAlerta"), atual = sel.value;
    sel.replaceChildren(new Option("Selecione um alerta", ""));
    alertas.forEach(a => sel.add(new Option(`#${a.id} · ${a.alunoNome} · ${a.periodoLetivoNome} · ${a.nivel}`, a.id)));
    if (inicio && alertas.some(a => String(a.id) === inicio)) sel.value = inicio;
    else if (alertas.some(a => String(a.id) === atual)) sel.value = atual;
    await carregarHistorico()
  }

  async function carregarHistorico() {
    const a = alertaAtual(), tb = $("listaEncaminhamentos");
    tb.replaceChildren();
    $("detalhesAlerta").textContent = a ? `${a.alunoNome} · ${a.periodoLetivoNome} · ${a.percentualFaltas}% · ${a.situacao}` : "";

    if (!a) {
      const tr = document.createElement("tr");
      cel(tr, "Selecione um alerta para consultar os encaminhamentos.").colSpan = 6;
      tb.append(tr);
      return
    }

    try {
      const itens = await requisicaoAPI(`/alertas/${a.id}/encaminhamentos`);

      if (!itens.length) {
        const tr = document.createElement("tr");
        cel(tr, "Nenhum encaminhamento registrado.").colSpan = 6;
        tb.append(tr);
        return
      }

      itens.forEach(e => {
        const tr = document.createElement("tr");
        cel(tr, data(e.dataEncaminhamento));
        cel(tr, e.servicoDestino);
        cel(tr, e.finalidade);
        cel(tr, e.observacao);
        cel(tr, e.status);
        const td = cel(tr, "");

        if (e.status !== "CONCLUIDO" && e.status !== "CANCELADO") {
          const sel = document.createElement("select");
          sel.className = "form-select form-select-sm";
          ["PENDENTE", "EM_ANDAMENTO", "CONCLUIDO", "CANCELADO"].forEach(x => sel.add(new Option(x, x)));
          sel.value = e.status;

          sel.onchange = async () => {
            try {
              await requisicaoAPI(`/alertas/${a.id}/encaminhamentos/${e.id}/status?status=${sel.value}`, { method: "PUT" });
              avisar("Situação atualizada.", true);
              await carregarHistorico()
            } catch (err) {
              avisar(err.message)
            }
          };

          td.append(sel)
        } else td.textContent = "Finalizado";

        tb.append(tr)
      })
    } catch (e) {
      avisar(e.message)
    }
  }

  $("formEncaminhamento").addEventListener("submit", async e => {
    e.preventDefault();
    const a = alertaAtual();

    if (!a) {
      avisar("Selecione um alerta.");
      return
    }

    const body = {
      servicoDestino: $("servicoDestino").value.trim(),
      finalidade: $("finalidade").value.trim(),
      observacao: $("observacao").value.trim(),
      dataEncaminhamento: $("dataEncaminhamento").value || null
    };

    try {
      await requisicaoAPI(`/alertas/${a.id}/encaminhamentos`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(body)
      });
      avisar("Encaminhamento registrado.", true);
      $("formEncaminhamento").reset();
      await carregarHistorico()
    } catch (err) {
      avisar(err.message)
    }
  });

  $("selecaoAlerta").addEventListener("change", carregarHistorico);
  $("filtroSituacaoAlerta").addEventListener("change", carregarAlertas);
  $("atualizarEncaminhamentos").onclick = carregarHistorico;

  try {
    await carregarAlertas()
  } catch (e) {
    avisar(e.message)
  }
})();
