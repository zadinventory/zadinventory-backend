export interface Usuario {
  id?: number;
  nome: string;
  email: string;
  senha?: string;   // opcional, para não trafegar no front
  role: 'ADMIN' | 'FUNCIONARIO';
}
