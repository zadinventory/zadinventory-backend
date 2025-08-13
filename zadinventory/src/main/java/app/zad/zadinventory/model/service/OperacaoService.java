package app.zad.zadinventory.model.service;

import app.zad.zadinventory.model.entity.OperacaoEntity;
import app.zad.zadinventory.model.enums.Situacao;
import app.zad.zadinventory.model.exception.RegraNegocioException;
import app.zad.zadinventory.model.repository.OperacaoRepository;
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

        if (operacao.getData() == null) {
            operacao.setData(LocalDateTime.now());
        }

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

    public List<OperacaoEntity> buscarRecentes(LocalDateTime data) {
        return repository.findByDataAfter(data);
    }

    @Transactional
    public OperacaoEntity atualizarSituacao(Long id, Situacao novaSituacao) {
        OperacaoEntity operacao = buscarPorId(id);
        operacao.setSituacao(novaSituacao);
        return repository.save(operacao);
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