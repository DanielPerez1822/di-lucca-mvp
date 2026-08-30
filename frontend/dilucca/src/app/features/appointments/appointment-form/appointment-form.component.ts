import { Component, EventEmitter, Input, OnInit, Output, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Appointment } from '../../../core/models/appointment.model';
import { Patient } from '../../../core/models/patient.model';
import { AvailabilitySlot } from '../../../core/models/schedule.model';
import { Procedure } from '../../../core/models/procedure.model';
import { User } from '../../../core/models/user.model';
import { AppointmentService } from '../../../core/services/appointment.service';
import { PatientService } from '../../../core/services/patient.service';
import { ScheduleService } from '../../../core/services/schedule.service';
import { ProcedureService } from '../../../core/services/procedure.service';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-appointment-form',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './appointment-form.component.html',
  styleUrl: './appointment-form.component.css'
})
export class AppointmentFormComponent implements OnInit {

  @Input() appointment: Appointment | null = null;
  @Input() isReschedule: boolean = false;
  @Input() preselectedSlot: AvailabilitySlot | null = null;
  @Input() preselectedDate: string | null = null;
  @Input() preselectedDentistId: string | null = null;

  @Output() saved = new EventEmitter<void>();
  @Output() closed = new EventEmitter<void>();

  bookingForm!: FormGroup;
  patients = signal<Patient[]>([]);
  dentists = signal<User[]>([]);
  procedures = signal<Procedure[]>([]);
  availableSlots = signal<AvailabilitySlot[]>([]);
  loading = signal(false);
  errorMessage = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private appointmentService: AppointmentService,
    private patientService: PatientService,
    private scheduleService: ScheduleService,
    private procedureService: ProcedureService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const today = new Date().toISOString().split('T')[0];
    const currentUser = this.authService.currentUser();

    const initialDentistId = this.preselectedSlot?.dentistId
      || this.preselectedDentistId
      || this.appointment?.dentistId
      || currentUser?.id
      || '';

    const initialDate = this.preselectedSlot?.date
      || this.preselectedDate
      || this.appointment?.date
      || today;

    const initialSlotId = this.preselectedSlot?.id || '';

    this.bookingForm = this.fb.group({
      patientId: [this.appointment?.patientId || '', this.isReschedule ? [] : [Validators.required]],
      dentistId: [initialDentistId, [Validators.required]],
      procedureId: [''],
      selectedDate: [initialDate, [Validators.required]],
      slotId: [initialSlotId, [Validators.required]],
      reason: [this.appointment?.reason || '']
    });

    if (!this.isReschedule) {
      this.loadPatients();
      this.loadDentists();
      this.loadProcedures();
    } else {
      this.loadDentists();
    }
    this.onDateOrDentistChange();
  }

  loadPatients(): void {
    this.patientService.getPatients('', true).subscribe({
      next: (data) => this.patients.set(data),
      error: () => {}
    });
  }

  loadDentists(): void {
    this.scheduleService.getDentists().subscribe({
      next: (data) => {
        this.dentists.set(data);
        const currentDentistId = this.bookingForm.get('dentistId')?.value;
        if (!currentDentistId && data.length > 0) {
          this.bookingForm.patchValue({ dentistId: data[0].id });
          this.onDateOrDentistChange();
        }
      },
      error: () => {}
    });
  }

  loadProcedures(): void {
    this.procedureService.getProcedures('', true).subscribe({
      next: (data) => this.procedures.set(data),
      error: () => {}
    });
  }

  getSelectedProcedure(): Procedure | null {
    const procId = this.bookingForm.get('procedureId')?.value;
    if (!procId) return null;
    return this.procedures().find(p => p.id === procId) || null;
  }

  onDateOrDentistChange(): void {
    const dentistId = this.bookingForm.get('dentistId')?.value;
    const date = this.bookingForm.get('selectedDate')?.value;
    if (!dentistId || !date) return;

    this.scheduleService.getAvailableSlots(dentistId, date).subscribe({
      next: (slots) => {
        this.availableSlots.set(slots);
        const currentSlotId = this.bookingForm.get('slotId')?.value;
        const matching = slots.find(s => s.id === currentSlotId || (this.preselectedSlot && s.id === this.preselectedSlot.id));

        if (matching) {
          this.bookingForm.patchValue({ slotId: matching.id });
        } else if (slots.length > 0 && !currentSlotId) {
          this.bookingForm.patchValue({ slotId: slots[0].id });
        }
      },
      error: () => this.availableSlots.set([])
    });
  }

  // Calculate total contiguous available duration starting at slot
  getContiguousDurationMinutes(slot: AvailabilitySlot): number {
    const slots = this.availableSlots();
    const parseMin = (t: string) => {
      const [h, m] = t.split(':').map(Number);
      return h * 60 + m;
    };

    let totalMinutes = parseMin(slot.endTime) - parseMin(slot.startTime);
    let currentEndTime = slot.endTime;

    while (true) {
      const nextSlot = slots.find(s => s.startTime === currentEndTime && s.status === 'AVAILABLE');
      if (!nextSlot) break;
      const nextDuration = parseMin(nextSlot.endTime) - parseMin(nextSlot.startTime);
      totalMinutes += nextDuration;
      currentEndTime = nextSlot.endTime;
    }

    return totalMinutes;
  }

  isSlotSufficientForProcedure(slot: AvailabilitySlot): boolean {
    const proc = this.getSelectedProcedure();
    if (!proc) return true; // No procedure selected, allow any slot
    const requiredMin = proc.duration || proc.durationInMinutes || 30;
    const availableMin = this.getContiguousDurationMinutes(slot);
    return availableMin >= requiredMin;
  }

  onClose(): void {
    this.closed.emit();
  }

  onSubmit(): void {
    if (this.bookingForm.invalid) {
      this.bookingForm.markAllAsTouched();
      return;
    }

    const user = this.authService.currentUser();
    if (!user) return;

    this.loading.set(true);
    this.errorMessage.set(null);

    if (this.isReschedule && this.appointment) {
      const newSlotId = this.bookingForm.value.slotId;
      this.appointmentService.rescheduleAppointment(this.appointment.id, { newSlotId }).subscribe({
        next: () => {
          this.loading.set(false);
          this.saved.emit();
        },
        error: (err) => {
          this.loading.set(false);
          this.errorMessage.set(err.message || 'Error al reagendar cita');
        }
      });
    } else {
      const formVal = this.bookingForm.value;
      const selectedProc = this.getSelectedProcedure();
      const reasonText = selectedProc
        ? `[${selectedProc.name}] ${formVal.reason || ''}`.trim()
        : formVal.reason;

      this.appointmentService.bookAppointment({
        patientId: formVal.patientId,
        dentistId: formVal.dentistId,
        slotId: formVal.slotId,
        reason: reasonText
      }).subscribe({
        next: () => {
          this.loading.set(false);
          this.saved.emit();
        },
        error: (err) => {
          this.loading.set(false);
          this.errorMessage.set(err.message || 'Error al agendar cita. Verifica la disponibilidad.');
        }
      });
    }
  }
}
