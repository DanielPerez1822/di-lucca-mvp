import { Component, OnInit, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MedicalRecord } from '../../../core/models/medical-record.model';
import { Patient } from '../../../core/models/patient.model';
import { MedicalRecordService } from '../../../core/services/medical-record.service';
import { PatientService } from '../../../core/services/patient.service';
import { InvoiceService } from '../../../core/services/invoice.service';
import { MedicalRecordFormComponent } from '../medical-record-form/medical-record-form.component';
import { AuthService } from '../../../core/services/auth.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-medical-record-list',
  standalone: true,
  imports: [CommonModule, FormsModule, MedicalRecordFormComponent],
  templateUrl: './medical-record-list.component.html',
  styleUrl: './medical-record-list.component.css'
})
export class MedicalRecordListComponent implements OnInit {

  records = signal<MedicalRecord[]>([]);
  patients = signal<Patient[]>([]);
  loading = signal(false);
  showFormModal = signal(false);
  selectedPatientId = '';
  patientSearchQuery = '';

  /** Solo el Dentista (DENTIST) y el Administrador (ADMIN) pueden registrar atención médica */
  canCreateRecord = computed(() => this.authService.hasRole(['DENTIST', 'ADMIN']));

  constructor(
    private medicalRecordService: MedicalRecordService,
    private patientService: PatientService,
    private invoiceService: InvoiceService,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadPatients();
    this.route.queryParams.subscribe(params => {
      if (params['patientId']) {
        this.selectedPatientId = params['patientId'];
        this.loadHistory();
      }
      if (params['search']) {
        this.patientSearchQuery = params['search'];
      }
    });
  }

  loadPatients(): void {
    this.patientService.getPatients('', true).subscribe({
      next: (data) => {
        this.patients.set(data);
        if (this.selectedPatientId) {
          this.loadHistory();
        }
      },
      error: () => {}
    });
  }

  filteredPatients(): Patient[] {
    const q = this.patientSearchQuery.trim().toLowerCase();
    if (!q) return this.patients();
    return this.patients().filter(p =>
      (p.firstName && p.firstName.toLowerCase().includes(q)) ||
      (p.lastName && p.lastName.toLowerCase().includes(q)) ||
      (p.documentNumber && p.documentNumber.toLowerCase().includes(q))
    );
  }

  loadHistory(): void {
    if (!this.selectedPatientId) {
      this.records.set([]);
      return;
    }

    this.loading.set(true);
    this.medicalRecordService.getPatientHistory(this.selectedPatientId).subscribe({
      next: (data) => {
        this.records.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  openCreateModal(): void {
    this.showFormModal.set(true);
  }

  closeModal(): void {
    this.showFormModal.set(false);
  }

  onRecordSaved(): void {
    this.closeModal();
    this.loadHistory();
  }

  generateInvoice(record: MedicalRecord): void {
    this.invoiceService.createInvoiceFromMedicalRecord(record.id).subscribe({
      next: (invoice) => {
        alert(`¡Factura ${invoice.invoiceNumber} generada exitosamente por un valor de $${invoice.totalAmount}! Puedes verla y registrar su pago en la sección Facturación y Pagos.`);
      },
      error: (err) => {
        alert(err.message || 'La factura ya fue generada previamente o no pudo procesarse.');
      }
    });
  }
}
