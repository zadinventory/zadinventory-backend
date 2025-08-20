package app.zad.zadinventory.model.service;

import app.zad.zadinventory.model.entity.OperacaoEntity;
import app.zad.zadinventory.model.entity.ProdutoEntity;
import app.zad.zadinventory.model.entity.UsuarioEntity;
import app.zad.zadinventory.model.enums.Situacao;
import app.zad.zadinventory.model.exception.RegraNegocioException;
import app.zad.zadinventory.model.repository.OperacaoRepository;
import app.zad.zadinventory.model.repository.ProdutoRepository;
import app.zad.zadinventory.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperacaoService {

    private final OperacaoRepository repository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public OperacaoEntity salvar(OperacaoEntity operacao) {
        validarOperacao(operacao);

        // Carrega as entidades completas do banco
        ProdutoEntity produto = produtoRepository.findById(operacao.getProduto().getId())
                .orElseThrow(() -> new RegraNegocioException("Produto não encontrado com ID: " + operacao.getProduto().getId()));

        UsuarioEntity usuario = usuarioRepository.findById(operacao.getUsuario().getId())
                .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado com ID: " + operacao.getUsuario().getId()));

        operacao.setProduto(produto);
        operacao.setUsuario(usuario);

        return repository.save(operacao);
    }

    public List<OperacaoEntity> buscarTodos() {
        return repository.findAll();
    }

    public OperacaoEntity buscarPorId(Long id) {
        return repository.findComRelacionamentosById(id)
                .orElseThrow(() -> new RegraNegocioException("Operação não encontrada!"));
    }

    public List<OperacaoEntity> buscarPorSituacao(Situacao situacao) {
        return repository.findBySituacao(situacao);
    }

    public List<OperacaoEntity> buscarPorProduto(Long produtoId) {
        return repository.findByProdutoId(produtoId);
    }

    public List<OperacaoEntity> buscarPorUsuario(Long usuarioId) {
        return repository.findByUsuarioId(usuarioId);
    }

    @Transactional
    public OperacaoEntity atualizarSituacao(Long id, Situacao novaSituacao) {
        OperacaoEntity operacao = buscarPorId(id);
        operacao.setSituacao(novaSituacao);
        return repository.save(operacao);
    }

    @Transactional
    public OperacaoEntity atualizar(Long id, OperacaoEntity operacaoAtualizada) {
        OperacaoEntity operacaoExistente = buscarPorId(id);

        // Atualiza a situação se for fornecida
        if (operacaoAtualizada.getSituacao() != null) {
            operacaoExistente.setSituacao(operacaoAtualizada.getSituacao());
        }

        // Atualiza o produto se for fornecido
        if (operacaoAtualizada.getProduto() != null && operacaoAtualizada.getProduto().getId() != null) {
            ProdutoEntity produto = produtoRepository.findById(operacaoAtualizada.getProduto().getId())
                    .orElseThrow(() -> new RegraNegocioException("Produto não encontrado com ID: " + operacaoAtualizada.getProduto().getId()));
            operacaoExistente.setProduto(produto);
        }

        // Atualiza o usuário se for fornecido
        if (operacaoAtualizada.getUsuario() != null && operacaoAtualizada.getUsuario().getId() != null) {
            UsuarioEntity usuario = usuarioRepository.findById(operacaoAtualizada.getUsuario().getId())
                    .orElseThrow(() -> new RegraNegocioException("Usuário não encontrado com ID: " + operacaoAtualizada.getUsuario().getId()));
            operacaoExistente.setUsuario(usuario);
        }

        return repository.save(operacaoExistente);
    }
    private void validarOperacao(OperacaoEntity operacao) {
        if (operacao.getProduto() == null || operacao.getProduto().getId() == null) {
            throw new RegraNegocioException("Produto é obrigatório!");
        }
        if (operacao.getUsuario() == null || operacao.getUsuario().getId() == null) {
            throw new RegraNegocioException("Usuário é obrigatório!");
        }
        if (operacao.getSituacao() == null) {
            throw new RegraNegocioException("Situação é obrigatória!");
        }
    }
}