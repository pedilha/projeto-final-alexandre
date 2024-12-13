package com.biblioteca.pessoal.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "biblioteca_pessoal")
@Data
public class BibliotecaPessoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;
    
    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
    
    public User getUsuario() {
        return usuario;
    }

    @ManyToOne
    @JoinColumn(name = "livro_id", nullable = false)
    private Livro livro;

    @Column(name = "pagina_atual", nullable = false)
    private Integer paginaAtual = 1;

    @Column(nullable = false)
    private Boolean finalizado = false;

}
