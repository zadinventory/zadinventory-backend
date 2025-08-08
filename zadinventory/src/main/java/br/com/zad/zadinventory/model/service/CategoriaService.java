package br.com.zad.zadinventory.model.service;

import br.com.zad.zadinventory.model.entity.CategoriaEntity;
import br.com.zad.zadinventory.model.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;

    @Transactional
    public CategoriaEntity salvar(CategoriaEntity categoria) {
        validarCategoria(categoria);
        return repository.save(categoria);
    }

    public List<CategoriaEntity> buscarTodos() {
        return repository.findAll();
    }

    public Optional<CategoriaEntity> buscarPorId(Long id) {
        return repository.findById(id);
    }

    public List<String> buscarTodosNomes() {
        return repository.findAllNomes();
    }

    public Optional<Long> buscarIdPorNome(String nome) {
        return repository.findByNome(nome)
                .map(CategoriaEntity::getId);
    }

    @Transactional
    public CategoriaEntity atualizar(Long id, CategoriaEntity categoriaAtualizada) {
        CategoriaEntity categoria = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada!"));

        categoria.setNome(categoriaAtualizada.getNome());
        categoria.setDescricao(categoriaAtualizada.getDescricao());

        return repository.save(categoria);
    }

    @Transactional
    public void remover(Long id) {
        repository.deleteById(id);
    }

    private void validarCategoria(CategoriaEntity categoria) {
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da categoria é obrigatório!");
        }
        if (repository.existsByNome(categoria.getNome())) {
            throw new IllegalArgumentException("Categoria já existe!");
        }
    }
}