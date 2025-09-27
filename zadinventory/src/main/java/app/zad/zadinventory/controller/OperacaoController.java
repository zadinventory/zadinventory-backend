package app.zad.zadinventory.controller;

import app.zad.zadinventory.controller.dto.OperacoesDto;
import app.zad.zadinventory.controller.dto.OperacoesDTORequest;
import app.zad.zadinventory.model.entity.OperacaoEntity;
import app.zad.zadinventory.model.enums.Situacao;
import app.zad.zadinventory.model.repository.OperacaoRepository;
import app.zad.zadinventory.model.service.OperacaoService;
import app.zad.zadinventory.model.service.ProdutoService;
import app.zad.zadinventory.model.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/operacoes")
@RequiredArgsConstructor
public class OperacaoController {

    private final OperacaoService service;
    private final OperacaoRepository operacaoRepository;
    private final ProdutoService produtoService;
    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<OperacoesDto> criar(@RequestBody OperacoesDTORequest dto) {
        var produto = produtoService.buscarPorId(dto.produtoId());
        var usuario = usuarioService.buscarPorId(dto.usuarioId());

        var operacao = OperacaoEntity.builder()
                .produto(produto)
                .usuario(usuario)
                .situacao(Situacao.valueOf(dto.situacao().toUpperCase()))
                .diaOperacao(dto.diaOperacao())
                .build();

        var salva = service.salvar(operacao);

        return ResponseEntity.ok(
                new OperacoesDto(
                        salva.getId(),
                        salva.getSituacao().name(),
                        salva.getDiaOperacao()
                )
        );
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

    // NOVO MÉTODO PUT PARA ATUALIZAR OPERAÇÃO
    @PutMapping("/{id}")
    public ResponseEntity<OperacaoEntity> atualizar(
            @PathVariable Long id,
            @RequestBody OperacaoEntity operacao) {

        return ResponseEntity.ok(service.atualizar(id, operacao));
    }

    @PatchMapping("/{id}/situacao")
    public ResponseEntity<OperacaoEntity> atualizarSituacao(
            @PathVariable Long id,
            @RequestParam Situacao novaSituacao) {

        return ResponseEntity.ok(service.atualizarSituacao(id, novaSituacao));
    }
}