document.addEventListener("DOMContentLoaded", () => {
  if (document.getElementById("tabela-produtos")) inicializarListaProdutos();
  if (document.getElementById("btn-salvar-produto")) inicializarFormularioProduto();
});

/* =========================
   LISTAGEM (RF020)
========================= */

async function inicializarListaProdutos() {
  if (!ehGerente()) {
    document.getElementById("link-novo-produto")?.remove();
  }

  await carregarProdutos();

  const busca = document.getElementById("busca-produto");
  busca?.addEventListener("input", () => {
    const termo = normalizarBusca(busca.value);
    document.querySelectorAll("#tabela-produtos tr[data-id]").forEach((linha) => {
      linha.style.display = normalizarBusca(linha.innerText).includes(termo) ? "" : "none";
    });
  });
}

async function carregarProdutos() {
  const tabela = document.getElementById("tabela-produtos");
  const erroEl = document.getElementById("erro-produtos");

  try {
    const pagina = await apiFetch("/produtos?size=100");
    renderizarProdutos(pagina.content || []);
    document.getElementById("pag-info-produtos").textContent =
      `Exibindo ${pagina.numberOfElements} de ${pagina.totalElements} produtos`;
  } catch (erro) {
    exibirErro(erroEl, erro.message);
    tabela.innerHTML = `<tr><td colspan="5" class="empty-state">${erro.message}</td></tr>`;
  }
}

function renderizarProdutos(produtos) {
  const tabela = document.getElementById("tabela-produtos");
  const podeEditar = ehGerente();

  if (produtos.length === 0) {
    tabela.innerHTML = '<tr><td colspan="5" class="empty-state">Nenhum produto cadastrado ainda.</td></tr>';
    return;
  }

  tabela.innerHTML = produtos.map((produto) => `
    <tr data-id="${produto.id}">
      <td>
        <div class="cell-name">${escaparHtml(produto.nome)}</div>
        <div class="cell-sub">${escaparHtml(produto.codigo)}</div>
      </td>
      <td>${produto.tamanho === "UNICO" ? "Único" : produto.tamanho}</td>
      <td>${formatarMoedaBR(produto.preco)}</td>
      <td><span class="badge ${classeEstoque(produto.quantidadeEstoque)}">${produto.quantidadeEstoque} un.</span></td>
      <td>
        <div class="actions">
          ${podeEditar ? `
            <a href="editar-produto.html?id=${produto.id}"><button class="btn-edit">EDITAR</button></a>
            <button class="btn-del" onclick="excluirProduto(${produto.id})">EXCLUIR</button>
          ` : ""}
        </div>
      </td>
    </tr>
  `).join("");
}

function classeEstoque(quantidade) {
  if (quantidade <= 0) return "badge-out";
  if (quantidade <= 10) return "badge-low";
  return "badge-ok";
}

async function excluirProduto(id) {
  if (!confirm("Tem certeza que deseja excluir este produto?")) return;

  try {
    await apiFetch(`/produtos/${id}`, { method: "DELETE" });
    await carregarProdutos();
  } catch (erro) {
    alert(erro.message);
  }
}

/* =========================
   CRIAR / EDITAR (RF019 / RF021)
========================= */

async function inicializarFormularioProduto() {
  const idProduto = lerParametroUrl("id");
  const erroEl = document.getElementById("erro-produto");

  await carregarFornecedoresNoSelect();

  if (idProduto) {
    try {
      const produto = await apiFetch(`/produtos/${idProduto}`);
      document.getElementById("produto-codigo").value = produto.codigo;
      document.getElementById("produto-nome").value = produto.nome;
      document.getElementById("produto-descricao").value = produto.descricao || "";
      document.getElementById("produto-tamanho").value = produto.tamanho;
      document.getElementById("produto-cor").value = produto.cor || "";
      document.getElementById("produto-preco").value = produto.preco;
      document.getElementById("produto-estoque").value = produto.quantidadeEstoque;
      document.getElementById("produto-fornecedor").value = produto.fornecedorId;
    } catch (erro) {
      exibirErro(erroEl, erro.message);
      return;
    }
  }

  document.getElementById("btn-salvar-produto").addEventListener("click", () => salvarProduto(idProduto));
}

async function carregarFornecedoresNoSelect() {
  const select = document.getElementById("produto-fornecedor");

  try {
    const pagina = await apiFetch("/fornecedores?size=100");
    const fornecedores = pagina.content || [];

    if (fornecedores.length === 0) {
      select.innerHTML = '<option value="">Cadastre um fornecedor primeiro</option>';
      return;
    }

    select.innerHTML = fornecedores
      .map((fornecedor) => `<option value="${fornecedor.id}">${escaparHtml(fornecedor.nome)}</option>`)
      .join("");
  } catch (erro) {
    select.innerHTML = `<option value="">${erro.message}</option>`;
  }
}

async function salvarProduto(idProduto) {
  const erroEl = document.getElementById("erro-produto");
  exibirErro(erroEl, "");

  const corpo = {
    codigo: document.getElementById("produto-codigo").value.trim(),
    nome: document.getElementById("produto-nome").value.trim(),
    descricao: document.getElementById("produto-descricao").value.trim(),
    tamanho: document.getElementById("produto-tamanho").value,
    cor: document.getElementById("produto-cor").value.trim(),
    preco: Number(document.getElementById("produto-preco").value),
    quantidadeEstoque: Number(document.getElementById("produto-estoque").value),
    fornecedorId: Number(document.getElementById("produto-fornecedor").value)
  };

  try {
    if (idProduto) {
      await apiFetch(`/produtos/${idProduto}`, { method: "PUT", body: JSON.stringify(corpo) });
    } else {
      await apiFetch("/produtos", { method: "POST", body: JSON.stringify(corpo) });
    }
    window.location.href = "produtos.html";
  } catch (erro) {
    exibirErro(erroEl, erro.message);
  }
}
