package br.com.zad.zadinventory.model.repository;

import br.com.zad.zadinventory.model.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Long> {

    // Busca por nome (equivalente ao buscarPorNome)
    Optional<CategoriaEntity> findByNome(String nome);

    // Busca todos os nomes (equivalente ao buscarTodosNomes)
    @Query("SELECT c.nome FROM CategoriaEntity c")
    List<String> findAllNomes();

    // Verifica existência por ID (método padrão do JpaRepository)
    boolean existsById(Long id);
}