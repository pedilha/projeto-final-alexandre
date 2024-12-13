package com.biblioteca.pessoal.repository;

import com.biblioteca.pessoal.model.BibliotecaPessoal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BibliotecaPessoalRepository extends JpaRepository<BibliotecaPessoal, Long> {
    
    /**
     * Busca um registro na biblioteca pessoal por ID do livro e email do usuário.
     * @param livroId ID do livro.
     * @param email Email do usuário.
     * @return Registro encontrado ou vazio.
     */
    @Query("SELECT bp FROM BibliotecaPessoal bp WHERE bp.livro.id = :livroId AND bp.usuario.email = :email")
    Optional<BibliotecaPessoal> findByLivroIdAndUsuarioEmail(@Param("livroId") Long livroId, @Param("email") String email);

    /**
     * Busca todos os registros da biblioteca pessoal de um usuário pelo ID do usuário.
     * @param usuarioId ID do usuário.
     * @return Lista de registros encontrados.
     */
    List<BibliotecaPessoal> findByUsuarioId(Long usuarioId);

    /**
     * Busca todos os registros da biblioteca pessoal de um usuário pelo email.
     * @param email Email do usuário.
     * @return Lista de registros encontrados.
     */
    @Query("SELECT bp FROM BibliotecaPessoal bp WHERE bp.usuario.email = :email")
    List<BibliotecaPessoal> findByUsuarioEmail(@Param("email") String email);

    @Query("SELECT bp FROM BibliotecaPessoal bp WHERE bp.livro.id = :livroId AND bp.usuario.id = :usuarioId")
    Optional<BibliotecaPessoal> findByLivroIdAndUsuarioId(@Param("livroId") Long livroId, @Param("usuarioId") Long usuarioId);
}
