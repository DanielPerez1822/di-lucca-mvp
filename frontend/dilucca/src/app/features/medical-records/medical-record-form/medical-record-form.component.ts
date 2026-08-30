import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Patient } from '../../../core/models/patient.model';
import { Procedure } from '../../../core/models/procedure.model';
import { MedicalRecordService } from '../../../core/services/medical-record.service';
import { PatientService } from '../../../core/services/patient.service';
import { ProcedureService } from '../../../core/services/procedure.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-medical-record-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './medical-record-form.component.html',
  styleUrl: './medical-record-form.component.css'
})
export class MedicalRecordFormComponent implements OnInit {

  @Input() patientId: string | null = null;
  @Input() appointmentId: string | null = null;
  @Output() saved = new EventEmitter<void>();
  @Output() closed = new EventEmitter<void>();

  recordForm!: FormGroup;
  patients = signal<Patient[]>([]);
  procedures = signal<Procedure[]>([]);
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private medicalRecordService: MedicalRecordService,
    private patientService: PatientService,
    private procedureService: ProcedureService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.recordForm = this.fb.group({
      patientId: [this.patientId || '', [Validators.required]],
      diagnosis: ['', [Validators.required]],
      notes: [''],
      items: this.fb.array([])
    });

    this.loadPatients();
    this.loadProcedures();
    this.addItem(); // Add 1 initial item row
  }

  get itemsFormArray(): FormArray {
    return this.recordForm.get('items') as FormArray;
  }

  addItem(): void {
    const itemGroup = this.fb.group({
      procedureId: ['', [Validators.required]],
      appliedPrice: [0, [Validators.required, Validators.min(0)]],
      toothNumber: [null],
      notes: ['']
    });
    this.itemsFormArray.push(itemGroup);
  }

  removeItem(index: number): void {
    if (this.itemsFormArray.length > 1) {
      this.itemsFormArray.removeAt(index);
    }
  }

  onProcedureSelect(index: number): void {
    const group = this.itemsFormArray.at(index);
    const procId = group.get('procedureId')?.value;
    const proc = this.procedures().find(p => p.id === procId);
    if (proc) {
      group.patchValue({ appliedPrice: proc.price });
    }
  }

  loadPatients(): void {
    this.patientService.getPatients('', true).subscribe({
      next: (data) => this.patients.set(data),
      error: () => {}
    });
  }

  loadProcedures(): void {
    this.procedureService.getProcedures('', true).subscribe({
      next: (data) => this.procedures.set(data),
      error: () => {}
    });
  }

  onClose(): void {
    this.closed.emit();
  }

  onSubmit(): void {
    if (this.recordForm.invalid) {
      this.recordForm.markAllAsTouched();
      return;
    }

    const user = this.authService.currentUser();
    if (!user) return;

    this.loading.set(true);
    this.errorMessage.set(null);

    const formVal = this.recordForm.value;

    this.medicalRecordService.createMedicalRecord({
      patientId: formVal.patientId,
      dentistId: user.id,
      appointmentId: this.appointmentId || undefined,
      diagnosis: formVal.diagnosis,
      notes: formVal.notes,
      items: formVal.items
    }).subscribe({
      next: () => {
        this.loading.set(false);
        this.saved.emit();
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMessage.set(err.message || 'Error al registrar atención en historia clínica');
      }
    });
  }
}
