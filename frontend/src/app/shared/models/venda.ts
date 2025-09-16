import { Produto } from './produto';

export interface Venda {
  id?: number;
  data: Date;
  produtos: Produto[];   // lista de produtos vendidos
  total: number;         // valor total da venda
  usuarioId?: number;    // quem registrou a venda (opcional)
}
