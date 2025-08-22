import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CheckInRequest, CheckIn, CheckInUpdate, PageResponse } from '../models/check-in.model';

@Injectable({
  providedIn: 'root'
})
export class CheckInService {
  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient) { }

  // Métodos para Check-in
  realizarCheckIn(checkInRequest: CheckInRequest): Observable<CheckIn> {
    return this.http.post<CheckIn>(`${this.apiUrl}/check-in`, checkInRequest);
  }

  buscarCheckInPorId(id: number): Observable<CheckIn> {
    return this.http.get<CheckIn>(`${this.apiUrl}/check-in/${id}`);
  }

  atualizarCheckIn(id: number, checkInUpdate: CheckInUpdate): Observable<CheckIn> {
    return this.http.put<CheckIn>(`${this.apiUrl}/check-in/${id}`, checkInUpdate);
  }

  deletarCheckIn(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/check-in/${id}`);
  }

  listarTodosCheckIns(pagina: number = 0, tamanho: number = 10): Observable<PageResponse<CheckIn>> {
    const params = new HttpParams()
      .set('pagina', pagina.toString())
      .set('tamanho', tamanho.toString());
    return this.http.get<PageResponse<CheckIn>>(`${this.apiUrl}/check-in`, { params });
  }

  buscarCheckInsPorPessoa(pessoaId: number, pagina: number = 0, tamanho: number = 10): Observable<PageResponse<CheckIn>> {
    const params = new HttpParams()
      .set('pagina', pagina.toString())
      .set('tamanho', tamanho.toString());
    return this.http.get<PageResponse<CheckIn>>(`${this.apiUrl}/check-in/pessoas/${pessoaId}`, { params });
  }

  buscarHospedesAtivos(pagina: number = 0, tamanho: number = 10): Observable<PageResponse<CheckIn>> {
    const params = new HttpParams()
      .set('pagina', pagina.toString())
      .set('tamanho', tamanho.toString());
    return this.http.get<PageResponse<CheckIn>>(`${this.apiUrl}/check-in/hospedes/ativos`, { params });
  }

  buscarHospedesFinalizados(pagina: number = 0, tamanho: number = 10): Observable<PageResponse<CheckIn>> {
    const params = new HttpParams()
      .set('pagina', pagina.toString())
      .set('tamanho', tamanho.toString());
    return this.http.get<PageResponse<CheckIn>>(`${this.apiUrl}/check-in/hospedes/finalizados`, { params });
  }

  buscarHospedesAtivosPorPessoa(pessoaId: number, pagina: number = 0, tamanho: number = 10): Observable<PageResponse<CheckIn>> {
    const params = new HttpParams()
      .set('pagina', pagina.toString())
      .set('tamanho', tamanho.toString());
    return this.http.get<PageResponse<CheckIn>>(`${this.apiUrl}/check-in/pessoas/${pessoaId}/hospedes/ativos`, { params });
  }

  buscarHospedesFinalizadosPorPessoa(pessoaId: number, pagina: number = 0, tamanho: number = 10): Observable<PageResponse<CheckIn>> {
    const params = new HttpParams()
      .set('pagina', pagina.toString())
      .set('tamanho', tamanho.toString());
    return this.http.get<PageResponse<CheckIn>>(`${this.apiUrl}/check-in/pessoas/${pessoaId}/hospedes/finalizados`, { params });
  }

  buscarCheckInAtivoPorPessoa(pessoaId: number): Observable<CheckIn> {
    return this.http.get<CheckIn>(`${this.apiUrl}/check-in/pessoas/${pessoaId}/check-in/ativo`);
  }
}
