package com.biblioteca.pessoal.controller;

import com.biblioteca.pessoal.model.User;
import com.biblioteca.pessoal.repository.UserRepository;
import com.biblioteca.pessoal.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Registro de Usuário
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já existe.");
        }
        user.setSenha(passwordEncoder.encode(user.getSenha())); // Criptografa a senha
        userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso.");
    }
    

// Login de Usuário
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody User user) {
    System.out.println("Tentando fazer login com o email: " + user.getEmail());

    // Busca o usuário no banco de dados pelo email
    Optional<User> usuario = userRepository.findByEmail(user.getEmail());

    if (usuario.isPresent()) {
        System.out.println("Usuário encontrado: " + usuario.get().getEmail());

        // Verifica se a senha fornecida corresponde à senha armazenada (usando passwordEncoder)
        if (passwordEncoder.matches(user.getSenha(), usuario.get().getSenha())) {
            System.out.println("Senha válida. Gerando token...");

            // Recupera o ID do usuário do banco e gera o token JWT
            String token = jwtService.generateToken(usuario.get().getEmail(), usuario.get().getId());
            return ResponseEntity.ok(token); // Retorna token JWT
        } else {
            System.out.println("Senha inválida.");
        }
    } else {
        System.out.println("Usuário não encontrado.");
    }

    // Retorna erro 401 se email ou senha forem inválidos
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Email ou senha inválidos.");
}

    
    
}
