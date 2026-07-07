package com.aurafarm.backend.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Automação dos casos de teste descritos em /teste/casos-de-teste-validacao.md
 * (mesmos IDs: CT-001 a CT-006).
 *
 * Pré-requisitos para rodar esta classe (ela NÃO sobe a aplicação sozinha):
 *  - Backend rodando em http://localhost:8080
 *      cd backend && set -a && source .env && set +a && ./mvnw spring-boot:run
 *  - Frontend servido em http://localhost:5500
 *      cd frontend && python3 -m http.server 5500
 *  - Banco com os dados usados nos casos de teste: usuário admin@aurafarm.com /
 *    admin123 (seed), fornecedor com CNPJ 12345678000199 (Malhas Aura Ltda) e
 *    funcionário "João Silva Atualizado" com CPF 397.112.914-52.
 */
class SeleniumValidacaoTest {

    private static final String BASE_URL = "http://localhost:5500/pages/";

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeAll
    static void configurarWebDriverManager() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void iniciarNavegador() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments(
                "--headless=new",
                "--no-sandbox",
                "--disable-dev-shm-usage",
                "--window-size=1366,768"
        );
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void fecharNavegador() {
        if (driver != null) {
            driver.quit();
        }
    }

    private void fazerLogin(String email, String senha) {
        driver.get(BASE_URL + "login.html");
        driver.findElement(By.id("email")).sendKeys(email);
        driver.findElement(By.id("senha")).sendKeys(senha);
        driver.findElement(By.id("btn-entrar")).click();
    }

    @Test
    @DisplayName("CT-001 - Login com credenciais válidas deve redirecionar para o dashboard")
    void ct001_loginComCredenciaisValidas() {
        fazerLogin("admin@aurafarm.com", "admin123");

        wait.until(ExpectedConditions.urlContains("dashboard.html"));

        assertTrue(driver.getCurrentUrl().contains("dashboard.html"),
                "Esperava ser redirecionado para o dashboard após login válido");
    }

    @Test
    @DisplayName("CT-002 - Login com senha incorreta deve exibir mensagem de erro")
    void ct002_loginComSenhaIncorreta() {
        fazerLogin("admin@aurafarm.com", "senhaerrada123");

        WebElement erro = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("erro-login")));

        assertTrue(erro.getText().toLowerCase().contains("inválid"),
                "Esperava mensagem de e-mail ou senha inválido, mas veio: " + erro.getText());
        assertTrue(driver.getCurrentUrl().endsWith("login.html"),
                "Não deveria sair da tela de login quando as credenciais estão erradas");
    }

    @Test
    @DisplayName("CT-003 - Cadastro de fornecedor com dados válidos deve redirecionar para a listagem")
    void ct003_cadastrarFornecedorComDadosValidos() {
        fazerLogin("admin@aurafarm.com", "admin123");
        wait.until(ExpectedConditions.urlContains("dashboard.html"));

        driver.get(BASE_URL + "novo-fornecedor.html");

        String cnpjUnico = "9" + String.format("%013d", System.currentTimeMillis() % 1_000_000_000_000L);
        String emailUnico = "fornecedor.selenium." + System.currentTimeMillis() + "@teste.com";

        driver.findElement(By.id("fornecedor-nome")).sendKeys("Fornecedor Teste Selenium");
        driver.findElement(By.id("fornecedor-cnpj")).sendKeys(cnpjUnico);
        driver.findElement(By.id("fornecedor-telefone")).sendKeys("11999998888");
        driver.findElement(By.id("fornecedor-email")).sendKeys(emailUnico);
        driver.findElement(By.id("fornecedor-cidade")).sendKeys("Lavras");
        new Select(driver.findElement(By.id("fornecedor-estado"))).selectByValue("MG");

        driver.findElement(By.id("btn-salvar-fornecedor")).click();

        wait.until(ExpectedConditions.urlContains("fornecedores.html"));

        assertTrue(driver.getCurrentUrl().contains("fornecedores.html"),
                "Esperava redirecionar para a listagem de fornecedores após salvar com sucesso");
    }

    @Test
    @DisplayName("CT-004 - Cadastro de fornecedor com CNPJ duplicado deve ser bloqueado")
    void ct004_cadastrarFornecedorComCnpjDuplicado() {
        fazerLogin("admin@aurafarm.com", "admin123");
        wait.until(ExpectedConditions.urlContains("dashboard.html"));

        driver.get(BASE_URL + "novo-fornecedor.html");

        driver.findElement(By.id("fornecedor-nome")).sendKeys("Fornecedor Duplicado");
        driver.findElement(By.id("fornecedor-cnpj")).sendKeys("12345678000199"); // já cadastrado (Malhas Aura Ltda)
        driver.findElement(By.id("fornecedor-telefone")).sendKeys("11999997777");
        driver.findElement(By.id("fornecedor-email")).sendKeys("duplicado.selenium@teste.com");
        driver.findElement(By.id("fornecedor-cidade")).sendKeys("Lavras");
        new Select(driver.findElement(By.id("fornecedor-estado"))).selectByValue("MG");

        driver.findElement(By.id("btn-salvar-fornecedor")).click();

        WebElement erro = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("erro-fornecedor")));

        assertTrue(erro.getText().toLowerCase().contains("cnpj"),
                "Esperava mensagem de erro sobre CNPJ já cadastrado, mas veio: " + erro.getText());
        assertTrue(driver.getCurrentUrl().endsWith("novo-fornecedor.html"),
                "Não deveria sair da tela de cadastro quando o CNPJ é duplicado");
    }

    @Test
    @DisplayName("CT-005 - Cadastro de produto com preço zero deve ser bloqueado")
    void ct005_cadastrarProdutoComPrecoZero() {
        fazerLogin("admin@aurafarm.com", "admin123");
        wait.until(ExpectedConditions.urlContains("dashboard.html"));

        driver.get(BASE_URL + "novo-produto.html");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#produto-fornecedor option"), 0));

        driver.findElement(By.id("produto-codigo")).sendKeys("SEL" + (System.currentTimeMillis() % 100_000_000L));
        driver.findElement(By.id("produto-nome")).sendKeys("Produto Teste Selenium");
        new Select(driver.findElement(By.id("produto-tamanho"))).selectByValue("M");
        driver.findElement(By.id("produto-cor")).sendKeys("Azul");
        driver.findElement(By.id("produto-preco")).sendKeys("0");
        driver.findElement(By.id("produto-estoque")).sendKeys("10");

        driver.findElement(By.id("btn-salvar-produto")).click();

        WebElement erro = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("erro-produto")));
        String mensagem = erro.getText().toLowerCase();

        assertTrue(mensagem.contains("preço") || mensagem.contains("preco"),
                "Esperava mensagem de erro sobre preço inválido, mas veio: " + erro.getText());
        assertTrue(driver.getCurrentUrl().endsWith("novo-produto.html"),
                "Não deveria sair da tela de cadastro quando o preço é inválido");
    }

    @Test
    @DisplayName("CT-006 - Busca de funcionário pelo CPF deve filtrar a listagem")
    void ct006_buscarFuncionarioPeloCpf() {
        fazerLogin("admin@aurafarm.com", "admin123");
        wait.until(ExpectedConditions.urlContains("dashboard.html"));

        driver.get(BASE_URL + "funcionarios.html");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#tabela-funcionarios tr[data-id]"), 0));

        driver.findElement(By.id("busca-funcionario")).sendKeys("397.112.914-52");

        wait.until(driverAtual -> driver.findElements(By.cssSelector("#tabela-funcionarios tr[data-id]"))
                .stream()
                .filter(WebElement::isDisplayed)
                .count() == 1);

        List<WebElement> linhasVisiveis = driver.findElements(By.cssSelector("#tabela-funcionarios tr[data-id]"))
                .stream()
                .filter(WebElement::isDisplayed)
                .toList();

        assertEquals(1, linhasVisiveis.size(), "Esperava que a busca filtrasse para exatamente 1 linha visível");
        assertTrue(linhasVisiveis.get(0).getText().contains("João Silva Atualizado"),
                "Esperava que a linha filtrada fosse a do funcionário João Silva Atualizado");
    }
}
