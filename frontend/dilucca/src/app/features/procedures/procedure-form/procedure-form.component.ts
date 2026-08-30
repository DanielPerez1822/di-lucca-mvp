import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Procedure } from '../../../core/models/procedure.model';
import { ProcedureService } from '../../../core/services/procedure.service';

@Component({
  selector: 'app-procedure-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './procedure-form.component.html',
  styleUrl: './procedure-form.component.css'
})
export class ProcedureFormComponent implements OnInit {

  @Input() procedure: Procedure | null = null;
  @Output() saved = new EventEmitter<void>();
  @Output() closed = new EventEmitter<void>();

  procedureForm!: FormGroup;
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private procedureService: ProcedureService
  ) {}

  ngOnInit(): void {
    this.procedureForm = this.fb.group({
      name: [this.procedure?.name || '', [Validators.required]],
      description: [this.procedure?.description || '', [Validators.required]],
      price: [this.procedure?.price || '', [Validators.required, Validators.min(0)]],
      durationInMinutes: [this.procedure?.duration || this.procedure?.durationInMinutes || 30, [Validators.required, Validators.min(5)]]
    });
  }

  onClose(): void {
    this.closed.emit();
  }

  onSubmit(): void {
    if (this.procedureForm.invalid) {
      this.procedureForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    const formValue = this.procedureForm.value;
    const payload = {
      name: formValue.name,
      description: formValue.description,
      price: formValue.price,
      duration: formValue.durationInMinutes
    };

    if (this.procedure) {
      this.procedureService.updateProcedure(this.procedure.id, payload).subscribe({
        next: () => {
          this.loading.set(false);
          this.saved.emit();
        },
        error: (err) => {
          this.loading.set(false);
          this.errorMessage.set(err.message || 'Error al actualizar el procedimiento');
        }
      });
    } else {
      this.procedureService.createProcedure(payload).subscribe({
        next: () => {
          this.loading.set(false);
          this.saved.emit();
        },
        error: (err) => {
          this.loading.set(false);
          this.errorMessage.set(err.message || 'Error al guardar procedimiento. Verifica los datos.');
        }
      });
    }
  }
}
