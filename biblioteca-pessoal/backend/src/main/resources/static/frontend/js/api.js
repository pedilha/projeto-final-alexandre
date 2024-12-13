const API_BASE_URL = "http://localhost:8080/api";

// Registrar Usuário
async function registrarUsuario(nome, email, senha) {
    const response = await fetch(`${API_BASE_URL}/auth/register`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nome, email, senha }),
    });
    return response.json();
}

// Adicionar Livro
async function adicionarLivro(usuarioId, livro) {
    const response = await fetch(`${API_BASE_URL}/livros/${usuarioId}`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(livro),
    });
    return response.json();
}

// Listar Livros
async function listarLivros(usuarioId) {
    const response = await fetch(`${API_BASE_URL}/livros/${usuarioId}`);
    return response.json();
}
