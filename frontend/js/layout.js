document.addEventListener("DOMContentLoaded", () => {
  exigirAutenticacao();
  preencherUsuarioNaSidebar();
  configurarLogout();
  restringirMenuPorCargo();
});

function preencherUsuarioNaSidebar() {
  const usuario = getUsuarioLogado();
  if (!usuario) return;

  const nomeEl = document.querySelector(".user-name");
  const cargoEl = document.querySelector(".user-role");
  const avatarEl = document.querySelector(".user-avatar");

  if (nomeEl) nomeEl.textContent = usuario.nome;
  if (cargoEl) cargoEl.textContent = cargoLabel(usuario.cargo);
  if (avatarEl) avatarEl.textContent = iniciaisDoNome(usuario.nome);
}

function iniciaisDoNome(nome) {
  return nome
    .split(" ")
    .filter(Boolean)
    .slice(0, 2)
    .map((parte) => parte[0].toUpperCase())
    .join("");
}

function configurarLogout() {
  const linkSair = document.querySelector('.nav-item[href="login.html"]');
  if (!linkSair) return;

  linkSair.addEventListener("click", (event) => {
    event.preventDefault();
    logout();
  });
}

// Fornecedores e Funcionários são exclusivos do Gerente (RF001-004, RF005-008).
// Funcionário nem enxerga essas seções no menu.
function restringirMenuPorCargo() {
  if (ehGerente()) return;

  document.querySelectorAll('a[href="fornecedores.html"], a[href="funcionarios.html"]').forEach((link) => {
    link.closest(".nav-section")?.remove();
  });
}
