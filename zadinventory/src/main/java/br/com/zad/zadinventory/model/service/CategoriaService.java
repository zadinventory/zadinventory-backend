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

        if (categoriaAtualizada.getNome() != null) {
            validarNomeCategoria(categoriaAtualizada.getNome(), id);
            categoria.setNome(categoriaAtualizada.getNome());
        }
        if (categoriaAtualizada.getDescricao() != null) {
            categoria.setDescricao(categoriaAtualizada.getDescricao());
        }

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
        validarNomeCategoria(categoria.getNome(), categoria.getId());
    }

    private void validarNomeCategoria(String nome, Long id) {
        boolean nomeExiste = repository.existsByNome(nome);
        boolean ehAtualizacao = id != null;

        if (nomeExiste) {
            if (!ehAtualizacao || !repository.findById(id).get().getNome().equals(nome)) {
                throw new IllegalArgumentException("Categoria já existe!");
            }
        }
    }
}