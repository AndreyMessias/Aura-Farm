document.addEventListener("DOMContentLoaded", () => {
  configurarBusca();
  configurarQuantidadeVenda();
  configurarFormularios();
  configurarBotoesDeAcao();
  aplicarMascaras();
});

/* =========================
   BUSCA NAS TABELAS
========================= */

function configurarBusca() {
  const campoBusca = document.querySelector(".search-box input");
  const tabela = document.querySelector("table tbody");

  if (!campoBusca || !tabela) return;

  campoBusca.addEventListener("input", () => {
    const termo = campoBusca.value.toLowerCase();
    const linhas = tabela.querySelectorAll("tr");

    linhas.forEach((linha) => {
      const textoLinha = linha.innerText.toLowerCase();
      linha.style.display = textoLinha.includes(termo) ? "" : "none";
    });
  });
}

/* =========================
   VENDA: QUANTIDADE E RESUMO
========================= */

function configurarQuantidadeVenda() {
  const produtos = document.querySelectorAll(".product-item");

  produtos.forEach((produto) => {
    const checkbox = produto.querySelector("input[type='checkbox']");
    const botoes = produto.querySelectorAll(".qty-btn");
    const quantidade = produto.querySelector(".qty-val");

    if (!quantidade || botoes.length < 2) return;

    const botaoMenos = botoes[0];
    const botaoMais = botoes[1];

    botaoMenos.addEventListener("click", () => {
      let valor = Number(quantidade.innerText);

      if (valor > 1) {
        quantidade.innerText = valor - 1;
      }

      atualizarResumoVenda();
    });

    botaoMais.addEventListener("click", () => {
      let valor = Number(quantidade.innerText);
      quantidade.innerText = valor + 1;

      if (checkbox) checkbox.checked = true;

      atualizarResumoVenda();
    });

    if (checkbox) {
      checkbox.addEventListener("change", atualizarResumoVenda);
    }
  });

  atualizarResumoVenda();
}

function atualizarResumoVenda() {
  const resumo = document.querySelector(".summary-card");
  const produtos = document.querySelectorAll(".product-item");

  if (!resumo || produtos.length === 0) return;

  resumo.querySelectorAll(".summary-item").forEach((item) => item.remove());

  let total = 0;
  const totalDiv = resumo.querySelector(".summary-total");

  produtos.forEach((produto) => {
    const checkbox = produto.querySelector("input[type='checkbox']");
    const nome = produto.querySelector(".product-info-name")?.innerText;
    const precoTexto = produto.querySelector(".product-info-price")?.innerText;
    const quantidade = Number(produto.querySelector(".qty-val")?.innerText || 1);

    if (!checkbox || !checkbox.checked || !nome || !precoTexto) return;

    const preco = extrairPreco(precoTexto);
    const subtotal = preco * quantidade;
    total += subtotal;

    const item = document.createElement("div");
    item.className = "summary-item";
    item.innerHTML = `
      <div class="summary-item-name">${nome} x${quantidade}</div>
      <div class="summary-item-val">${formatarMoeda(subtotal)}</div>
    `;

    resumo.insertBefore(item, totalDiv);
  });

  const totalValor = resumo.querySelector(".summary-total-val");

  if (totalValor) {
    totalValor.innerText = formatarMoeda(total);
  }
}

function extrairPreco(texto) {
  const resultado = texto.match(/R\$ ?([\d.,]+)/);

  if (!resultado) return 0;

  return Number(resultado[1].replace(".", "").replace(",", "."));
}

function formatarMoeda(valor) {
  return valor.toLocaleString("pt-BR", {
    style: "currency",
    currency: "BRL"
  });
}

/* =========================
   FORMULÁRIOS
========================= */

function configurarFormularios() {
  const botoes = document.querySelectorAll(".btn-dark, .btn-confirm-white");

  botoes.forEach((botao) => {
    const texto = botao.innerText.toLowerCase();

    if (
      texto.includes("salvar") ||
      texto.includes("cadastrar") ||
      texto.includes("confirmar")
    ) {
      botao.addEventListener("click", (event) => {
        const valido = validarCamposObrigatorios();

        if (!valido) {
          event.preventDefault();
          alert("Preencha todos os campos obrigatórios antes de continuar.");
          return;
        }

        if (texto.includes("confirmar")) {
          alert("Venda registrada com sucesso.");
        } else {
          alert("Dados salvos com sucesso.");
        }
      });
    }
  });
}

function validarCamposObrigatorios() {
  const campos = document.querySelectorAll(
    "input:not([disabled]), textarea:not([disabled]), select:not([disabled])"
  );

  let valido = true;

  campos.forEach((campo) => {
    if (campo.offsetParent === null) return;

    if (campo.value.trim() === "") {
      campo.style.borderColor = "red";
      valido = false;
    } else {
      campo.style.borderColor = "";
    }

    if (campo.type === "email" && campo.value.trim() !== "") {
      if (!campo.value.includes("@") || !campo.value.includes(".")) {
        campo.style.borderColor = "red";
        valido = false;
      }
    }
  });

  return valido;
}

/* =========================
   MÁSCARAS
========================= */

function aplicarMascaras() {
  document.querySelectorAll("input").forEach((input) => {
    const label = input.closest(".field")?.querySelector("label")?.innerText.toLowerCase();

    if (!label) return;

    input.addEventListener("input", () => {
      if (label.includes("cpf")) {
        input.value = mascaraCPF(input.value);
      }

      if (label.includes("cnpj")) {
        input.value = mascaraCNPJ(input.value);
      }

      if (label.includes("telefone")) {
        input.value = mascaraTelefone(input.value);
      }

      if (label.includes("cep")) {
        input.value = mascaraCEP(input.value);
      }
    });
  });
}

function apenasNumeros(valor) {
  return valor.replace(/\D/g, "");
}

function mascaraCPF(valor) {
  valor = apenasNumeros(valor).slice(0, 11);

  return valor
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d)/, "$1.$2")
    .replace(/(\d{3})(\d{1,2})$/, "$1-$2");
}

function mascaraCNPJ(valor) {
  valor = apenasNumeros(valor).slice(0, 14);

  return valor
    .replace(/^(\d{2})(\d)/, "$1.$2")
    .replace(/^(\d{2})\.(\d{3})(\d)/, "$1.$2.$3")
    .replace(/\.(\d{3})(\d)/, ".$1/$2")
    .replace(/(\d{4})(\d)/, "$1-$2");
}

function mascaraTelefone(valor) {
  valor = apenasNumeros(valor).slice(0, 11);

  if (valor.length <= 10) {
    return valor
      .replace(/(\d{2})(\d)/, "($1) $2")
      .replace(/(\d{4})(\d)/, "$1-$2");
  }

  return valor
    .replace(/(\d{2})(\d)/, "($1) $2")
    .replace(/(\d{5})(\d)/, "$1-$2");
}

function mascaraCEP(valor) {
  valor = apenasNumeros(valor).slice(0, 8);

  return valor.replace(/(\d{5})(\d)/, "$1-$2");
}

/* =========================
   AÇÕES DE TABELA
========================= */

function configurarBotoesDeAcao() {
  const botoesExcluir = document.querySelectorAll(".btn-del");

  botoesExcluir.forEach((botao) => {
    botao.addEventListener("click", (event) => {
      event.preventDefault();

      const confirmar = confirm("Tem certeza que deseja realizar esta ação?");

      if (!confirmar) return;

      const linha = botao.closest("tr");

      if (linha) {
        linha.remove();
        alert("Registro removido da tela com sucesso.");
      } else {
        alert("Ação confirmada com sucesso.");
      }
    });
  });
}

/* =========================
   STATUS DA VENDA
========================= */

function alterarStatus(botao) {
  const linha = botao.closest("tr");
  const badge = linha.querySelector(".badge");

  const novoStatus = prompt(
`Escolha o novo status:

1 - Pendente
2 - Enviado
3 - Entregue`
  );

  if (novoStatus === null) return;

  badge.classList.remove("badge-pending", "badge-sent", "badge-done");

  switch (novoStatus) {
    case "1":
      badge.textContent = "Pendente";
      badge.classList.add("badge-pending");
      break;

    case "2":
      badge.textContent = "Enviado";
      badge.classList.add("badge-sent");
      break;

    case "3":
      badge.textContent = "Entregue";
      badge.classList.add("badge-done");
      break;

    default:
      alert("Status inválido.");
  }
}

/* =========================
   LOGIN
========================= */

function validarLogin() {
  const email = document.getElementById("email");
  const senha = document.getElementById("senha");

  if (email.value.trim() === "") {
    alert("Informe o e-mail.");
    email.focus();
    return;
  }

  if (!email.value.includes("@") || !email.value.includes(".")) {
    alert("Digite um e-mail válido.");
    email.focus();
    return;
  }

  if (senha.value.trim() === "") {
    alert("Informe a senha.");
    senha.focus();
    return;
  }

  window.location.href = "dashboard.html";
}   