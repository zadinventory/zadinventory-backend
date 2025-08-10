package br.com.zad.zadinventory.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    private String descricao;
    private Integer quantidade;

    @ManyToOne(optional = false)
    @JoinColumn(name = "categoria_id")
    private CategoriaEntity categoria;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;
}