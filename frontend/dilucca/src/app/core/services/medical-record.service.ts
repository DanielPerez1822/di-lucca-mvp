import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { CreateMedicalRecordRequest, MedicalRecord } from '../models/medical-record.model';

@Injectable({
  providedIn: 'root'
})
export class MedicalRecordService {

  private readonly API_URL = 'http://localhost:9000/api/medical-records';

  constructor(private http: HttpClient) {}

  getPatientHistory(patientId: string): Observable<MedicalRecord[]> {
    return this.http.get<MedicalRecord[]>(`${this.API_URL}/patient/${patientId}`);
  }

  getMedicalRecordById(id: string): Observable<MedicalRecord> {
    return this.http.get<MedicalRecord>(`${this.API_URL}/${id}`);
  }

  createMedicalRecord(request: CreateMedicalRecordRequest): Observable<MedicalRecord> {
    return this.http.post<MedicalRecord>(this.API_URL, request);
  }

  updateMedicalRecord(id: string, request: Partial<CreateMedicalRecordRequest>): Observable<MedicalRecord> {
    return this.http.put<MedicalRecord>(`${this.API_URL}/${id}`, request);
  }
}
