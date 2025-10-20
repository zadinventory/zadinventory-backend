package app.zad.zadinventory.Integration.controller;

import app.zad.zadinventory.model.entity.ProdutoEntity;
import app.zad.zadinventory.model.service.ProdutoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProdutoControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProdutoService service;

    private ProdutoEntity produto;
    private List<ProdutoEntity> listaProdutos;

    @BeforeEach
    void setUp() {
        produto = new ProdutoEntity();
        produto.setId(1L);
        produto.setNome("Copo Stanley Tradicional");
        produto.setDescricao("Copo Stanley Tradicional de Teste");
        produto.setPreco(new BigDecimal("90.0"));
        produto.setQuantidade(10);

        ProdutoEntity produto2 = new ProdutoEntity();
        produto2.setId(2L);
        produto2.setNome("Copo Térmico Preto");
        produto2.setDescricao("Copo Térmico Preto de Teste");
        produto2.setPreco(new BigDecimal("120.0"));
        produto2.setQuantidade(5);

        listaProdutos = Arrays.asList(produto, produto2);
    }

    @Test
    void deveCriarProdutoCorretamente() throws Exception {
        when(service.salvar(any(ProdutoEntity.class))).thenReturn(produto);

        mockMvc.perform(post("/api/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Copo Stanley Tradicional"))
                .andExpect(jsonPath("$.preco").value(90.0));
    }

    @Test
    void deveListarTodosOsProdutos() throws Exception {
        when(service.buscarTodos()).thenReturn(listaProdutos);

        mockMvc.perform(get("/api/produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void deveBuscarProdutoPorId() throws Exception {
        when(service.buscarPorId(anyLong())).thenReturn(produto);

        mockMvc.perform(get("/api/produtos/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Copo Stanley Tradicional"));
    }

    @Test
    void deveBuscarProdutoPorNome() throws Exception {
        when(service.buscarPorNome(anyString())).thenReturn(listaProdutos);

        mockMvc.perform(get("/api/produtos/por-nome")
                        .param("nome", "Copo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Copo Stanley Tradicional"));
    }

    @Test
    void deveBuscarPorCategoria() throws Exception {
        when(service.buscarPorCategoria(anyLong())).thenReturn(listaProdutos);

        mockMvc.perform(get("/api/produtos/por-categoria/{categoriaId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[1].nome").value("Copo Térmico Preto"));
    }

    @Test
    void deveBuscarComBaixoEstoque() throws Exception {
        when(service.buscarComBaixoEstoque(any())).thenReturn(List.of(produto));

        mockMvc.perform(get("/api/produtos/baixo-estoque")
                        .param("quantidadeMinima", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void deveAtualizarProduto() throws Exception {
        when(service.atualizar(anyLong(), any(ProdutoEntity.class))).thenReturn(produto);

        mockMvc.perform(put("/api/produtos/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Copo Stanley Tradicional"));
    }

    @Test
    void deveRemoverProduto() throws Exception {
        doNothing().when(service).remover(anyLong());

        mockMvc.perform(delete("/api/produtos/{id}", 1L))
                .andExpect(status().isNoContent());
    }
}
