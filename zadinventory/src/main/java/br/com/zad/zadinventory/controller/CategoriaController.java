package br.com.zad.zadinventory.controller;

import br.com.zad.zadinventory.model.entity.CategoriaEntity;
import br.com.zad.zadinventory.model.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService service;

    // CREATE (POST)
    @PostMapping
    public ResponseEntity<CategoriaEntity> cadastrarCategoria(@RequestBody CategoriaEntity categoria) {
        return ResponseEntity.ok(service.salvar(categoria));
    }

    // READ ALL (GET)
    @GetMapping
    public ResponseEntity<List<CategoriaEntity>> listarCategorias() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    // READ BY ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaEntity> buscarCategoriaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // READ NAMES (GET)
    @GetMapping("/nomes")
    public ResponseEntity<List<String>> buscarTodosNomes() {
        return ResponseEntity.ok(service.buscarTodosNomes());
    }

    // GET ID BY NAME (GET)
    @GetMapping("/id-por-nome")
    public ResponseEntity<Long> buscarIdPorNome(@RequestParam String nome) {
        return ResponseEntity.ok(service.buscarIdPorNome(nome));
    }

    // UPDATE (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaEntity> atualizarCategoria(
            @PathVariable Long id,
            @RequestBody CategoriaEntity categoria) {
        return ResponseEntity.ok(service.atualizar(id, categoria));
    }

    // DELETE (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerCategoria(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}