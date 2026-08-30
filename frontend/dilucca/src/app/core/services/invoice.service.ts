import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Invoice, InvoiceStatus, RegisterPaymentRequest } from '../models/invoice.model';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {

  private readonly API_URL = 'http://localhost:9000/api/invoices';

  constructor(private http: HttpClient) {}

  getInvoices(patientId?: string, status?: InvoiceStatus): Observable<Invoice[]> {
    let params = new HttpParams();
    if (patientId) params = params.set('patientId', patientId);
    if (status) params = params.set('status', status);

    return this.http.get<Invoice[]>(this.API_URL, { params });
  }

  getInvoiceById(id: string): Observable<Invoice> {
    return this.http.get<Invoice>(`${this.API_URL}/${id}`);
  }

  createInvoiceFromMedicalRecord(medicalRecordId: string): Observable<Invoice> {
    return this.http.post<Invoice>(`${this.API_URL}/from-medical-record/${medicalRecordId}`, {});
  }

  registerPayment(invoiceId: string, request: RegisterPaymentRequest): Observable<Invoice> {
    return this.http.post<Invoice>(`${this.API_URL}/${invoiceId}/payments`, request);
  }

  sendInvoiceEmail(invoiceId: string): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/${invoiceId}/send-email`, {});
  }

  updateInvoice(id: string, status: InvoiceStatus, paidAmount: number): Observable<Invoice> {
    return this.http.put<Invoice>(`${this.API_URL}/${id}`, { status, paidAmount });
  }
}
