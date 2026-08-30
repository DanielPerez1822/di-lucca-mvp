import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Patient } from '../../../core/models/patient.model';
import { PatientService } from '../../../core/services/patient.service';

@Component({
  selector: 'app-patient-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './patient-form.component.html',
  styleUrl: './patient-form.component.css'
})
export class PatientFormComponent implements OnInit {

  @Input() patient: Patient | null = null;
  @Output() saved = new EventEmitter<void>();
  @Output() closed = new EventEmitter<void>();

  patientForm!: FormGroup;
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private patientService: PatientService
  ) {}

  ngOnInit(): void {
    this.patientForm = this.fb.group({
      firstName: [this.patient?.firstName || '', [Validators.required]],
      lastName: [this.patient?.lastName || '', [Validators.required]],
      documentType: [this.patient?.documentType || 'CC', [Validators.required]],
      documentNumber: [this.patient?.documentNumber || '', [Validators.required]],
      phone: [this.patient?.phone || '', [Validators.required]],
      email: [this.patient?.email || '', [Validators.email]],
      dateOfBirth: [this.patient?.dateOfBirth || ''],
      address: [this.patient?.address || '']
    });
  }

  onClose(): void {
    this.closed.emit();
  }

  onSubmit(): void {
    if (this.patientForm.invalid) {
      this.patientForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    if (this.patient) {
      this.patientService.updatePatient(this.patient.id, this.patientForm.value).subscribe({
        next: () => {
          this.loading.set(false);
          this.saved.emit();
        },
        error: (err) => {
          this.loading.set(false);
          this.errorMessage.set(err.message || 'Error al actualizar paciente');
        }
      });
    } else {
      this.patientService.createPatient(this.patientForm.value).subscribe({
        next: () => {
          this.loading.set(false);
          this.saved.emit();
        },
        error: (err) => {
          this.loading.set(false);
          this.errorMessage.set(err.message || 'Error al guardar paciente. Verifica si ya existe.');
        }
      });
    }
  }
}
