package com.biblioteca.pessoal.controller;

import com.biblioteca.pessoal.model.BibliotecaPessoal;
import com.biblioteca.pessoal.model.Livro;
import com.biblioteca.pessoal.repository.BibliotecaPessoalRepository;
import com.biblioteca.pessoal.repository.LivroRepository;
import com.biblioteca.pessoal.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.biblioteca.pessoal.model.User; // Import correto


import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/livros")
@CrossOrigin(origins = "http://localhost:3000")
public class LivroController {

    private static final Logger logger = LoggerFactory.getLogger(LivroController.class);

    @Autowired
    private BibliotecaPessoalRepository bibliotecaPessoalRepository;

    @Autowired
    private LivroRepository livroRepository;

    // Listar livros do usuário autenticado
    @GetMapping("/{userId}")
    public ResponseEntity<?> listarLivros(
        @PathVariable Long userId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        logger.info("Listando livros para o usuário ID: {}", userId);
    
        // Buscar o usuário pelo ID fornecido
        Optional<User> userOptional = userRepository.findById(userId);
    
        if (userOptional.isEmpty()) {
            logger.warn("Usuário não encontrado com ID: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
    
        User user = userOptional.get();
    
        // Verificar se o e-mail do usuário autenticado corresponde ao e-mail do usuário solicitado
        if (!user.getEmail().equals(userDetails.getUsername())) {
            logger.warn("Acesso negado para o usuário ID: {}", userId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado.");
        }
    
        // Buscar os livros do usuário
        List<BibliotecaPessoal> livros = bibliotecaPessoalRepository.findByUsuarioId(userId);
        logger.info("Número de livros encontrados: {}", livros.size());
        return ResponseEntity.ok(livros);
    }
    

    // Atualizar página atual do livro
    @PutMapping("/{livroId}")
    public ResponseEntity<?> atualizarPaginaAtual(
        @PathVariable Long livroId,
        @RequestBody Map<String, Integer> payload,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        logger.info("Atualizando página para o livro com ID: {}", livroId);
    
        Integer novaPagina = payload.get("paginaAtual"); // Pegando a nova página do payload
        if (novaPagina == null) {
            return ResponseEntity.badRequest().body("O campo 'paginaAtual' é obrigatório.");
        }
    
        Optional<BibliotecaPessoal> bibliotecaOptional = bibliotecaPessoalRepository.findByLivroIdAndUsuarioEmail(livroId, userDetails.getUsername());
        if (bibliotecaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Livro não pertence ao usuário.");
        }
    
        BibliotecaPessoal biblioteca = bibliotecaOptional.get();
        biblioteca.setPaginaAtual(novaPagina);
        bibliotecaPessoalRepository.save(biblioteca);
    
        logger.info("Página atualizada com sucesso para o livro ID: {}", livroId);
        return ResponseEntity.ok("Página atualizada com sucesso.");
    }
    
    

    // Remover livro
    @DeleteMapping("/{livroId}")
    public ResponseEntity<?> removerLivro(
        @PathVariable Long livroId,
        @AuthenticationPrincipal UserDetails userDetails
    ) {
        logger.info("Removendo livro com ID: {}", livroId);

        // Buscar o registro na biblioteca pessoal do usuário autenticado
        Optional<BibliotecaPessoal> bibliotecaOptional = bibliotecaPessoalRepository.findByLivroIdAndUsuarioEmail(livroId, userDetails.getUsername());
        if (bibliotecaOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Livro não pertence ao usuário.");
        }

        BibliotecaPessoal biblioteca = bibliotecaOptional.get();
        bibliotecaPessoalRepository.delete(biblioteca);

        logger.info("Livro removido com sucesso. ID: {}", livroId);
        return ResponseEntity.ok("Livro removido com sucesso.");
    }
    @Autowired
    private UserRepository userRepository;
   

    @PostMapping("/{userId}")
    public ResponseEntity<?> adicionarLivro(
        @PathVariable Long userId,
        @RequestBody Livro livroRequest
    ) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        }
    
        User user = userOptional.get();
    
        Optional<Livro> livroOptional = livroRepository.findByGoogleBookId(livroRequest.getGoogleBookId());
        Livro livro = livroOptional.orElseGet(() -> livroRepository.save(livroRequest));
    
        Optional<BibliotecaPessoal> bibliotecaOptional = bibliotecaPessoalRepository.findByLivroIdAndUsuarioId(livro.getId(), userId);
        if (bibliotecaOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Livro já adicionado à biblioteca do usuário.");
        }
    
        BibliotecaPessoal biblioteca = new BibliotecaPessoal();
        biblioteca.setLivro(livro);
        biblioteca.setUsuario(user);
        biblioteca.setPaginaAtual(1);
        biblioteca.setFinalizado(false);
    
        bibliotecaPessoalRepository.save(biblioteca);
        return ResponseEntity.ok("Livro adicionado com sucesso!");
    }
    
    
}
