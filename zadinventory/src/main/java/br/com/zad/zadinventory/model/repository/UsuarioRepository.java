package br.com.zad.zadinventory.model.repository;

import br.com.zad.zadinventory.model.entity.UsuarioEntity;
import br.com.zad.zadinventory.model.enums.TipoUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByEmail(String email);

    @Query("SELECT u FROM UsuarioEntity u WHERE u.tipoUsuario = :tipo")
    List<UsuarioEntity> findByTipoUsuario(@Param("tipo") TipoUsuario tipo);

    boolean existsByEmail(String email);
}