/* =========================
   CONFIGURAÇÃO
========================= */

const API_BASE_URL = "http://localhost:8080/aurafarm";

const UFS = [
  "AC", "AL", "AP", "AM", "BA", "CE", "DF", "ES", "GO",
  "MA", "MT", "MS", "MG", "PA", "PB", "PR", "PE", "PI",
  "RJ", "RN", "RS", "RO", "RR", "SC", "SP", "SE", "TO"
];

/* =========================
   SESSÃO (token + usuário logado)
========================= */

function getToken() {
  return localStorage.getItem("auraToken");
}

function getUsuarioLogado() {
  const bruto = localStorage.getItem("auraUsuario");
  return bruto ? JSON.parse(bruto) : null;
}

function salvarSessao({ token, nome, email, cargo }) {
  localStorage.setItem("auraToken", token);
  localStorage.setItem("auraUsuario", JSON.stringify({ nome, email, cargo }));
}

function limparSessao() {
  localStorage.removeItem("auraToken");
  localStorage.removeItem("auraUsuario");
}

function logout() {
  limparSessao();
  window.location.href = "login.html";
}

function exigirAutenticacao() {
  if (!getToken()) {
    window.location.href = "login.html";
  }
}

function ehGerente() {
  return getUsuarioLogado()?.cargo === "GERENTE";
}

/* =========================
   CLIENTE HTTP
========================= */

async function apiFetch(caminho, opcoes = {}) {
  const headers = {
    "Content-Type": "application/json",
    ...(opcoes.headers || {})
  };

  const token = getToken();
  if (token) headers["Authorization"] = `Bearer ${token}`;

  let resposta;
  try {
    resposta = await fetch(`${API_BASE_URL}${caminho}`, { ...opcoes, headers });
  } catch (erro) {
    throw new Error("Não foi possível conectar ao servidor. Verifique se o backend está rodando.");
  }

  if (resposta.status === 401) {
    limparSessao();
    window.location.href = "login.html";
    throw new Error("Sessão expirada. Faça login novamente.");
  }

  if (resposta.status === 204) return null;

  const tipo = resposta.headers.get("content-type") || "";
  const corpo = tipo.includes("application/json") ? await resposta.json() : null;

  if (!resposta.ok) {
    throw new Error(extrairMensagemErro(corpo, resposta.status));
  }

  return corpo;
}

function extrairMensagemErro(corpo, status) {
  if (!corpo) return `Erro inesperado (HTTP ${status}).`;

  if (corpo.errors && typeof corpo.errors === "object") {
    return Object.values(corpo.errors).join(" ");
  }

  return corpo.message || `Erro inesperado (HTTP ${status}).`;
}

/* =========================
   HELPERS DE FORMATAÇÃO
========================= */

function formatarMoedaBR(valor) {
  return Number(valor || 0).toLocaleString("pt-BR", { style: "currency", currency: "BRL" });
}

function formatarDataBR(isoString) {
  if (!isoString) return "-";
  const data = new Date(isoString);
  return data.toLocaleDateString("pt-BR");
}

function formatarCpf(cpf) {
  if (!cpf) return "-";
  return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, "$1.$2.$3-$4");
}

function formatarCnpj(cnpj) {
  if (!cnpj) return "-";
  return cnpj.replace(/(\d{2})(\d{3})(\d{3})(\d{4})(\d{2})/, "$1.$2.$3/$4-$5");
}

function cargoLabel(cargo) {
  return cargo === "GERENTE" ? "Gerente" : "Funcionário";
}

function statusVendaInfo(status) {
  const mapa = {
    PENDENTE: { label: "Pendente", classe: "badge-pending" },
    ENVIADO: { label: "Enviado", classe: "badge-sent" },
    ENTREGUE: { label: "Entregue", classe: "badge-done" },
    CANCELADO: { label: "Cancelado", classe: "badge-cancelled" }
  };
  return mapa[status] || { label: status, classe: "" };
}

function preencherSelectUf(select, valorSelecionado) {
  if (!select) return;
  select.innerHTML = UFS.map((uf) => `<option value="${uf}">${uf}</option>`).join("");
  if (valorSelecionado) select.value = valorSelecionado;
}

function lerParametroUrl(nome) {
  return new URLSearchParams(window.location.search).get(nome);
}

function escaparHtml(texto) {
  const div = document.createElement("div");
  div.textContent = texto ?? "";
  return div.innerHTML;
}

function exibirErro(elemento, mensagem) {
  if (!elemento) {
    alert(mensagem);
    return;
  }
  elemento.textContent = mensagem;
  elemento.style.display = mensagem ? "block" : "none";
}
