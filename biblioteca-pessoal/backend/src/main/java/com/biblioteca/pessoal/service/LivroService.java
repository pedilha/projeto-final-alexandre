package com.biblioteca.pessoal.service;

import com.biblioteca.pessoal.model.Livro;
import com.biblioteca.pessoal.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LivroService {

    private final LivroRepository livroRepository;

    @Autowired
    public LivroService(LivroRepository livroRepository) {
        this.livroRepository = livroRepository;
    }

    // Método para buscar todos os livros
    public List<Livro> buscarTodos() {
        return livroRepository.findAll();
    }

    // Método para buscar um livro pelo ID
    public Optional<Livro> buscarPorId(Long id) {
        return livroRepository.findById(id);
    }

    // Método para salvar um novo livro
    public Livro salvar(Livro livro) {
        return livroRepository.save(livro);
    }

    // Método para atualizar um livro existente
    public Livro atualizar(Long id, Livro livroAtualizado) {
        Optional<Livro> livroExistente = livroRepository.findById(id);
        if (livroExistente.isPresent()) {
            Livro livro = livroExistente.get();
            livro.setGoogleBookId(livroAtualizado.getGoogleBookId());
            livro.setTitulo(livroAtualizado.getTitulo());
            livro.setAutor(livroAtualizado.getAutor());
            livro.setImagemUrl(livroAtualizado.getImagemUrl());
            livro.setPaginasTotais(livroAtualizado.getPaginasTotais());
            return livroRepository.save(livro);
        } else {
            throw new RuntimeException("Livro não encontrado com o ID: " + id);
        }
    }

    // Método para deletar um livro pelo ID
    public void deletar(Long id) {
        if (livroRepository.existsById(id)) {
            livroRepository.deleteById(id);
        } else {
            throw new RuntimeException("Livro não encontrado com o ID: " + id);
        }
    }
}
