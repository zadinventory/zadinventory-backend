package br.com.zad.zadinventory.model.service;

import br.com.zad.zadinventory.model.entity.OperacaoEntity;
import br.com.zad.zadinventory.model.enums.Situacao;
import br.com.zad.zadinventory.model.repository.OperacaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OperacaoService {

    private final OperacaoRepository repository;

    @Transactional
    public OperacaoEntity salvar(OperacaoEntity operacao) {
        validarOperacao(operacao);
        return repository.save(operacao);
    }

    public List<OperacaoEntity> buscarTodos() {
        return repository.findAll();
    }

    public OperacaoEntity buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Operação não encontrada!"));
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

    private void validarOperacao(OperacaoEntity operacao) {
        if (operacao.getProduto() == null || operacao.getProduto().getId() == null) {
            throw new IllegalArgumentException("Produto é obrigatório!");
        }
        if (operacao.getUsuario() == null || operacao.getUsuario().getId() == null) {
            throw new IllegalArgumentException("Usuário é obrigatório!");
        }
        if (operacao.getSituacao() == null) {
            throw new IllegalArgumentException("Situação é obrigatória!");
        }
        if (operacao.getData() == null) {
            operacao.setData(LocalDateTime.now());
        }
    }
}