package app.zad.zadinventory.unit.service;

import app.zad.zadinventory.model.entity.TagEntity;
import app.zad.zadinventory.model.exception.RegraNegocioException;
import app.zad.zadinventory.model.repository.TagRepository;
import app.zad.zadinventory.model.service.TagService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("TESTE DE UNIDADE - TagService")
class TagServiceUnitTest {

    @Mock
    private TagRepository repository;

    @InjectMocks
    private TagService service;

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com dados válidos deve salvar tag com sucesso")
    void deveSalvarTagComDadosValidos() {
        // Arrange
        TagEntity tag = TagEntity.builder()
                .nome("Nova Tag")
                .build();

        TagEntity tagSalva = TagEntity.builder()
                .id(1L)
                .nome("Nova Tag")
                .build();

        when(repository.findByNomeIgnoreCase(anyString())).thenReturn(Optional.empty());
        when(repository.save(any(TagEntity.class))).thenReturn(tagSalva);

        // Act
        TagEntity resultado = service.salvar(tag);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Nova Tag", resultado.getNome());
        verify(repository, times(1)).findByNomeIgnoreCase("Nova Tag");
        verify(repository, times(1)).save(tag);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com nome vazio deve lançar RegraNegocioException")
    void deveLancarExcecaoQuandoNomeForVazio() {
        // Arrange
        TagEntity tag = TagEntity.builder()
                .nome("")
                .build();

        // Act & Assert
        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> service.salvar(tag)
        );

        assertEquals("Nome da tag é obrigatório!", exception.getMessage());
        verify(repository, never()).findByNomeIgnoreCase(anyString());
        verify(repository, never()).save(any(TagEntity.class));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com nome duplicado deve lançar RegraNegocioException")
    void deveLancarExcecaoQuandoNomeForDuplicado() {
        // Arrange
        TagEntity tagExistente = TagEntity.builder()
                .id(1L)
                .nome("Tag Existente")
                .build();

        TagEntity novaTag = TagEntity.builder()
                .nome("Tag Existente")
                .build();

        when(repository.findByNomeIgnoreCase("Tag Existente")).thenReturn(Optional.of(tagExistente));

        // Act & Assert
        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> service.salvar(novaTag)
        );

        assertEquals("Já existe uma tag com este nome!", exception.getMessage());
        verify(repository, times(1)).findByNomeIgnoreCase("Tag Existente");
        verify(repository, never()).save(any(TagEntity.class));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com ID existente deve retornar tag")
    void deveBuscarTagPorIdExistente() {
        // Arrange
        TagEntity tag = TagEntity.builder()
                .id(1L)
                .nome("Tag Teste")
                .build();

        when(repository.findComProdutosById(1L)).thenReturn(Optional.of(tag));

        // Act
        TagEntity resultado = service.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Tag Teste", resultado.getNome());
        verify(repository, times(1)).findComProdutosById(1L);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com ID inexistente deve lançar RegraNegocioException")
    void deveLancarExcecaoQuandoIdNaoExistir() {
        // Arrange
        when(repository.findComProdutosById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> service.buscarPorId(99L)
        );

        assertEquals("Tag não encontrada!", exception.getMessage());
        verify(repository, times(1)).findComProdutosById(99L);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Deve retornar lista de tags ao buscar por nome")
    void deveBuscarTagsPorNome() {
        // Arrange
        List<TagEntity> tags = Arrays.asList(
                TagEntity.builder().id(1L).nome("Tag 1").build(),
                TagEntity.builder().id(2L).nome("Tag 2").build()
        );

        when(repository.findByNomeContainingIgnoreCase("Tag")).thenReturn(tags);

        // Act
        List<TagEntity> resultado = service.buscarPorNome("Tag");

        // Assert
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findByNomeContainingIgnoreCase("Tag");
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Deve retornar lista vazia quando não encontrar tags por termo")
    void deveRetornarListaVaziaQuandoNaoEncontrarPorTermo() {
        // Arrange
        when(repository.buscarPorTermo("inexistente")).thenReturn(Arrays.asList());

        // Act
        List<TagEntity> resultado = service.buscarPorTermo("inexistente");

        // Assert
        assertTrue(resultado.isEmpty());
        verify(repository, times(1)).buscarPorTermo("inexistente");
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Deve remover tag com ID existente")
    void deveRemoverTagComIdExistente() {
        // Act
        service.remover(1L);

        // Assert
        verify(repository, times(1)).deleteById(1L);
    }
}