package app.zad.zadinventory.controller;

import app.zad.zadinventory.model.entity.OperacaoEntity;
import app.zad.zadinventory.model.enums.Situacao;
import app.zad.zadinventory.model.service.OperacaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/operacoes")
@RequiredArgsConstructor
public class OperacaoController {

    private final OperacaoService service;

    @PostMapping
    public ResponseEntity<OperacaoEntity> criar(@RequestBody OperacaoEntity operacao) {
        return ResponseEntity.ok(service.salvar(operacao));
    }

    @GetMapping
    public ResponseEntity<List<OperacaoEntity>> listar() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperacaoEntity> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping("/por-situacao")
    public ResponseEntity<List<OperacaoEntity>> buscarPorSituacao(@RequestParam Situacao situacao) {
        return ResponseEntity.ok(service.buscarPorSituacao(situacao));
    }

    @GetMapping("/por-produto/{produtoId}")
    public ResponseEntity<List<OperacaoEntity>> buscarPorProduto(@PathVariable Long produtoId) {
        return ResponseEntity.ok(service.buscarPorProduto(produtoId));
    }

    @GetMapping("/por-usuario/{usuarioId}")
    public ResponseEntity<List<OperacaoEntity>> buscarPorUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(service.buscarPorUsuario(usuarioId));
    }

    @GetMapping("/recentes")
    public ResponseEntity<List<OperacaoEntity>> buscarRecentes(@RequestParam LocalDateTime data) {
        return ResponseEntity.ok(service.buscarRecentes(data));
    }

    @PatchMapping("/{id}/situacao")
    public ResponseEntity<OperacaoEntity> atualizarSituacao(
            @PathVariable Long id,
            @RequestParam Situacao novaSituacao) {

        return ResponseEntity.ok(service.atualizarSituacao(id, novaSituacao));
    }
}