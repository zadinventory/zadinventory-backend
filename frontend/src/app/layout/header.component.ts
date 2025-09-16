import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [RouterLink],
  template: `
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
      <div class="container-fluid">
        <a class="navbar-brand" routerLink="/">Mega Inventory</a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <ul class="navbar-nav ms-auto">
            <li class="nav-item">
              <a class="nav-link" routerLink="/produtos">Produtos</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/vendas">Vendas</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/usuarios">Usuários</a>
            </li>
            <li class="nav-item">
              <a class="nav-link" routerLink="/login">Sair</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
    <!-- espaço para não sobrepor conteúdo -->
    <div style="height: 56px;"></div>
  `
})
export class HeaderComponent {}
