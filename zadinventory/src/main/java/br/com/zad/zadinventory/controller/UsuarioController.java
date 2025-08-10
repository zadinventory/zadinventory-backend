package br.com.zad.zadinventory.controller;

import br.com.zad.zadinventory.model.entity.UsuarioEntity;
import br.com.zad.zadinventory.model.enums.TipoUsuario;
import br.com.zad.zadinventory.model.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;

    @PostMapping
    public ResponseEntity<UsuarioEntity> criar(@RequestBody UsuarioEntity usuario) {
        return ResponseEntity.ok(service.salvar(usuario));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioEntity>> listar() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/por-email")
    public ResponseEntity<UsuarioEntity> buscarPorEmail(@RequestParam String email) {
        return ResponseEntity.ok(service.buscarPorEmail(email));
    }

    @GetMapping("/por-tipo")
    public ResponseEntity<List<UsuarioEntity>> buscarPorTipo(@RequestParam TipoUsuario tipo) {
        return ResponseEntity.ok(service.buscarPorTipo(tipo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UsuarioEntity> atualizar(
            @PathVariable Long id,
            @RequestBody UsuarioEntity usuario) {
        return ResponseEntity.ok(service.atualizar(id, usuario));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}