document.addEventListener("DOMContentLoaded", () => {
  if (document.getElementById("tabela-funcionarios")) inicializarListaFuncionarios();
  if (document.getElementById("btn-salvar-funcionario")) inicializarFormularioFuncionario();
});

/* =========================
   LISTAGEM (RF006)
========================= */

async function inicializarListaFuncionarios() {
  await carregarFuncionarios();

  const busca = document.getElementById("busca-funcionario");
  busca?.addEventListener("input", () => {
    const termo = busca.value.toLowerCase();
    document.querySelectorAll("#tabela-funcionarios tr[data-id]").forEach((linha) => {
      linha.style.display = linha.innerText.toLowerCase().includes(termo) ? "" : "none";
    });
  });
}

async function carregarFuncionarios() {
  const tabela = document.getElementById("tabela-funcionarios");
  const erroEl = document.getElementById("erro-funcionarios");

  try {
    const pagina = await apiFetch("/usuarios?size=100");
    renderizarFuncionarios(pagina.content || []);
    document.getElementById("pag-info-funcionarios").textContent =
      `Exibindo ${pagina.numberOfElements} de ${pagina.totalElements} funcionários`;
  } catch (erro) {
    exibirErro(erroEl, erro.message);
    tabela.innerHTML = `<tr><td colspan="5" class="empty-state">${erro.message}</td></tr>`;
  }
}

function renderizarFuncionarios(funcionarios) {
  const tabela = document.getElementById("tabela-funcionarios");
  const usuarioLogado = getUsuarioLogado();

  if (funcionarios.length === 0) {
    tabela.innerHTML = '<tr><td colspan="5" class="empty-state">Nenhum funcionário cadastrado ainda.</td></tr>';
    return;
  }

  tabela.innerHTML = funcionarios.map((funcionario) => `
    <tr data-id="${funcionario.id}">
      <td>${escaparHtml(funcionario.nome)}</td>
      <td>${formatarCpf(funcionario.cpf)}</td>
      <td>${escaparHtml(funcionario.email)}</td>
      <td>${cargoLabel(funcionario.cargo)}</td>
      <td>
        <div class="actions">
          <a href="editar-funcionario.html?id=${funcionario.id}"><button class="btn-edit">EDITAR</button></a>
          ${funcionario.email !== usuarioLogado?.email
            ? `<button class="btn-del" onclick="demitirFuncionario(${funcionario.id})">DEMITIR</button>`
            : ""}
        </div>
      </td>
    </tr>
  `).join("");
}

async function demitirFuncionario(id) {
  if (!confirm("Tem certeza que deseja demitir este funcionário?")) return;

  try {
    await apiFetch(`/usuarios/${id}`, { method: "DELETE" });
    await carregarFuncionarios();
  } catch (erro) {
    alert(erro.message);
  }
}

/* =========================
   CRIAR / EDITAR (RF005 / RF007)
========================= */

async function inicializarFormularioFuncionario() {
  const idFuncionario = lerParametroUrl("id");
  const erroEl = document.getElementById("erro-funcionario");

  preencherSelectUf(document.getElementById("funcionario-estado"));

  if (idFuncionario) {
    try {
      const funcionario = await apiFetch(`/usuarios/${idFuncionario}`);
      document.getElementById("funcionario-nome").value = funcionario.nome;
      document.getElementById("funcionario-cpf").value = formatarCpf(funcionario.cpf);
      document.getElementById("funcionario-email").value = funcionario.email;
      document.getElementById("funcionario-telefone").value = funcionario.telefone || "";
      document.getElementById("funcionario-cargo").value = funcionario.cargo;
      document.getElementById("funcionario-cidade").value = funcionario.cidade || "";
      document.getElementById("funcionario-estado").value = funcionario.estado || "";
      document.getElementById("funcionario-pais").value = funcionario.pais || "Brasil";

      // Guarda os valores reais (nao-formatados) de cpf/email, que ficam desabilitados na tela
      document.getElementById("funcionario-cpf").dataset.valorReal = funcionario.cpf;
      document.getElementById("funcionario-email").dataset.valorReal = funcionario.email;
    } catch (erro) {
      exibirErro(erroEl, erro.message);
      return;
    }
  }

  document.getElementById("btn-salvar-funcionario").addEventListener("click", () => salvarFuncionario(idFuncionario));
}

async function salvarFuncionario(idFuncionario) {
  const erroEl = document.getElementById("erro-funcionario");
  exibirErro(erroEl, "");

  const campoCpf = document.getElementById("funcionario-cpf");
  const campoEmail = document.getElementById("funcionario-email");

  const corpo = {
    nome: document.getElementById("funcionario-nome").value.trim(),
    cpf: (campoCpf.dataset.valorReal || campoCpf.value).replace(/\D/g, ""),
    email: campoEmail.dataset.valorReal || campoEmail.value.trim(),
    cargo: document.getElementById("funcionario-cargo").value,
    telefone: document.getElementById("funcionario-telefone").value.trim(),
    cidade: document.getElementById("funcionario-cidade").value.trim(),
    estado: document.getElementById("funcionario-estado").value,
    pais: document.getElementById("funcionario-pais").value.trim()
  };

  try {
    if (idFuncionario) {
      await apiFetch(`/usuarios/${idFuncionario}`, { method: "PUT", body: JSON.stringify(corpo) });
    } else {
      await apiFetch("/usuarios", { method: "POST", body: JSON.stringify(corpo) });
    }
    window.location.href = "funcionarios.html";
  } catch (erro) {
    exibirErro(erroEl, erro.message);
  }
}
