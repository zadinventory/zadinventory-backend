import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout.component';

export const routes: Routes = [
  {
    path: '',
    component: MainLayoutComponent,
    children: [
      {
        path: 'produtos',
        loadComponent: () =>
          import(
            './features/produtos/produto-list/produto-list.component'
          ).then((m) => m.ProdutoListComponent),
      },
      {
        path: 'produtos/novo',
        loadComponent: () =>
          import(
            './features/produtos/produto-form/produto-form.component'
          ).then((m) => m.ProdutoFormComponent),
      },
      {
        path: 'vendas',
        loadComponent: () =>
          import('./features/vendas/venda-list/venda-list.component').then(
            (m) => m.VendaListComponent
          ),
      },
      {
        path: 'usuarios',
        loadComponent: () =>
          import(
            './features/usuarios/usuario-list/usuario-list.component'
          ).then((m) => m.UsuarioListComponent),
      },
      { path: '', redirectTo: 'produtos', pathMatch: 'full' },
    ],
  },
  {
    path: 'login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then(
        (m) => m.LoginComponent
      ),
  },
  { path: '**', redirectTo: 'produtos' },
];
