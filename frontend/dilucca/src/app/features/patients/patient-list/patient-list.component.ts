import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Patient } from '../../../core/models/patient.model';
import { PatientService } from '../../../core/services/patient.service';
import { PatientFormComponent } from '../patient-form/patient-form.component';

@Component({
  selector: 'app-patient-list',
  standalone: true,
  imports: [CommonModule, FormsModule, PatientFormComponent],
  templateUrl: './patient-list.component.html',
  styleUrl: './patient-list.component.css'
})
export class PatientListComponent implements OnInit {

  patients = signal<Patient[]>([]);
  loading = signal(false);
  showFormModal = signal(false);
  selectedPatient = signal<Patient | null>(null);

  searchQuery = '';
  activeOnly = false;
  private searchTimeout: any;

  constructor(private patientService: PatientService) {}

  ngOnInit(): void {
    this.loadPatients();
  }

  loadPatients(): void {
    this.loading.set(true);
    this.patientService.getPatients(this.searchQuery, this.activeOnly).subscribe({
      next: (data) => {
        this.patients.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  onSearch(): void {
    clearTimeout(this.searchTimeout);
    this.searchTimeout = setTimeout(() => {
      this.loadPatients();
    }, 300);
  }

  openCreateModal(): void {
    this.selectedPatient.set(null);
    this.showFormModal.set(true);
  }

  openEditModal(patient: Patient): void {
    this.selectedPatient.set(patient);
    this.showFormModal.set(true);
  }

  closeModal(): void {
    this.showFormModal.set(false);
    this.selectedPatient.set(null);
  }

  onPatientSaved(): void {
    this.closeModal();
    this.loadPatients();
  }

  onDeactivate(patient: Patient): void {
    if (confirm(`¿Estás seguro de desactivar al paciente ${patient.firstName} ${patient.lastName}?`)) {
      this.patientService.deletePatient(patient.id).subscribe({
        next: () => {
          this.loadPatients();
        }
      });
    }
  }
}
