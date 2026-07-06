/* =========================
   LOGIN
========================= */

document.addEventListener("DOMContentLoaded", () => {
  configurarLogin();
  configurarRecuperarSenha();
  configurarPrimeiroAcesso();
});

function configurarLogin() {
  const botao = document.getElementById("btn-entrar");
  if (!botao) return;

  botao.addEventListener("click", async () => {
    const email = document.getElementById("email").value.trim();
    const senha = document.getElementById("senha").value;
    const erroEl = document.getElementById("erro-login");

    exibirErro(erroEl, "");

    if (!email || !senha) {
      exibirErro(erroEl, "Informe e-mail e senha.");
      return;
    }

    try {
      const resposta = await apiFetch("/auth/login", {
        method: "POST",
        body: JSON.stringify({ email, senha })
      });

      salvarSessao(resposta);
      window.location.href = "dashboard.html";
    } catch (erro) {
      exibirErro(erroEl, erro.message);
    }
  });
}

/* =========================
   RECUPERAR SENHA (RF010) — 3 passos
========================= */

function configurarRecuperarSenha() {
  const btnSolicitar = document.getElementById("btn-solicitar-codigo");
  if (!btnSolicitar) return;

  let emailEmRecuperacao = "";
  let codigoVerificado = "";

  const erroEl = document.getElementById("erro-recuperar-senha");
  const passo1 = document.getElementById("passo-1-email");
  const passo2 = document.getElementById("passo-2-codigo");
  const passo3 = document.getElementById("passo-3-nova-senha");

  btnSolicitar.addEventListener("click", async () => {
    const email = document.getElementById("email-recuperacao").value.trim();
    exibirErro(erroEl, "");

    if (!email) {
      exibirErro(erroEl, "Informe o e-mail cadastrado.");
      return;
    }

    try {
      await apiFetch("/auth/recuperar-senha/solicitar", {
        method: "POST",
        body: JSON.stringify({ email })
      });

      emailEmRecuperacao = email;
      passo1.style.display = "none";
      passo2.style.display = "block";
    } catch (erro) {
      exibirErro(erroEl, erro.message);
    }
  });

  const btnVerificar = document.getElementById("btn-verificar-codigo");
  btnVerificar.addEventListener("click", async () => {
    const codigo = document.getElementById("codigo-recuperacao").value.trim();
    exibirErro(erroEl, "");

    if (!codigo) {
      exibirErro(erroEl, "Informe o código recebido por e-mail.");
      return;
    }

    try {
      await apiFetch("/auth/recuperar-senha/verificar", {
        method: "POST",
        body: JSON.stringify({ email: emailEmRecuperacao, codigo })
      });

      codigoVerificado = codigo;
      passo2.style.display = "none";
      passo3.style.display = "block";
    } catch (erro) {
      exibirErro(erroEl, erro.message);
    }
  });

  const btnRedefinir = document.getElementById("btn-redefinir-senha");
  btnRedefinir.addEventListener("click", async () => {
    const novaSenha = document.getElementById("nova-senha").value;
    const confirmacaoNovaSenha = document.getElementById("confirmar-nova-senha").value;
    exibirErro(erroEl, "");

    if (!novaSenha || !confirmacaoNovaSenha) {
      exibirErro(erroEl, "Preencha os dois campos de senha.");
      return;
    }

    if (novaSenha !== confirmacaoNovaSenha) {
      exibirErro(erroEl, "As senhas não coincidem.");
      return;
    }

    try {
      await apiFetch("/auth/recuperar-senha/redefinir", {
        method: "POST",
        body: JSON.stringify({
          email: emailEmRecuperacao,
          codigo: codigoVerificado,
          novaSenha,
          confirmacaoNovaSenha
        })
      });

      mostrarSucessoERedirecionar(passo3, document.getElementById("passo-4-sucesso"), "login.html");
    } catch (erro) {
      exibirErro(erroEl, erro.message);
    }
  });
}

/* =========================
   PRIMEIRO ACESSO (definir senha inicial)
========================= */

function configurarPrimeiroAcesso() {
  const btnValidar = document.getElementById("btn-validar-email");
  if (!btnValidar) return;

  let emailValidado = "";

  const erroEl = document.getElementById("erro-primeiro-acesso");
  const passo1 = document.getElementById("passo-1-validar-email");
  const passo2 = document.getElementById("passo-2-definir-senha");

  btnValidar.addEventListener("click", async () => {
    const email = document.getElementById("email-primeiro-acesso").value.trim();
    exibirErro(erroEl, "");

    if (!email) {
      exibirErro(erroEl, "Informe o e-mail cadastrado pelo seu gerente.");
      return;
    }

    try {
      await apiFetch("/auth/validar-email", {
        method: "POST",
        body: JSON.stringify({ email })
      });

      emailValidado = email;
      passo1.style.display = "none";
      passo2.style.display = "block";
    } catch (erro) {
      exibirErro(erroEl, erro.message);
    }
  });

  const btnDefinir = document.getElementById("btn-definir-senha");
  btnDefinir.addEventListener("click", async () => {
    const senha = document.getElementById("senha-primeiro-acesso").value;
    const confirmacaoSenha = document.getElementById("confirmar-senha-primeiro-acesso").value;
    exibirErro(erroEl, "");

    if (!senha || !confirmacaoSenha) {
      exibirErro(erroEl, "Preencha os dois campos de senha.");
      return;
    }

    if (senha !== confirmacaoSenha) {
      exibirErro(erroEl, "As senhas não coincidem.");
      return;
    }

    try {
      await apiFetch("/auth/definir-senha", {
        method: "POST",
        body: JSON.stringify({ email: emailValidado, senha, confirmacaoSenha })
      });

      mostrarSucessoERedirecionar(passo2, document.getElementById("passo-3-sucesso"), "login.html");
    } catch (erro) {
      exibirErro(erroEl, erro.message);
    }
  });
}
