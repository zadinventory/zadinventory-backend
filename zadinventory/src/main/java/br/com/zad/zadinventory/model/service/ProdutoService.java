package br.com.zad.zadinventory.model.service;

import br.com.zad.zadinventory.model.entity.ProdutoEntity;
import br.com.zad.zadinventory.model.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;

    @Transactional
    public ProdutoEntity salvar(ProdutoEntity produto) {
        validarProduto(produto);
        return repository.save(produto);
    }

    public List<ProdutoEntity> buscarTodos() {
        return repository.findAll();
    }

    public ProdutoEntity buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado!"));
    }

    public List<ProdutoEntity> buscarPorNome(String nome) {
        return repository.findByNomeContainingIgnoreCase(nome);
    }

    public List<ProdutoEntity> buscarPorCategoria(Long categoriaId) {
        return repository.findByCategoriaId(categoriaId);
    }

    @Transactional
    public ProdutoEntity atualizar(Long id, ProdutoEntity produtoAtualizado) {
        ProdutoEntity produtoExistente = buscarPorId(id);
        produtoExistente.setNome(produtoAtualizado.getNome());
        produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        produtoExistente.setQuantidade(produtoAtualizado.getQuantidade());
        produtoExistente.setCategoria(produtoAtualizado.getCategoria());
        return repository.save(produtoExistente);
    }

    @Transactional
    public void remover(Long id) {
        repository.deleteById(id);
    }

    private void validarProduto(ProdutoEntity produto) {
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do produto é obrigatório!");
        }
        if (produto.getQuantidade() != null && produto.getQuantidade() < 0) {
            throw new IllegalArgumentException("Quantidade não pode ser negativa!");
        }
    }
}