package app.zad.zadinventory.Integration.controller;

import app.zad.zadinventory.model.entity.CategoriaEntity;
import app.zad.zadinventory.model.service.CategoriaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CategoriaControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CategoriaService service;

    private CategoriaEntity categoria;

    @BeforeEach
    void setUp() {
        categoria = new CategoriaEntity();
        categoria.setId(1L);
        categoria.setNome("Eletrônicos");
        categoria.setDescricao("Categoria de produtos eletrônicos");
    }

    // ---------- POST ----------
    @Test
    void deveSalvarCategoriaCorretamente() throws Exception {
        when(service.salvar(any(CategoriaEntity.class))).thenReturn(categoria);

        mockMvc.perform(post("/api/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoria)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("Eletrônicos"))
                .andExpect(jsonPath("$.descricao").value("Categoria de produtos eletrônicos"));
    }

    // ---------- GET /api/categorias ----------
    @Test
    void deveListarTodasAsCategorias() throws Exception {
        when(service.buscarTodos()).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("Eletrônicos"));
    }

    // ---------- GET /api/categorias/{id} ----------
    @Test
    void deveBuscarCategoriaPorId() throws Exception {
        when(service.buscarPorId(1L)).thenReturn(Optional.of(categoria));

        mockMvc.perform(get("/api/categorias/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Eletrônicos"));
    }

    @Test
    void deveRetornarNotFoundQuandoCategoriaNaoExistir() throws Exception {
        when(service.buscarPorId(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categorias/99"))
                .andExpect(status().isNotFound());
    }

    // ---------- GET /api/categorias/{id}/com-produtos ----------
    @Test
    void deveBuscarCategoriaComProdutos() throws Exception {
        when(service.buscarComProdutosPorId(1L)).thenReturn(Optional.of(categoria));

        mockMvc.perform(get("/api/categorias/1/com-produtos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void deveRetornarNotFoundAoBuscarCategoriaComProdutosInexistente() throws Exception {
        when(service.buscarComProdutosPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/categorias/1/com-produtos"))
                .andExpect(status().isNotFound());
    }

    // ---------- GET /api/categorias/buscar?nome= ----------
    @Test
    void deveBuscarCategoriasPorNome() throws Exception {
        when(service.buscarPorNome("Eletrônicos")).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias/buscar")
                        .param("nome", "Eletrônicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Eletrônicos"));
    }

    // ---------- GET /api/categorias/buscar-termo?termo= ----------
    @Test
    void deveBuscarCategoriasPorTermo() throws Exception {
        when(service.buscarPorTermo("ele")).thenReturn(List.of(categoria));

        mockMvc.perform(get("/api/categorias/buscar-termo")
                        .param("termo", "ele"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Eletrônicos"));
    }

    // ---------- PUT /api/categorias/{id} ----------
    @Test
    void deveAtualizarCategoria() throws Exception {
        CategoriaEntity categoriaAtualizada = new CategoriaEntity();
        categoriaAtualizada.setId(1L);
        categoriaAtualizada.setNome("Informática");
        categoriaAtualizada.setDescricao("Produtos de informática");

        when(service.atualizar(eq(1L), any(CategoriaEntity.class))).thenReturn(categoriaAtualizada);

        mockMvc.perform(put("/api/categorias/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoriaAtualizada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Informática"))
                .andExpect(jsonPath("$.descricao").value("Produtos de informática"));
    }

    // ---------- DELETE /api/categorias/{id} ----------
    @Test
    void deveDeletarCategoria() throws Exception {
        doNothing().when(service).deletar(1L);

        mockMvc.perform(delete("/api/categorias/1"))
                .andExpect(status().isNoContent());
    }
}
