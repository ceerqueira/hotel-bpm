import { Pessoa } from './pessoa.model';

export interface CheckInRequest {
  pessoa: Pessoa;
  dataEntrada: string;
  dataSaidaPrevista: string;
  adicionalVeiculo: boolean;
}

export interface CheckInUpdate {
  dataEntrada: string;
  dataSaidaPrevista: string;
  adicionalVeiculo: boolean;
}

export interface CheckIn {
  id?: number;
  pessoa: Pessoa;
  dataEntrada: string;
  dataSaidaPrevista: string;
  adicionalVeiculo: boolean;
  valorTotal?: number;
}

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
  first: boolean;
  last: boolean;
}
