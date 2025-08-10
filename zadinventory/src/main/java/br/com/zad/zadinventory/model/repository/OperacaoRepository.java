package br.com.zad.zadinventory.model.repository;

import br.com.zad.zadinventory.model.entity.OperacaoEntity;
import br.com.zad.zadinventory.model.enums.Situacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OperacaoRepository extends JpaRepository<OperacaoEntity, Long> {
    List<OperacaoEntity> findBySituacao(Situacao situacao);

    @Query("SELECT o FROM OperacaoEntity o WHERE o.produto.id = :produtoId")
    List<OperacaoEntity> findByProdutoId(@Param("produtoId") Long produtoId);

    @Query("SELECT o FROM OperacaoEntity o WHERE o.usuario.id = :usuarioId")
    List<OperacaoEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}