package br.com.zad.zadinventory.model.service;

import br.com.zad.zadinventory.model.entity.CategoriaEntity;
import br.com.zad.zadinventory.model.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;

    public CategoriaEntity salvar(CategoriaEntity categoria) {
        if (categoria.getNome() == null || categoria.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome da categoria é obrigatório!");
        }
        if (repository.findByNome(categoria.getNome()).isPresent()) {
            throw new IllegalArgumentException("Categoria já existe!");
        }
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

    public void remover(Long id) {
        repository.deleteById(id);
    }
}