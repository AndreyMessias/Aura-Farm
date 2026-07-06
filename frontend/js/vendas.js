document.addEventListener("DOMContentLoaded", () => {
  if (document.getElementById("tabela-vendas")) inicializarListaVendas();
  if (document.getElementById("lista-produtos-venda")) inicializarFormularioVenda();
});

/* =========================
   LISTAGEM (RF014)
========================= */

async function inicializarListaVendas() {
  await carregarVendas();

  const busca = document.getElementById("busca-venda");
  busca?.addEventListener("input", () => {
    const termo = busca.value.toLowerCase();
    document.querySelectorAll("#tabela-vendas tr[data-id]").forEach((linha) => {
      linha.style.display = linha.innerText.toLowerCase().includes(termo) ? "" : "none";
    });
  });
}

async function carregarVendas() {
  const tabela = document.getElementById("tabela-vendas");
  const erroEl = document.getElementById("erro-vendas");

  try {
    const pagina = await apiFetch("/vendas?size=100");
    renderizarVendas(pagina.content || []);
    document.getElementById("pag-info-vendas").textContent =
      `Exibindo ${pagina.numberOfElements} de ${pagina.totalElements} vendas`;
  } catch (erro) {
    exibirErro(erroEl, erro.message);
    tabela.innerHTML = `<tr><td colspan="7" class="empty-state">${erro.message}</td></tr>`;
  }
}

function renderizarVendas(vendas) {
  const tabela = document.getElementById("tabela-vendas");

  if (vendas.length === 0) {
    tabela.innerHTML = '<tr><td colspan="7" class="empty-state">Nenhuma venda registrada ainda.</td></tr>';
    return;
  }

  tabela.innerHTML = vendas.map((venda) => {
    const status = statusVendaInfo(venda.status);
    const entrega = [venda.cidadeEntrega, venda.estadoEntrega].filter(Boolean).join("/") || "-";

    return `
      <tr data-id="${venda.id}">
        <td>#${String(venda.id).padStart(4, "0")}</td>
        <td>${formatarDataBR(venda.dataPedido)}</td>
        <td>${entrega}</td>
        <td>${venda.quantidadeTotal} ${venda.quantidadeTotal === 1 ? "item" : "itens"}</td>
        <td>${formatarMoedaBR(venda.valorTotal)}</td>
        <td><span class="badge ${status.classe}">${status.label}</span></td>
        <td>
          <div class="actions">
            <button class="btn-edit" onclick="alterarStatusVenda(${venda.id})">STATUS</button>
            <a href="editar-venda.html?id=${venda.id}"><button class="btn-edit">EDITAR</button></a>
            <button class="btn-del" onclick="cancelarVenda(${venda.id})">CANCELAR</button>
          </div>
        </td>
      </tr>
    `;
  }).join("");
}

async function alterarStatusVenda(id) {
  const escolha = prompt("Escolha o novo status:\n\n1 - Pendente\n2 - Enviado\n3 - Entregue\n4 - Cancelado");
  if (escolha === null) return;

  const mapa = { "1": "PENDENTE", "2": "ENVIADO", "3": "ENTREGUE", "4": "CANCELADO" };
  const status = mapa[escolha.trim()];

  if (!status) {
    alert("Opção inválida.");
    return;
  }

  try {
    await apiFetch(`/vendas/${id}/status`, { method: "PATCH", body: JSON.stringify({ status }) });
    await carregarVendas();
  } catch (erro) {
    alert(erro.message);
  }
}

async function cancelarVenda(id) {
  if (!confirm("Tem certeza que deseja cancelar esta venda?")) return;

  try {
    await apiFetch(`/vendas/${id}/status`, { method: "PATCH", body: JSON.stringify({ status: "CANCELADO" }) });
    await carregarVendas();
  } catch (erro) {
    alert(erro.message);
  }
}

/* =========================
   FORMULÁRIO (RF013 / RF016)
   Compartilhado entre nova-venda.html e editar-venda.html
========================= */

let produtosCatalogo = [];
let itensSelecionados = {}; // codigoProduto -> quantidade

async function inicializarFormularioVenda() {
  const idVenda = lerParametroUrl("id");
  const erroEl = document.getElementById("erro-venda");

  preencherSelectUf(document.getElementById("venda-estado"));

  try {
    const pagina = await apiFetch("/produtos?size=200");
    produtosCatalogo = pagina.content || [];
  } catch (erro) {
    exibirErro(erroEl, erro.message);
    return;
  }

  if (idVenda) {
    try {
      const venda = await apiFetch(`/vendas/${idVenda}`);
      venda.itens.forEach((item) => {
        itensSelecionados[item.codigoProduto] = item.quantidade;
      });
      preencherEndereco(venda.endereco);
    } catch (erro) {
      exibirErro(erroEl, erro.message);
      return;
    }
  }

  renderizarSeletorProdutos();

  document.getElementById("btn-confirmar-venda").addEventListener("click", () => salvarVenda(idVenda));
}

function preencherEndereco(endereco) {
  document.getElementById("venda-rua").value = endereco.rua;
  document.getElementById("venda-numero").value = endereco.numero;
  document.getElementById("venda-bairro").value = endereco.bairro;
  document.getElementById("venda-cep").value = endereco.cep;
  document.getElementById("venda-complemento").value = endereco.complemento || "";
  document.getElementById("venda-cidade").value = endereco.cidade;
  document.getElementById("venda-estado").value = endereco.estado;
}

function renderizarSeletorProdutos() {
  const container = document.getElementById("lista-produtos-venda");

  if (produtosCatalogo.length === 0) {
    container.innerHTML = '<p class="empty-state">Nenhum produto cadastrado. Cadastre produtos antes de registrar uma venda.</p>';
    return;
  }

  container.innerHTML = produtosCatalogo.map((produto) => {
    const marcado = itensSelecionados[produto.codigo] !== undefined;
    const quantidade = itensSelecionados[produto.codigo] || 0;
    const tamanho = produto.tamanho === "UNICO" ? "Único" : produto.tamanho;

    return `
      <div class="product-item" data-codigo="${produto.codigo}" data-preco="${produto.preco}">
        <div class="product-check">
          <input type="checkbox" ${marcado ? "checked" : ""} onchange="alternarProdutoSelecionado(this)">
          <div>
            <div class="product-info-name">${escaparHtml(produto.nome)}</div>
            <div class="product-info-price">${formatarMoedaBR(produto.preco)} — ${tamanho}</div>
          </div>
        </div>
        <div class="qty-control">
          <button type="button" class="qty-btn" onclick="alterarQuantidade('${produto.codigo}', -1)">-</button>
          <div class="qty-val">${quantidade}</div>
          <button type="button" class="qty-btn" onclick="alterarQuantidade('${produto.codigo}', 1)">+</button>
        </div>
      </div>
    `;
  }).join("");

  atualizarResumoVenda();
}

function alternarProdutoSelecionado(checkbox) {
  const item = checkbox.closest(".product-item");
  const codigo = item.dataset.codigo;
  const valorEl = item.querySelector(".qty-val");

  if (checkbox.checked) {
    itensSelecionados[codigo] = 1;
    valorEl.textContent = 1;
  } else {
    delete itensSelecionados[codigo];
    valorEl.textContent = 0;
  }

  atualizarResumoVenda();
}

function alterarQuantidade(codigo, delta) {
  const item = document.querySelector(`.product-item[data-codigo="${codigo}"]`);
  const valorEl = item.querySelector(".qty-val");
  const checkbox = item.querySelector('input[type="checkbox"]');

  let valor = Number(valorEl.textContent) + delta;
  if (valor < 0) valor = 0;
  valorEl.textContent = valor;

  if (valor === 0) {
    checkbox.checked = false;
    delete itensSelecionados[codigo];
  } else {
    checkbox.checked = true;
    itensSelecionados[codigo] = valor;
  }

  atualizarResumoVenda();
}

function atualizarResumoVenda() {
  const resumoEl = document.getElementById("resumo-itens-venda");
  const totalEl = document.getElementById("resumo-total-venda");

  let total = 0;
  const linhas = [];

  Object.entries(itensSelecionados).forEach(([codigo, quantidade]) => {
    const produto = produtosCatalogo.find((p) => p.codigo === codigo);
    if (!produto) return;

    const subtotal = produto.preco * quantidade;
    total += subtotal;

    linhas.push(`
      <div class="summary-item">
        <div class="summary-item-name">${escaparHtml(produto.nome)} x${quantidade}</div>
        <div class="summary-item-val">${formatarMoedaBR(subtotal)}</div>
      </div>
    `);
  });

  resumoEl.innerHTML = linhas.join("") ||
    '<p style="color:rgba(255,255,255,0.5); font-size:12px;">Nenhum produto selecionado.</p>';
  totalEl.textContent = formatarMoedaBR(total);
}

async function salvarVenda(idVenda) {
  const erroEl = document.getElementById("erro-venda");
  exibirErro(erroEl, "");

  const itens = Object.entries(itensSelecionados).map(([codigoProduto, quantidade]) => ({
    codigoProduto,
    quantidade
  }));

  if (itens.length === 0) {
    exibirErro(erroEl, "Quantidade de produtos deve ser maior que zero. Selecione ao menos um produto.");
    return;
  }

  const endereco = {
    rua: document.getElementById("venda-rua").value.trim(),
    numero: document.getElementById("venda-numero").value.trim(),
    bairro: document.getElementById("venda-bairro").value.trim(),
    cep: document.getElementById("venda-cep").value.replace(/\D/g, ""),
    complemento: document.getElementById("venda-complemento").value.trim(),
    cidade: document.getElementById("venda-cidade").value.trim(),
    estado: document.getElementById("venda-estado").value
  };

  try {
    if (idVenda) {
      await apiFetch(`/vendas/${idVenda}`, { method: "PUT", body: JSON.stringify({ itens, endereco }) });
    } else {
      await apiFetch("/vendas", { method: "POST", body: JSON.stringify({ itens, endereco }) });
    }
    window.location.href = "vendas.html";
  } catch (erro) {
    exibirErro(erroEl, erro.message);
  }
}
