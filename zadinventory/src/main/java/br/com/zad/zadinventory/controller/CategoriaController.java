package br.com.zad.zadinventory.controller;

import br.com.zad.zadinventory.model.entity.CategoriaEntity;
import br.com.zad.zadinventory.model.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
public class CategoriaController {

    private final CategoriaService service;

    @PostMapping
    public ResponseEntity<CategoriaEntity> criar(@RequestBody CategoriaEntity categoria) {
        return ResponseEntity.ok(service.salvar(categoria));
    }

    @GetMapping
    public ResponseEntity<List<CategoriaEntity>> listar() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaEntity> buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nomes")
    public ResponseEntity<List<String>> listarNomes() {
        return ResponseEntity.ok(service.buscarTodosNomes());
    }

    @GetMapping("/buscar-id")
    public ResponseEntity<Optional<Long>> buscarIdPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(service.buscarIdPorNome(nome));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaEntity> atualizar(
            @PathVariable Long id,
            @RequestBody CategoriaEntity categoria) {
        return ResponseEntity.ok(service.atualizar(id, categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}