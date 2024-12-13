// Registro de Usuário
document.getElementById("register-form").addEventListener("submit", async (event) => {
    event.preventDefault();

    const nome = document.getElementById("nome").value;
    const email = document.getElementById("email-reg").value;
    const senha = document.getElementById("senha-reg").value;

    try {
        const response = await fetch("http://localhost:8080/api/auth/register", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ nome, email, senha }),
        });

        if (response.status === 201) {
            alert("Usuário registrado com sucesso! Faça login agora.");
            // Redirecionar ao formulário de login
            document.getElementById("register-form").classList.remove("active-form");
            document.getElementById("login-form").classList.remove("hidden-form");
        } else {
            const error = await response.text();
            alert(`Erro: ${error}`);
        }
    } catch (error) {
        alert("Erro de conexão.");
    }
});


// Evento de login
document.getElementById("login-form").addEventListener("submit", async (event) => {
    event.preventDefault(); // Evita o recarregamento da página

    const email = document.getElementById("email").value; // Captura o email
    const senha = document.getElementById("senha").value; // Captura a senha

    try {
        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ email, senha }),
        });

        if (response.ok) {
            const token = await response.text(); // Recebe o token JWT
            localStorage.setItem("jwtToken", token); // Salva o token no Local Storage
            alert("Login bem-sucedido!");
            window.location.href = "pagina-principal.html"; // Redireciona para a página principal
        } else {
            alert("Email ou senha inválidos."); // Mensagem de erro
        }
    } catch (error) {
        alert("Erro ao conectar com o servidor."); // Caso o servidor esteja inacessível
    }
});

// Alternar entre formulários de login e registro
document.getElementById("show-register").addEventListener("click", () => {
    document.getElementById("login-form").classList.add("hidden-form");
    document.getElementById("register-form").classList.add("active-form");
});

document.getElementById("show-login").addEventListener("click", () => {
    document.getElementById("register-form").classList.remove("active-form");
    document.getElementById("login-form").classList.remove("hidden-form");
});
