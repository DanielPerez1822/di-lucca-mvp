import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateProcedureRequest, Procedure, UpdateProcedureRequest } from '../models/procedure.model';

@Injectable({
  providedIn: 'root'
})
export class ProcedureService {

  private readonly API_URL = 'http://localhost:9000/api/procedures';

  constructor(private http: HttpClient) {}

  getProcedures(search?: string, activeOnly: boolean = false): Observable<Procedure[]> {
    let params = new HttpParams();
    if (search) {
      params = params.set('search', search);
    }
    if (activeOnly) {
      params = params.set('activeOnly', 'true');
    }
    return this.http.get<Procedure[]>(this.API_URL, { params });
  }

  getProcedureById(id: string): Observable<Procedure> {
    return this.http.get<Procedure>(`${this.API_URL}/${id}`);
  }

  createProcedure(request: CreateProcedureRequest): Observable<Procedure> {
    return this.http.post<Procedure>(this.API_URL, request);
  }

  updateProcedure(id: string, request: UpdateProcedureRequest): Observable<Procedure> {
    return this.http.put<Procedure>(`${this.API_URL}/${id}`, request);
  }

  deleteProcedure(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
