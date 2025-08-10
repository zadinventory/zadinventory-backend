package br.com.zad.zadinventory.model.repository;

import br.com.zad.zadinventory.model.entity.ProdutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<ProdutoEntity, Long> {
    List<ProdutoEntity> findByNomeContainingIgnoreCase(String nome);
    Optional<ProdutoEntity> findByNomeIgnoreCase(String nome);
    List<ProdutoEntity> findByCategoriaId(Long categoriaId);

    @Query("SELECT p FROM ProdutoEntity p WHERE p.usuario.id = :usuarioId")
    List<ProdutoEntity> findByUsuarioId(@Param("usuarioId") Long usuarioId);
}