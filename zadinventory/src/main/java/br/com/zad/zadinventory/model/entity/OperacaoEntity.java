package br.com.zad.zadinventory.model.entity;

import br.com.zad.zadinventory.model.enums.Situacao;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "operacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperacaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id")
    private ProdutoEntity produto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id")
    private UsuarioEntity usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Situacao situacao;

    @Column(nullable = false)
    private LocalDateTime data;
}