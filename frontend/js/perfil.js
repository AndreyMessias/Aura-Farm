document.addEventListener("DOMContentLoaded", () => {
  if (document.getElementById("perfil-nome")) carregarPerfil();
  if (document.getElementById("editar-perfil-nome")) carregarEdicaoPerfil();
});

/* =========================
   VER PERFIL (RF011)
========================= */

async function carregarPerfil() {
  const erroEl = document.getElementById("erro-perfil");

  try {
    const usuario = await apiFetch("/usuarios/me");

    document.getElementById("perfil-nome").value = usuario.nome;
    document.getElementById("perfil-email").value = usuario.email;
    document.getElementById("perfil-cpf").value = formatarCpf(usuario.cpf);
    document.getElementById("perfil-telefone").value = usuario.telefone || "";
    document.getElementById("perfil-cargo").value = cargoLabel(usuario.cargo);
    document.getElementById("perfil-cidade").value = usuario.cidade || "";
    document.getElementById("perfil-estado").value = usuario.estado || "";
    document.getElementById("perfil-pais").value = usuario.pais || "";
  } catch (erro) {
    exibirErro(erroEl, erro.message);
  }
}

/* =========================
   EDITAR PERFIL (RF012 — e-mail e CPF bloqueados)
========================= */

async function carregarEdicaoPerfil() {
  const erroEl = document.getElementById("erro-editar-perfil");
  preencherSelectUf(document.getElementById("editar-perfil-estado"));

  try {
    const usuario = await apiFetch("/usuarios/me");

    document.getElementById("editar-perfil-nome").value = usuario.nome;
    document.getElementById("editar-perfil-email").value = usuario.email;
    document.getElementById("editar-perfil-cpf").value = formatarCpf(usuario.cpf);
    document.getElementById("editar-perfil-telefone").value = usuario.telefone || "";
    document.getElementById("editar-perfil-cidade").value = usuario.cidade || "";
    document.getElementById("editar-perfil-estado").value = usuario.estado || "";
    document.getElementById("editar-perfil-pais").value = usuario.pais || "Brasil";
  } catch (erro) {
    exibirErro(erroEl, erro.message);
    return;
  }

  document.getElementById("btn-salvar-perfil").addEventListener("click", salvarPerfil);
}

async function salvarPerfil() {
  const erroEl = document.getElementById("erro-editar-perfil");
  exibirErro(erroEl, "");

  const corpo = {
    nome: document.getElementById("editar-perfil-nome").value.trim(),
    telefone: document.getElementById("editar-perfil-telefone").value.trim(),
    cidade: document.getElementById("editar-perfil-cidade").value.trim(),
    estado: document.getElementById("editar-perfil-estado").value,
    pais: document.getElementById("editar-perfil-pais").value.trim()
  };

  try {
    await apiFetch("/usuarios/me", { method: "PUT", body: JSON.stringify(corpo) });
    window.location.href = "perfil.html";
  } catch (erro) {
    exibirErro(erroEl, erro.message);
  }
}
