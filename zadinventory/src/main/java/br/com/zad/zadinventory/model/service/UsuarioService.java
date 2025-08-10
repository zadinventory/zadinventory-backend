package br.com.zad.zadinventory.model.service;

import br.com.zad.zadinventory.model.entity.UsuarioEntity;
import br.com.zad.zadinventory.model.enums.TipoUsuario;
import br.com.zad.zadinventory.model.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;

    @Transactional
    public UsuarioEntity salvar(UsuarioEntity usuario) {
        validarUsuario(usuario);
        return repository.save(usuario); // Senha armazenada como texto puro (apenas para testes)
    }

    public List<UsuarioEntity> buscarTodos() {
        return repository.findAll();
    }

    public UsuarioEntity buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
    }

    public UsuarioEntity buscarPorEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado!"));
    }

    public List<UsuarioEntity> buscarPorTipo(TipoUsuario tipo) {
        return repository.findByTipoUsuario(tipo);
    }

    @Transactional
    public UsuarioEntity atualizar(Long id, UsuarioEntity usuarioAtualizado) {
        UsuarioEntity usuarioExistente = buscarPorId(id);
        usuarioExistente.setEmail(usuarioAtualizado.getEmail());
        usuarioExistente.setTipoUsuario(usuarioAtualizado.getTipoUsuario());
        usuarioExistente.setSenha(usuarioAtualizado.getSenha()); // Atualiza senha como texto puro
        return repository.save(usuarioExistente);
    }

    @Transactional
    public void remover(Long id) {
        repository.deleteById(id);
    }

    private void validarUsuario(UsuarioEntity usuario) {
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email é obrigatório!");
        }
        if (usuario.getSenha() == null || usuario.getSenha().trim().isEmpty()) {
            throw new IllegalArgumentException("Senha é obrigatória!");
        }
        if (usuario.getTipoUsuario() == null) {
            throw new IllegalArgumentException("Tipo de usuário é obrigatório!");
        }
    }
}