import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Appointment, AppointmentStatus } from '../../../core/models/appointment.model';
import { AppointmentService } from '../../../core/services/appointment.service';
import { AuthService } from '../../../core/services/auth.service';
import { AppointmentFormComponent } from '../appointment-form/appointment-form.component';

@Component({
  selector: 'app-appointment-list',
  standalone: true,
  imports: [CommonModule, FormsModule, AppointmentFormComponent],
  templateUrl: './appointment-list.component.html',
  styleUrl: './appointment-list.component.css'
})
export class AppointmentListComponent implements OnInit {

  appointments = signal<Appointment[]>([]);
  loading = signal(false);
  showFormModal = signal(false);
  isReschedule = signal(false);
  selectedAppointment = signal<Appointment | null>(null);

  filterDate = '';
  filterStatus: AppointmentStatus | '' = '';

  constructor(
    private appointmentService: AppointmentService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadAppointments();
  }

  loadAppointments(): void {
    const user = this.authService.currentUser();
    if (!user) return;

    this.loading.set(true);
    const dentistId = user.roles.includes('DENTIST') ? user.id : undefined;

    this.appointmentService.getAppointments(
      undefined,
      dentistId,
      this.filterDate || undefined,
      (this.filterStatus as AppointmentStatus) || undefined
    ).subscribe({
      next: (data) => {
        this.appointments.set(data);
        this.loading.set(false);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  clearFilters(): void {
    this.filterDate = '';
    this.filterStatus = '';
    this.loadAppointments();
  }

  openBookModal(): void {
    this.selectedAppointment.set(null);
    this.isReschedule.set(false);
    this.showFormModal.set(true);
  }

  openRescheduleModal(appt: Appointment): void {
    this.selectedAppointment.set(appt);
    this.isReschedule.set(true);
    this.showFormModal.set(true);
  }

  closeModal(): void {
    this.showFormModal.set(false);
    this.selectedAppointment.set(null);
    this.isReschedule.set(false);
  }

  onAppointmentSaved(): void {
    this.closeModal();
    this.loadAppointments();
  }

  onCancel(appt: Appointment): void {
    const reason = prompt('Por favor ingresa el motivo de la cancelación:');
    if (reason !== null) {
      this.appointmentService.cancelAppointment(appt.id, { cancellationReason: reason }).subscribe({
        next: () => {
          this.loadAppointments();
        }
      });
    }
  }

  getStatusBadgeClass(status: AppointmentStatus): string {
    const classes: Record<AppointmentStatus, string> = {
      SCHEDULED: 'badge badge-info',
      CONFIRMED: 'badge badge-warning',
      IN_PROGRESS: 'badge badge-info',
      COMPLETED: 'badge badge-success',
      CANCELLED: 'badge badge-danger'
    };
    return classes[status] || 'badge';
  }

  getStatusText(status: AppointmentStatus): string {
    const text: Record<AppointmentStatus, string> = {
      SCHEDULED: 'PROGRAMADA',
      CONFIRMED: 'CONFIRMADA',
      IN_PROGRESS: 'EN ATENCIÓN',
      COMPLETED: 'COMPLETADA',
      CANCELLED: 'CANCELADA'
    };
    return text[status] || status;
  }
}
