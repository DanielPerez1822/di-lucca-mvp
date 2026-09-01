import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Appointment, AppointmentStatus, BookAppointmentRequest, CancelAppointmentRequest, RescheduleAppointmentRequest } from '../models/appointment.model';

@Injectable({
  providedIn: 'root'
})
export class AppointmentService {

  private readonly API_URL = '/api/appointments';

  constructor(private http: HttpClient) {}

  getAppointments(patientId?: string, dentistId?: string, date?: string, status?: AppointmentStatus): Observable<Appointment[]> {
    let params = new HttpParams();
    if (patientId) params = params.set('patientId', patientId);
    if (dentistId) params = params.set('dentistId', dentistId);
    if (date) params = params.set('date', date);
    if (status) params = params.set('status', status);

    return this.http.get<Appointment[]>(this.API_URL, { params });
  }

  getAppointmentById(id: string): Observable<Appointment> {
    return this.http.get<Appointment>(`${this.API_URL}/${id}`);
  }

  bookAppointment(request: BookAppointmentRequest): Observable<Appointment> {
    return this.http.post<Appointment>(this.API_URL, request);
  }

  rescheduleAppointment(id: string, request: RescheduleAppointmentRequest): Observable<Appointment> {
    return this.http.put<Appointment>(`${this.API_URL}/${id}/reschedule`, request);
  }

  cancelAppointment(id: string, request: CancelAppointmentRequest): Observable<void> {
    return this.http.request<void>('delete', `${this.API_URL}/${id}`, { body: request });
  }
}
