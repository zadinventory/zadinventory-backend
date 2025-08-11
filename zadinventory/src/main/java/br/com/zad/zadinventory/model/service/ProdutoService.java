package br.com.zad.zadinventory.model.service;

import br.com.zad.zadinventory.model.entity.ProdutoEntity;
import br.com.zad.zadinventory.model.entity.CategoriaEntity;
import br.com.zad.zadinventory.model.entity.UsuarioEntity;
import br.com.zad.zadinventory.model.repository.ProdutoRepository;
import br.com.zad.zadinventory.model.repository.CategoriaRepository;
import br.com.zad.zadinventory.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository repository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public ProdutoEntity salvar(ProdutoEntity produto) {
        validarProduto(produto);
        carregarEntidadesRelacionadas(produto);
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

        if (produtoAtualizado.getNome() != null) {
            produtoExistente.setNome(produtoAtualizado.getNome());
        }
        if (produtoAtualizado.getDescricao() != null) {
            produtoExistente.setDescricao(produtoAtualizado.getDescricao());
        }
        if (produtoAtualizado.getQuantidade() != null) {
            produtoExistente.setQuantidade(produtoAtualizado.getQuantidade());
        }

        if (produtoAtualizado.getCategoria() != null && produtoAtualizado.getCategoria().getId() != null) {
            CategoriaEntity categoria = categoriaRepository.findById(produtoAtualizado.getCategoria().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada!"));
            produtoExistente.setCategoria(categoria);
        }

        if (produtoAtualizado.getUsuario() != null && produtoAtualizado.getUsuario().getId() != null) {
            UsuarioEntity usuario = usuarioRepository.findById(produtoAtualizado.getUsuario().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
            produtoExistente.setUsuario(usuario);
        }

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
        if (produto.getUsuario() == null || produto.getUsuario().getId() == null) {
            throw new IllegalArgumentException("Usuário é obrigatório!");
        }
        if (produto.getCategoria() == null || produto.getCategoria().getId() == null) {
            throw new IllegalArgumentException("Categoria é obrigatória!");
        }
    }

    private void carregarEntidadesRelacionadas(ProdutoEntity produto) {
        UsuarioEntity usuario = usuarioRepository.findById(produto.getUsuario().getId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
        produto.setUsuario(usuario);

        CategoriaEntity categoria = categoriaRepository.findById(produto.getCategoria().getId())
                .orElseThrow(() -> new IllegalArgumentException("Categoria não encontrada!"));
        produto.setCategoria(categoria);
    }
}