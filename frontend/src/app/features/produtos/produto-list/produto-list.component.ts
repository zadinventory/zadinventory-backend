import { Component, OnInit, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { RouterModule } from '@angular/router';
import { ProdutosService } from '../../../core/services/produtos.service';
import { Produto } from '../../../shared/models/produto';

@Component({
  selector: 'app-produto-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './produto-list.component.html'
})
export class ProdutoListComponent implements OnInit {
  private service = inject(ProdutosService);
  private router = inject(Router);

  produtos: Produto[] = [];

  ngOnInit(): void {
    this.carregar();
  }

  trackById(index: number, item: Produto): number {
    return item.id!;
  }

  carregar() {
    this.service.listar().subscribe({
      next: (data) => (this.produtos = data),
      error: (err) => console.error('Erro ao carregar produtos', err)
    });
  }

  novo() {
    this.router.navigate(['/produtos/novo']);
  }

  editar(produto: Produto) {
    this.router.navigate(['/produtos/editar', produto.id]);
  }

  excluir(produto: Produto) {
    if (confirm(`Deseja excluir o produto ${produto.nome}?`)) {
      this.service.deletar(produto.id!).subscribe({
        next: () => this.carregar(),
        error: (err) => console.error('Erro ao excluir produto', err)
      });
    }
  }
}
