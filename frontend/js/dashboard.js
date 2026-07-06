document.addEventListener("DOMContentLoaded", () => {
  exibirDataHoje();
  carregarDashboard();
});

function exibirDataHoje() {
  const elemento = document.getElementById("data-hoje");
  if (!elemento) return;

  elemento.textContent = new Date().toLocaleDateString("pt-BR", {
    day: "2-digit",
    month: "long",
    year: "numeric"
  });
}

async function carregarDashboard() {
  const tabela = document.getElementById("tabela-ultimas-vendas");

  try {
    const dashboard = await apiFetch("/dashboard");

    document.getElementById("stat-total-produtos").textContent = dashboard.totalProdutos;
    document.getElementById("stat-vendas-pendentes").textContent = dashboard.vendasPendentes;
    document.getElementById("stat-vendas-enviadas").textContent = dashboard.vendasEnviadas;
    document.getElementById("stat-vendas-entregues").textContent = dashboard.vendasEntregues;

    renderizarUltimasVendas(dashboard.ultimasVendas || []);
  } catch (erro) {
    tabela.innerHTML = `<tr><td colspan="5" class="empty-state">${erro.message}</td></tr>`;
  }
}

function renderizarUltimasVendas(vendas) {
  const tabela = document.getElementById("tabela-ultimas-vendas");

  if (vendas.length === 0) {
    tabela.innerHTML = '<tr><td colspan="5" class="empty-state">Nenhuma venda registrada ainda.</td></tr>';
    return;
  }

  tabela.innerHTML = vendas.map((venda) => {
    const status = statusVendaInfo(venda.status);
    const entrega = [venda.cidadeEntrega, venda.estadoEntrega].filter(Boolean).join("/") || "-";

    return `
      <tr>
        <td>#${String(venda.id).padStart(4, "0")}</td>
        <td>${entrega}</td>
        <td>${venda.quantidadeTotal} ${venda.quantidadeTotal === 1 ? "item" : "itens"}</td>
        <td>${formatarMoedaBR(venda.valorTotal)}</td>
        <td><span class="badge ${status.classe}">${status.label}</span></td>
      </tr>
    `;
  }).join("");
}
