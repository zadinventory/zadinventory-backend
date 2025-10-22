package app.zad.zadinventory.unit.service;

import app.zad.zadinventory.controller.dto.OperacoesDTORequest;
import app.zad.zadinventory.controller.dto.RelatorioVendasProdutoDto;
import app.zad.zadinventory.controller.dto.TotalVendasDto;
import app.zad.zadinventory.model.entity.OperacaoEntity;
import app.zad.zadinventory.model.entity.ProdutoEntity;
import app.zad.zadinventory.model.entity.UsuarioEntity;
import app.zad.zadinventory.model.enums.Situacao;
import app.zad.zadinventory.model.enums.TipoUsuario;
import app.zad.zadinventory.model.exception.RegraNegocioException;
import app.zad.zadinventory.model.repository.OperacaoRepository;
import app.zad.zadinventory.model.repository.ProdutoRepository;
import app.zad.zadinventory.model.repository.UsuarioRepository;
import app.zad.zadinventory.model.service.OperacaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TESTE DE UNIDADE - OperacaoService")
class OperacaoServiceUnitTest {

    @Mock
    private OperacaoRepository operacaoRepository;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private OperacaoService service;

    @Test
    @DisplayName("Deve salvar operação com situação REALIZADA e atualizar estoque")
    void deveSalvarOperacaoRealizadaComSucesso() {
        // Arrange
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Notebook");
        produto.setQuantidade(10);
        produto.setPreco(BigDecimal.valueOf(2500.0));

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setEmail("user@test.com");
        usuario.setTipoUsuario(TipoUsuario.FUNCIONARIO);

        OperacoesDTORequest dto = new OperacoesDTORequest(
                1L, 1L, "REALIZADA", LocalDate.now(), 2
        );

        OperacaoEntity operacaoSalva = new OperacaoEntity();
        operacaoSalva.setId(1L);
        operacaoSalva.setProduto(produto);
        operacaoSalva.setUsuario(usuario);
        operacaoSalva.setSituacao(Situacao.REALIZADA);
        operacaoSalva.setDiaOperacao(LocalDate.now());
        operacaoSalva.setQuantidade(2);
        operacaoSalva.setValorTotal(BigDecimal.valueOf(5000.0));

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(produtoRepository.save(any(ProdutoEntity.class))).thenReturn(produto);
        when(operacaoRepository.save(any(OperacaoEntity.class))).thenReturn(operacaoSalva);

        // Act
        OperacaoEntity resultado = service.salvar(dto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(Situacao.REALIZADA, resultado.getSituacao());
        assertEquals(BigDecimal.valueOf(5000.0), resultado.getValorTotal());
        verify(produtoRepository).findById(1L);
        verify(usuarioRepository).findById(1L);
        verify(produtoRepository).save(produto);
        verify(operacaoRepository).save(any(OperacaoEntity.class));
    }

    @Test
    @DisplayName("Deve salvar operação com situação CANCELADA sem atualizar estoque")
    void deveSalvarOperacaoCanceladaSemAtualizarEstoque() {
        // Arrange
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Notebook");
        produto.setQuantidade(10);
        produto.setPreco(BigDecimal.valueOf(2500.0));

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);
        usuario.setEmail("user@test.com");

        OperacoesDTORequest dto = new OperacoesDTORequest(
                1L, 1L, "CANCELADA", LocalDate.now(), 2
        );

        OperacaoEntity operacaoSalva = new OperacaoEntity();
        operacaoSalva.setId(1L);
        operacaoSalva.setSituacao(Situacao.CANCELADA);

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(operacaoRepository.save(any(OperacaoEntity.class))).thenReturn(operacaoSalva);

        // Act
        OperacaoEntity resultado = service.salvar(dto);

        // Assert
        assertEquals(Situacao.CANCELADA, resultado.getSituacao());
        verify(produtoRepository, never()).save(any(ProdutoEntity.class)); // Não deve atualizar estoque
        verify(operacaoRepository).save(any(OperacaoEntity.class));
    }

    @Test
    @DisplayName("Deve lançar exceção quando produto não for encontrado ao salvar")
    void deveLancarExcecaoQuandoProdutoNaoEncontrado() {
        // Arrange
        OperacoesDTORequest dto = new OperacoesDTORequest(
                99L, 1L, "REALIZADA", LocalDate.now(), 2
        );

        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> service.salvar(dto));

        assertEquals("Produto não encontrado com ID: 99", ex.getMessage());
        verify(produtoRepository).findById(99L);
        verify(usuarioRepository, never()).findById(any());
        verify(operacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando usuário não for encontrado ao salvar")
    void deveLancarExcecaoQuandoUsuarioNaoEncontrado() {
        // Arrange
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setQuantidade(10);

        OperacoesDTORequest dto = new OperacoesDTORequest(
                1L, 99L, "REALIZADA", LocalDate.now(), 2
        );

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> service.salvar(dto));

        assertEquals("Usuário não encontrado com ID: 99", ex.getMessage());
        verify(produtoRepository).findById(1L);
        verify(usuarioRepository).findById(99L);
        verify(operacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar exceção quando estoque for insuficiente")
    void deveLancarExcecaoQuandoEstoqueInsuficiente() {
        // Arrange
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Notebook");
        produto.setQuantidade(1); // Estoque baixo
        produto.setPreco(BigDecimal.valueOf(2500.0));

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);

        OperacoesDTORequest dto = new OperacoesDTORequest(
                1L, 1L, "REALIZADA", LocalDate.now(), 5 // Quantidade maior que estoque
        );

        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        // Act & Assert
        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> service.salvar(dto));

        assertEquals("Estoque insuficiente para o produto: Notebook", ex.getMessage());
        verify(produtoRepository, never()).save(any(ProdutoEntity.class));
        verify(operacaoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve buscar operação por ID existente")
    void deveBuscarOperacaoPorIdExistente() {
        // Arrange
        OperacaoEntity operacao = new OperacaoEntity();
        operacao.setId(1L);
        operacao.setSituacao(Situacao.REALIZADA);

        when(operacaoRepository.findComRelacionamentosById(1L)).thenReturn(Optional.of(operacao));

        // Act
        OperacaoEntity resultado = service.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        verify(operacaoRepository).findComRelacionamentosById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção quando operação não for encontrada por ID")
    void deveLancarExcecaoQuandoOperacaoNaoEncontradaPorId() {
        // Arrange
        when(operacaoRepository.findComRelacionamentosById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RegraNegocioException ex = assertThrows(RegraNegocioException.class,
                () -> service.buscarPorId(99L));

        assertEquals("Operação não encontrada!", ex.getMessage());
        verify(operacaoRepository).findComRelacionamentosById(99L);
    }

    @Test
    @DisplayName("Deve buscar todas as operações")
    void deveBuscarTodasAsOperacoes() {
        // Arrange
        OperacaoEntity operacao1 = new OperacaoEntity();
        operacao1.setId(1L);

        OperacaoEntity operacao2 = new OperacaoEntity();
        operacao2.setId(2L);

        List<OperacaoEntity> operacoes = List.of(operacao1, operacao2);

        when(operacaoRepository.findAll()).thenReturn(operacoes);

        // Act
        List<OperacaoEntity> resultado = service.buscarTodos();

        // Assert
        assertEquals(2, resultado.size());
        verify(operacaoRepository).findAll();
    }

    @Test
    @DisplayName("Deve buscar operações por situação")
    void deveBuscarOperacoesPorSituacao() {
        // Arrange
        OperacaoEntity operacao = new OperacaoEntity();
        operacao.setId(1L);
        operacao.setSituacao(Situacao.REALIZADA);

        List<OperacaoEntity> operacoes = List.of(operacao);

        when(operacaoRepository.findBySituacao(Situacao.REALIZADA)).thenReturn(operacoes);

        // Act
        List<OperacaoEntity> resultado = service.buscarPorSituacao(Situacao.REALIZADA);

        // Assert
        assertEquals(1, resultado.size());
        assertEquals(Situacao.REALIZADA, resultado.get(0).getSituacao());
        verify(operacaoRepository).findBySituacao(Situacao.REALIZADA);
    }

    @Test
    @DisplayName("Deve buscar operações por produto")
    void deveBuscarOperacoesPorProduto() {
        // Arrange
        OperacaoEntity operacao = new OperacaoEntity();
        operacao.setId(1L);

        List<OperacaoEntity> operacoes = List.of(operacao);

        when(operacaoRepository.findByProdutoId(1L)).thenReturn(operacoes);

        // Act
        List<OperacaoEntity> resultado = service.buscarPorProduto(1L);

        // Assert
        assertEquals(1, resultado.size());
        verify(operacaoRepository).findByProdutoId(1L);
    }

    @Test
    @DisplayName("Deve buscar operações por usuário")
    void deveBuscarOperacoesPorUsuario() {
        // Arrange
        OperacaoEntity operacao = new OperacaoEntity();
        operacao.setId(1L);

        List<OperacaoEntity> operacoes = List.of(operacao);

        when(operacaoRepository.findByUsuarioId(1L)).thenReturn(operacoes);

        // Act
        List<OperacaoEntity> resultado = service.buscarPorUsuario(1L);

        // Assert
        assertEquals(1, resultado.size());
        verify(operacaoRepository).findByUsuarioId(1L);
    }

    @Test
    @DisplayName("Deve atualizar situação da operação")
    void deveAtualizarSituacaoDaOperacao() {
        // Arrange
        OperacaoEntity operacao = new OperacaoEntity();
        operacao.setId(1L);
        operacao.setSituacao(Situacao.SEPARADA);

        when(operacaoRepository.findComRelacionamentosById(1L)).thenReturn(Optional.of(operacao));
        when(operacaoRepository.save(any(OperacaoEntity.class))).thenReturn(operacao);

        // Act
        OperacaoEntity resultado = service.atualizarSituacao(1L, Situacao.REALIZADA);

        // Assert
        assertEquals(Situacao.REALIZADA, resultado.getSituacao());
        verify(operacaoRepository).findComRelacionamentosById(1L);
        verify(operacaoRepository).save(operacao);
    }

    @Test
    @DisplayName("Deve atualizar operação com dados válidos")
    void deveAtualizarOperacaoComDadosValidos() {
        // Arrange
        ProdutoEntity produtoAtual = new ProdutoEntity();
        produtoAtual.setId(1L);
        produtoAtual.setNome("Produto Atual");
        produtoAtual.setQuantidade(10);
        produtoAtual.setPreco(BigDecimal.valueOf(100.0));

        ProdutoEntity novoProduto = new ProdutoEntity();
        novoProduto.setId(2L);
        novoProduto.setNome("Novo Produto");
        novoProduto.setQuantidade(5);
        novoProduto.setPreco(BigDecimal.valueOf(200.0));

        UsuarioEntity usuarioAtual = new UsuarioEntity();
        usuarioAtual.setId(1L);
        usuarioAtual.setEmail("atual@test.com");

        UsuarioEntity novoUsuario = new UsuarioEntity();
        novoUsuario.setId(2L);
        novoUsuario.setEmail("novo@test.com");

        OperacaoEntity operacaoExistente = new OperacaoEntity();
        operacaoExistente.setId(1L);
        operacaoExistente.setProduto(produtoAtual);
        operacaoExistente.setUsuario(usuarioAtual);
        operacaoExistente.setSituacao(Situacao.SEPARADA);
        operacaoExistente.setDiaOperacao(LocalDate.now().minusDays(1));
        operacaoExistente.setQuantidade(1);
        operacaoExistente.setValorTotal(BigDecimal.valueOf(100.0));

        OperacoesDTORequest dto = new OperacoesDTORequest(
                2L, 2L, "REALIZADA", LocalDate.now(), 2
        );

        when(operacaoRepository.findComRelacionamentosById(1L)).thenReturn(Optional.of(operacaoExistente));
        when(produtoRepository.findById(2L)).thenReturn(Optional.of(novoProduto));
        when(usuarioRepository.findById(2L)).thenReturn(Optional.of(novoUsuario));
        when(operacaoRepository.save(any(OperacaoEntity.class))).thenReturn(operacaoExistente);

        // Act
        OperacaoEntity resultado = service.atualizar(1L, dto);

        // Assert
        assertEquals(novoProduto, resultado.getProduto());
        assertEquals(novoUsuario, resultado.getUsuario());
        assertEquals(Situacao.REALIZADA, resultado.getSituacao());
        assertEquals(LocalDate.now(), resultado.getDiaOperacao());
        assertEquals(2, resultado.getQuantidade());
        assertEquals(BigDecimal.valueOf(400.0), resultado.getValorTotal()); // 2 * 200
        verify(operacaoRepository).findComRelacionamentosById(1L);
        verify(operacaoRepository).save(operacaoExistente);
    }

    @Test
    @DisplayName("Deve excluir operação existente")
    void deveExcluirOperacaoExistente() {
        // Arrange
        OperacaoEntity operacao = new OperacaoEntity();
        operacao.setId(1L);

        when(operacaoRepository.findComRelacionamentosById(1L)).thenReturn(Optional.of(operacao));
        doNothing().when(operacaoRepository).delete(operacao);

        // Act
        service.excluir(1L);

        // Assert
        verify(operacaoRepository).findComRelacionamentosById(1L);
        verify(operacaoRepository).delete(operacao);
    }

    @Test
    @DisplayName("Deve calcular total de vendas com sucesso")
    void deveCalcularTotalVendasComSucesso() {
        // Arrange
        LocalDate inicio = LocalDate.now().minusDays(30);
        LocalDate fim = LocalDate.now();

        when(operacaoRepository.somaQuantidadePorPeriodo(inicio, fim)).thenReturn(50L);
        when(operacaoRepository.somaValorTotalPorPeriodo(inicio, fim)).thenReturn(BigDecimal.valueOf(5000.0));

        // Act
        TotalVendasDto resultado = service.totalVendas(inicio, fim);

        // Assert
        assertEquals(50L, resultado.quantidadeTotal());
        assertEquals(BigDecimal.valueOf(5000.0), resultado.valorTotal());
        verify(operacaoRepository).somaQuantidadePorPeriodo(inicio, fim);
        verify(operacaoRepository).somaValorTotalPorPeriodo(inicio, fim);
    }

    @Test
    @DisplayName("Deve calcular total de vendas com valores nulos")
    void deveCalcularTotalVendasComValoresNulos() {
        // Arrange
        LocalDate inicio = LocalDate.now().minusDays(30);
        LocalDate fim = LocalDate.now();

        when(operacaoRepository.somaQuantidadePorPeriodo(inicio, fim)).thenReturn(null);
        when(operacaoRepository.somaValorTotalPorPeriodo(inicio, fim)).thenReturn(null);

        // Act
        TotalVendasDto resultado = service.totalVendas(inicio, fim);

        // Assert
        assertEquals(0L, resultado.quantidadeTotal());
        assertEquals(BigDecimal.ZERO, resultado.valorTotal());
        verify(operacaoRepository).somaQuantidadePorPeriodo(inicio, fim);
        verify(operacaoRepository).somaValorTotalPorPeriodo(inicio, fim);
    }

    @Test
    @DisplayName("Deve gerar relatório de vendas por produto")
    void deveGerarRelatorioVendasPorProduto() {
        // Arrange
        LocalDate inicio = LocalDate.now().minusDays(30);
        LocalDate fim = LocalDate.now();

        Object[] resultado1 = new Object[]{1L, "Notebook", 10L, BigDecimal.valueOf(25000.0)};
        Object[] resultado2 = new Object[]{2L, "Mouse", 25L, BigDecimal.valueOf(1250.0)};
        List<Object[]> resultados = List.of(resultado1, resultado2);

        when(operacaoRepository.relatorioVendasPorProduto(inicio, fim)).thenReturn(resultados);

        // Act
        List<RelatorioVendasProdutoDto> relatorio = service.relatorioVendasPorProduto(inicio, fim);

        // Assert
        assertEquals(2, relatorio.size());

        RelatorioVendasProdutoDto item1 = relatorio.get(0);
        assertEquals(1L, item1.produtoId());
        assertEquals("Notebook", item1.nomeProduto());
        assertEquals(10L, item1.quantidadeVendida());
        assertEquals(BigDecimal.valueOf(25000.0), item1.valorTotal());

        RelatorioVendasProdutoDto item2 = relatorio.get(1);
        assertEquals(2L, item2.produtoId());
        assertEquals("Mouse", item2.nomeProduto());
        assertEquals(25L, item2.quantidadeVendida());
        assertEquals(BigDecimal.valueOf(1250.0), item2.valorTotal());

        verify(operacaoRepository).relatorioVendasPorProduto(inicio, fim);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar operação com estoque insuficiente")
    void deveLancarExcecaoAoAtualizarComEstoqueInsuficiente() {
        // Arrange
        ProdutoEntity produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Produto");
        produto.setQuantidade(1); // Estoque baixo

        OperacaoEntity operacaoExistente = new OperacaoEntity();
        operacaoExistente.setId(1L);
        operacaoExistente.setProduto(produto);
        operacaoExistente.setSituacao(Situacao.SEPARADA);
        operacaoExistente.setQuantidade(1);

        OperacoesDTORequest dto = new OperacoesDTORequest(
                1L, 1L, "REALIZADA", LocalDate.now(), 5 // Quantidade maior que estoque
        );

        when(operacaoRepository.findComRelacionamentosById(1L)).thenReturn(Optional.of(operacaoExistente));

        // Act & Assert
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> service.atualizar(1L, dto));

        assertEquals("Estoque insuficiente para o produto: Produto", ex.getMessage());
        verify(operacaoRepository).findComRelacionamentosById(1L);
        verify(produtoRepository, never()).save(any());
        verify(operacaoRepository, never()).save(any());
    }
}