document.addEventListener("DOMContentLoaded", () => {
  if (document.getElementById("tabela-fornecedores")) inicializarListaFornecedores();
  if (document.getElementById("btn-salvar-fornecedor")) inicializarFormularioFornecedor();
});

/* =========================
   LISTAGEM (RF002)
========================= */

async function inicializarListaFornecedores() {
  await carregarFornecedores();

  const busca = document.getElementById("busca-fornecedor");
  busca?.addEventListener("input", () => {
    const termo = busca.value.toLowerCase();
    document.querySelectorAll("#tabela-fornecedores tr[data-id]").forEach((linha) => {
      linha.style.display = linha.innerText.toLowerCase().includes(termo) ? "" : "none";
    });
  });
}

async function carregarFornecedores() {
  const tabela = document.getElementById("tabela-fornecedores");
  const erroEl = document.getElementById("erro-fornecedores");

  try {
    const pagina = await apiFetch("/fornecedores?size=100");
    renderizarFornecedores(pagina.content || []);
    document.getElementById("pag-info-fornecedores").textContent =
      `Exibindo ${pagina.numberOfElements} de ${pagina.totalElements} fornecedores`;
  } catch (erro) {
    exibirErro(erroEl, erro.message);
    tabela.innerHTML = `<tr><td colspan="4" class="empty-state">${erro.message}</td></tr>`;
  }
}

function renderizarFornecedores(fornecedores) {
  const tabela = document.getElementById("tabela-fornecedores");

  if (fornecedores.length === 0) {
    tabela.innerHTML = '<tr><td colspan="4" class="empty-state">Nenhum fornecedor cadastrado ainda.</td></tr>';
    return;
  }

  tabela.innerHTML = fornecedores.map((fornecedor) => `
    <tr data-id="${fornecedor.id}">
      <td>${escaparHtml(fornecedor.nome)}</td>
      <td>${formatarCnpj(fornecedor.cnpj)}</td>
      <td>${escaparHtml(fornecedor.email)}</td>
      <td>
        <div class="actions">
          <a href="editar-fornecedor.html?id=${fornecedor.id}"><button class="btn-edit">EDITAR</button></a>
          <button class="btn-del" onclick="excluirFornecedor(${fornecedor.id})">EXCLUIR</button>
        </div>
      </td>
    </tr>
  `).join("");
}

async function excluirFornecedor(id) {
  if (!confirm("Tem certeza que deseja excluir este fornecedor?")) return;

  try {
    await apiFetch(`/fornecedores/${id}`, { method: "DELETE" });
    await carregarFornecedores();
  } catch (erro) {
    alert(erro.message);
  }
}

/* =========================
   CRIAR / EDITAR (RF001 / RF003)
========================= */

async function inicializarFormularioFornecedor() {
  const idFornecedor = lerParametroUrl("id");
  const erroEl = document.getElementById("erro-fornecedor");

  preencherSelectUf(document.getElementById("fornecedor-estado"));

  if (idFornecedor) {
    try {
      const fornecedor = await apiFetch(`/fornecedores/${idFornecedor}`);
      document.getElementById("fornecedor-nome").value = fornecedor.nome;
      document.getElementById("fornecedor-cnpj").value = fornecedor.cnpj;
      document.getElementById("fornecedor-telefone").value = fornecedor.telefone || "";
      document.getElementById("fornecedor-email").value = fornecedor.email;
      document.getElementById("fornecedor-cidade").value = fornecedor.cidade || "";
      document.getElementById("fornecedor-estado").value = fornecedor.estado || "";
      document.getElementById("fornecedor-pais").value = fornecedor.pais || "Brasil";
    } catch (erro) {
      exibirErro(erroEl, erro.message);
      return;
    }
  }

  document.getElementById("btn-salvar-fornecedor").addEventListener("click", () => salvarFornecedor(idFornecedor));
}

async function salvarFornecedor(idFornecedor) {
  const erroEl = document.getElementById("erro-fornecedor");
  exibirErro(erroEl, "");

  const corpo = {
    nome: document.getElementById("fornecedor-nome").value.trim(),
    cnpj: document.getElementById("fornecedor-cnpj").value.replace(/\D/g, ""),
    telefone: document.getElementById("fornecedor-telefone").value.trim(),
    email: document.getElementById("fornecedor-email").value.trim(),
    cidade: document.getElementById("fornecedor-cidade").value.trim(),
    estado: document.getElementById("fornecedor-estado").value,
    pais: document.getElementById("fornecedor-pais").value.trim()
  };

  try {
    if (idFornecedor) {
      await apiFetch(`/fornecedores/${idFornecedor}`, { method: "PUT", body: JSON.stringify(corpo) });
    } else {
      await apiFetch("/fornecedores", { method: "POST", body: JSON.stringify(corpo) });
    }
    window.location.href = "fornecedores.html";
  } catch (erro) {
    exibirErro(erroEl, erro.message);
  }
}
