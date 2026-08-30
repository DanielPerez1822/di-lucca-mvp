import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreatePatientRequest, Patient, UpdatePatientRequest } from '../models/patient.model';

@Injectable({
  providedIn: 'root'
})
export class PatientService {

  private readonly API_URL = 'http://localhost:9000/api/patients';

  constructor(private http: HttpClient) {}

  getPatients(search?: string, activeOnly: boolean = false): Observable<Patient[]> {
    let params = new HttpParams();
    if (search) {
      params = params.set('search', search);
    }
    if (activeOnly) {
      params = params.set('activeOnly', 'true');
    }
    return this.http.get<Patient[]>(this.API_URL, { params });
  }

  getPatientById(id: string): Observable<Patient> {
    return this.http.get<Patient>(`${this.API_URL}/${id}`);
  }

  createPatient(request: CreatePatientRequest): Observable<Patient> {
    return this.http.post<Patient>(this.API_URL, request);
  }

  updatePatient(id: string, request: UpdatePatientRequest): Observable<Patient> {
    return this.http.put<Patient>(`${this.API_URL}/${id}`, request);
  }

  deletePatient(id: string): Observable<void> {
    return this.http.delete<void>(`${this.API_URL}/${id}`);
  }
}
