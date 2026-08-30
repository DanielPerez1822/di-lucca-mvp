import { Component, OnInit, computed, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { MedicalRecord } from '../../../core/models/medical-record.model';
import { Patient } from '../../../core/models/patient.model';
import { User } from '../../../core/models/user.model';
import { MedicalRecordService } from '../../../core/services/medical-record.service';
import { PatientService } from '../../../core/services/patient.service';
import { InvoiceService } from '../../../core/services/invoice.service';
import { UserManagementService } from '../../../core/services/user-management.service';
import { MedicalRecordFormComponent } from '../medical-record-form/medical-record-form.component';
import { InvoiceDetailModalComponent } from '../invoice-detail-modal/invoice-detail-modal.component';
import { AuthService } from '../../../core/services/auth.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-medical-record-list',
  standalone: true,
  imports: [CommonModule, FormsModule, MedicalRecordFormComponent, InvoiceDetailModalComponent],
  templateUrl: './medical-record-list.component.html',
  styleUrl: './medical-record-list.component.css'
})
export class MedicalRecordListComponent implements OnInit {

  records = signal<MedicalRecord[]>([]);
  patients = signal<Patient[]>([]);
  users = signal<User[]>([]);
  loading = signal(false);
  showFormModal = signal(false);
  selectedPatientId = '';
  patientSearchQuery = '';

  // Invoice modal state
  invoiceModalRecord = signal<MedicalRecord | null>(null);
  showInvoiceModal = signal(false);

  /** Solo el Dentista (DENTIST) y el Administrador (ADMIN) pueden registrar atención médica */
  canCreateRecord = computed(() => this.authService.hasRole(['DENTIST', 'ADMIN']));

  constructor(
    private medicalRecordService: MedicalRecordService,
    private patientService: PatientService,
    private invoiceService: InvoiceService,
    private userManagementService: UserManagementService,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.loadPatients();
    this.loadUsers();
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

  loadUsers(): void {
    // El endpoint /api/admin/users solo está disponible para el ADMIN.
    // Para otros roles solo necesitamos el usuario actual para resolver el nombre del dentista.
    if (this.authService.hasRole(['ADMIN'])) {
      this.userManagementService.getUsers().subscribe({
        next: (users) => this.users.set(users),
        error: () => {}
      });
    } else {
      // Para dentista / asistente, pre-cargamos solo el usuario actual en la lista
      const current = this.authService.currentUser();
      if (current) this.users.set([current as any]);
    }
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

  /** Returns the selected patient object or null */
  get selectedPatient(): Patient | null {
    return this.patients().find(p => p.id === this.selectedPatientId) ?? null;
  }

  /** Returns the dentist's full name for a given dentistId */
  getDentistName(dentistId: string): string {
    const user = this.users().find(u => u.id === dentistId);
    if (user) return `${user.firstName} ${user.lastName}`;
    return 'Odontólogo DI-LUCCA';
  }

  /** Opens the invoice detail modal for a record */
  openInvoiceModal(record: MedicalRecord): void {
    this.invoiceModalRecord.set(record);
    this.showInvoiceModal.set(true);
  }

  closeInvoiceModal(): void {
    this.showInvoiceModal.set(false);
    this.invoiceModalRecord.set(null);
  }
}
