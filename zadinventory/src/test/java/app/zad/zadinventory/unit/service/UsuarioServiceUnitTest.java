package app.zad.zadinventory.unit.service;

import app.zad.zadinventory.model.entity.UsuarioEntity;
import app.zad.zadinventory.model.enums.TipoUsuario;
import app.zad.zadinventory.model.exception.RegraNegocioException;
import app.zad.zadinventory.model.repository.UsuarioRepository;
import app.zad.zadinventory.model.service.UsuarioService;
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
@DisplayName("TESTE DE UNIDADE - UsuarioService")
class UsuarioServiceUnitTest {

    @Mock
    private UsuarioRepository repository;

    @InjectMocks
    private UsuarioService service;

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com dados válidos deve salvar usuário com sucesso")
    void deveSalvarUsuarioComDadosValidos() {
        // Arrange
        UsuarioEntity usuario = UsuarioEntity.builder()
                .email("usuario@teste.com")
                .senha("senha123")
                .tipoUsuario(TipoUsuario.FUNCIONARIO)
                .build();

        UsuarioEntity usuarioSalvo = UsuarioEntity.builder()
                .id(1L)
                .email("usuario@teste.com")
                .senha("senha123")
                .tipoUsuario(TipoUsuario.FUNCIONARIO)
                .build();

        when(repository.existsByEmail(anyString())).thenReturn(false);
        when(repository.save(any(UsuarioEntity.class))).thenReturn(usuarioSalvo);

        // Act
        UsuarioEntity resultado = service.salvar(usuario);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("usuario@teste.com", resultado.getEmail());
        verify(repository, times(1)).existsByEmail("usuario@teste.com");
        verify(repository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com email vazio deve lançar RegraNegocioException")
    void deveLancarExcecaoQuandoEmailForVazio() {
        // Arrange
        UsuarioEntity usuario = UsuarioEntity.builder()
                .email("")
                .senha("senha123")
                .tipoUsuario(TipoUsuario.FUNCIONARIO)
                .build();

        // Act & Assert
        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> service.salvar(usuario)
        );

        assertEquals("Email é obrigatório!", exception.getMessage());
        verify(repository, never()).existsByEmail(anyString());
        verify(repository, never()).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com senha vazia deve lançar RegraNegocioException")
    void deveLancarExcecaoQuandoSenhaForVazia() {
        // Arrange
        UsuarioEntity usuario = UsuarioEntity.builder()
                .email("teste@email.com")
                .senha("")
                .tipoUsuario(TipoUsuario.FUNCIONARIO)
                .build();

        // Act & Assert
        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> service.salvar(usuario)
        );

        assertEquals("Senha é obrigatória!", exception.getMessage());
        verify(repository, never()).existsByEmail(anyString());
        verify(repository, never()).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com tipo usuário nulo deve lançar RegraNegocioException")
    void deveLancarExcecaoQuandoTipoUsuarioForNulo() {
        // Arrange
        UsuarioEntity usuario = UsuarioEntity.builder()
                .email("teste@email.com")
                .senha("senha123")
                .tipoUsuario(null)
                .build();

        // Act & Assert
        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> service.salvar(usuario)
        );

        assertEquals("Tipo de usuário é obrigatório!", exception.getMessage());
        verify(repository, never()).existsByEmail(anyString());
        verify(repository, never()).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com email duplicado deve lançar RegraNegocioException")
    void deveLancarExcecaoQuandoEmailForDuplicado() {
        // Arrange
        UsuarioEntity usuario = UsuarioEntity.builder()
                .email("existente@email.com")
                .senha("senha123")
                .tipoUsuario(TipoUsuario.FUNCIONARIO)
                .build();

        when(repository.existsByEmail("existente@email.com")).thenReturn(true);

        // Act & Assert
        RegraNegocioException exception = assertThrows(
                RegraNegocioException.class,
                () -> service.salvar(usuario)
        );

        assertEquals("Email já está em uso!", exception.getMessage());
        verify(repository, times(1)).existsByEmail("existente@email.com");
        verify(repository, never()).save(any(UsuarioEntity.class));
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário com ID existente deve retornar usuário")
    void deveBuscarUsuarioPorIdExistente() {
        // Arrange
        UsuarioEntity usuario = UsuarioEntity.builder()
                .id(1L)
                .email("teste@email.com")
                .senha("senha123")
                .tipoUsuario(TipoUsuario.FUNCIONARIO)
                .build();

        when(repository.findComProdutosById(1L)).thenReturn(Optional.of(usuario));

        // Act
        UsuarioEntity resultado = service.buscarPorId(1L);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("teste@email.com", resultado.getEmail());
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

        assertEquals("Usuário não encontrado!", exception.getMessage());
        verify(repository, times(1)).findComProdutosById(99L);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Deve retornar lista de usuários ao buscar por tipo")
    void deveBuscarUsuariosPorTipo() {
        // Arrange
        List<UsuarioEntity> usuarios = Arrays.asList(
                UsuarioEntity.builder().id(1L).email("func1@email.com").tipoUsuario(TipoUsuario.FUNCIONARIO).build(),
                UsuarioEntity.builder().id(2L).email("func2@email.com").tipoUsuario(TipoUsuario.FUNCIONARIO).build()
        );

        when(repository.findByTipoUsuario(TipoUsuario.FUNCIONARIO)).thenReturn(usuarios);

        // Act
        List<UsuarioEntity> resultado = service.buscarPorTipo(TipoUsuario.FUNCIONARIO);

        // Assert
        assertEquals(2, resultado.size());
        verify(repository, times(1)).findByTipoUsuario(TipoUsuario.FUNCIONARIO);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Deve retornar usuário ao buscar por email")
    void deveBuscarUsuarioPorEmail() {
        // Arrange
        UsuarioEntity usuario = UsuarioEntity.builder()
                .id(1L)
                .email("teste@email.com")
                .senha("senha123")
                .tipoUsuario(TipoUsuario.FUNCIONARIO)
                .build();

        when(repository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));

        // Act
        UsuarioEntity resultado = service.buscarPorEmail("teste@email.com");

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("teste@email.com", resultado.getEmail());
        verify(repository, times(1)).findByEmail("teste@email.com");
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Deve remover usuário com ID existente")
    void deveRemoverUsuarioComIdExistente() {
        // Act
        service.remover(1L);

        // Assert
        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário de atualização com dados válidos deve atualizar usuário")
    void deveAtualizarUsuarioComDadosValidos() {
        // Arrange
        UsuarioEntity usuarioExistente = UsuarioEntity.builder()
                .id(1L)
                .email("antigo@email.com")
                .senha("senhaAntiga")
                .tipoUsuario(TipoUsuario.FUNCIONARIO)
                .build();

        UsuarioEntity usuarioAtualizado = UsuarioEntity.builder()
                .email("novo@email.com")
                .senha("novaSenha")
                .tipoUsuario(TipoUsuario.GERENTE)
                .build();

        UsuarioEntity usuarioSalvo = UsuarioEntity.builder()
                .id(1L)
                .email("novo@email.com")
                .senha("novaSenha")
                .tipoUsuario(TipoUsuario.GERENTE)
                .build();

        when(repository.findComProdutosById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(repository.save(any(UsuarioEntity.class))).thenReturn(usuarioSalvo);

        // Act
        UsuarioEntity resultado = service.atualizar(1L, usuarioAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("novo@email.com", resultado.getEmail());
        assertEquals(TipoUsuario.GERENTE, resultado.getTipoUsuario());
        verify(repository, times(1)).findComProdutosById(1L);
        verify(repository, times(1)).save(usuarioExistente);
    }

    @Test
    @DisplayName("TESTE DE UNIDADE - Cenário de atualização sem senha deve manter senha antiga")
    void deveManterSenhaAntigaQuandoNovaSenhaForNula() {
        // Arrange
        UsuarioEntity usuarioExistente = UsuarioEntity.builder()
                .id(1L)
                .email("antigo@email.com")
                .senha("senhaAntiga")
                .tipoUsuario(TipoUsuario.FUNCIONARIO)
                .build();

        UsuarioEntity usuarioAtualizado = UsuarioEntity.builder()
                .email("novo@email.com")
                .senha(null) // Senha não fornecida
                .tipoUsuario(TipoUsuario.GERENTE)
                .build();

        UsuarioEntity usuarioSalvo = UsuarioEntity.builder()
                .id(1L)
                .email("novo@email.com")
                .senha("senhaAntiga") // Manteve a senha antiga
                .tipoUsuario(TipoUsuario.GERENTE)
                .build();

        when(repository.findComProdutosById(1L)).thenReturn(Optional.of(usuarioExistente));
        when(repository.save(any(UsuarioEntity.class))).thenReturn(usuarioSalvo);

        // Act
        UsuarioEntity resultado = service.atualizar(1L, usuarioAtualizado);

        // Assert
        assertNotNull(resultado);
        assertEquals("senhaAntiga", resultado.getSenha());
        verify(repository, times(1)).findComProdutosById(1L);
        verify(repository, times(1)).save(usuarioExistente);
    }
}