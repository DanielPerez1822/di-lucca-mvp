import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AvailabilitySlot, DentistSchedule, GenerateSlotsRequest, SetDentistScheduleRequest } from '../models/schedule.model';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class ScheduleService {

  private readonly API_URL = '/api/schedules';

  constructor(private http: HttpClient) {}

  getScheduleByDentist(dentistId: string): Observable<DentistSchedule> {
    return this.http.get<DentistSchedule>(`${this.API_URL}/dentist/${dentistId}`);
  }

  setSchedule(request: SetDentistScheduleRequest): Observable<DentistSchedule> {
    return this.http.post<DentistSchedule>(this.API_URL, request);
  }

  generateSlots(request: GenerateSlotsRequest): Observable<AvailabilitySlot[]> {
    return this.http.post<AvailabilitySlot[]>(`${this.API_URL}/generate-slots`, request);
  }

  getAvailableSlots(dentistId: string, date: string): Observable<AvailabilitySlot[]> {
    const params = new HttpParams()
      .set('dentistId', dentistId)
      .set('date', date);

    return this.http.get<AvailabilitySlot[]>(`${this.API_URL}/slots/available`, { params });
  }

  getDentists(): Observable<User[]> {
    return this.http.get<User[]>(`${this.API_URL}/dentists`);
  }

  copySchedule(sourceDentistId: string, targetDentistId?: string, copyToAll: boolean = false): Observable<DentistSchedule[]> {
    return this.http.post<DentistSchedule[]>(`${this.API_URL}/copy`, {
      sourceDentistId,
      targetDentistId: targetDentistId || null,
      copyToAll
    });
  }
}
