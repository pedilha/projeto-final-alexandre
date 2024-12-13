package com.biblioteca.pessoal.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "livros")
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 255)
    @NotBlank(message = "O Google Book ID não pode ser vazio")
    @EqualsAndHashCode.Include
    private String googleBookId;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "O título do livro não pode ser vazio")
    private String titulo;

    @Column(length = 150)
    private String autor;

    @Column(length = 500)
    private String imagemUrl;

    @Column(name = "paginas_totais")
    @Positive(message = "O número de páginas deve ser maior que zero")
    private Integer paginasTotais;

    @Column(nullable = false)
    private LocalDateTime dataAdicao = LocalDateTime.now();

    // Construtor com argumentos para inicialização rápida
    public Livro(String googleBookId, String titulo, String autor, String imagemUrl, Integer paginasTotais) {
        this.googleBookId = googleBookId;
        this.titulo = titulo;
        this.autor = autor;
        this.imagemUrl = imagemUrl;
        this.paginasTotais = paginasTotais;
        this.dataAdicao = LocalDateTime.now();
    }
}
