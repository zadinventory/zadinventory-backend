export interface LoginResponse {
  token: string;
  usuario: {
    id: number;
    nome: string;
    role: string;
  };
}
