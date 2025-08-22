import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pessoa } from '../models/pessoa.model';
import { PageResponse } from '../models/check-in.model';

@Injectable({
  providedIn: 'root'
})
export class PessoaService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  cadastrarPessoa(pessoa: Pessoa): Observable<Pessoa> {
    return this.http.post<Pessoa>(`${this.apiUrl}/pessoas`, pessoa);
  }

  buscarPessoas(termo?: string, pagina: number = 0, tamanho: number = 5): Observable<PageResponse<Pessoa>> {
    let params = new HttpParams()
      .set('pagina', pagina.toString())
      .set('tamanho', tamanho.toString());
    
    if (termo) {
      params = params.set('termo', termo);
    }
    return this.http.get<PageResponse<Pessoa>>(`${this.apiUrl}/pessoas`, { params });
  }

  buscarPessoaPorId(id: number): Observable<Pessoa> {
    return this.http.get<Pessoa>(`${this.apiUrl}/pessoas/${id}`);
  }

  atualizarPessoa(id: number, pessoa: Pessoa): Observable<Pessoa> {
    return this.http.put<Pessoa>(`${this.apiUrl}/pessoas/${id}`, pessoa);
  }

  deletarPessoa(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/pessoas/${id}`);
  }
}
