package com.biblioteca.pessoal.repository;

import com.biblioteca.pessoal.model.Livro;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LivroRepository extends JpaRepository<Livro, Long> {
    Optional<Livro> findByGoogleBookId(String googleBookId);
}
