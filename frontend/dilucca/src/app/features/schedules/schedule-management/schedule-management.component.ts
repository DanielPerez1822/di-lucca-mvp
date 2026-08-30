import { Component, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { DayOfWeek, ScheduleDayConfig } from '../../../core/models/schedule.model';
import { User } from '../../../core/models/user.model';
import { ScheduleService } from '../../../core/services/schedule.service';
import { AuthService } from '../../../core/services/auth.service';

export interface ScheduleDayUIConfig {
  dayOfWeek: DayOfWeek;
  startTime: string;
  endTime: string;
  hasSecondShift?: boolean;
  startTime2?: string;
  endTime2?: string;
  slotDurationMinutes: number;
  active: boolean;
}

@Component({
  selector: 'app-schedule-management',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './schedule-management.component.html',
  styleUrl: './schedule-management.component.css'
})
export class ScheduleManagementComponent implements OnInit {

  daysConfig: ScheduleDayUIConfig[] = [
    { dayOfWeek: 'MONDAY', startTime: '08:00', endTime: '12:00', hasSecondShift: true, startTime2: '14:00', endTime2: '18:00', slotDurationMinutes: 30, active: true },
    { dayOfWeek: 'TUESDAY', startTime: '08:00', endTime: '12:00', hasSecondShift: true, startTime2: '14:00', endTime2: '18:00', slotDurationMinutes: 30, active: true },
    { dayOfWeek: 'WEDNESDAY', startTime: '08:00', endTime: '12:00', hasSecondShift: true, startTime2: '14:00', endTime2: '18:00', slotDurationMinutes: 30, active: true },
    { dayOfWeek: 'THURSDAY', startTime: '08:00', endTime: '12:00', hasSecondShift: true, startTime2: '14:00', endTime2: '18:00', slotDurationMinutes: 30, active: true },
    { dayOfWeek: 'FRIDAY', startTime: '08:00', endTime: '12:00', hasSecondShift: true, startTime2: '14:00', endTime2: '18:00', slotDurationMinutes: 30, active: true },
    { dayOfWeek: 'SATURDAY', startTime: '08:00', endTime: '13:00', hasSecondShift: false, slotDurationMinutes: 30, active: true },
    { dayOfWeek: 'SUNDAY', startTime: '08:00', endTime: '12:00', hasSecondShift: false, slotDurationMinutes: 30, active: false }
  ];

  dentists = signal<User[]>([]);
  selectedDentistId = signal<string>('');

  startDate: string = new Date().toISOString().split('T')[0];
  endDate: string = new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString().split('T')[0];

  loading = signal(false);
  genLoading = signal(false);
  successMessage = signal<string | null>(null);
  errorMessage = signal<string | null>(null);
  genSuccessMessage = signal<string | null>(null);

  constructor(
    private scheduleService: ScheduleService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.loadDentists();
  }

  loadDentists(): void {
    const currentUser = this.authService.currentUser();

    this.scheduleService.getDentists().subscribe({
      next: (list) => {
        this.dentists.set(list);
        if (list.length > 0) {
          // If logged in user is dentist and in the list, default to them, else first dentist
          const match = currentUser ? list.find(d => d.id === currentUser.id) : null;
          const targetId = match ? match.id : list[0].id;
          this.selectedDentistId.set(targetId);
          this.loadSchedule();
        } else if (currentUser) {
          this.selectedDentistId.set(currentUser.id);
          this.loadSchedule();
        }
      },
      error: () => {
        if (currentUser) {
          this.selectedDentistId.set(currentUser.id);
          this.loadSchedule();
        }
      }
    });
  }

  onDentistChange(): void {
    this.loadSchedule();
  }

  loadSchedule(): void {
    const dentistId = this.selectedDentistId();
    if (!dentistId) return;

    this.scheduleService.getScheduleByDentist(dentistId).subscribe({
      next: (schedule: any) => {
        if (schedule && Array.isArray(schedule) && schedule.length > 0) {
          this.parseBackendSchedules(schedule);
        } else {
          // Reset to default active days
          this.daysConfig.forEach(d => {
            d.active = d.dayOfWeek !== 'SUNDAY';
          });
        }
      },
      error: () => {}
    });
  }

  private parseBackendSchedules(schedules: any[]): void {
    // Reset defaults
    this.daysConfig.forEach(d => { d.active = false; d.hasSecondShift = false; });

    schedules.forEach(s => {
      const match = this.daysConfig.find(d => d.dayOfWeek === s.dayOfWeek);
      if (match) {
        if (!match.active) {
          match.active = true;
          match.startTime = s.startTime;
          match.endTime = s.endTime;
          match.slotDurationMinutes = s.slotDurationMinutes;
        } else {
          // Second shift
          match.hasSecondShift = true;
          match.startTime2 = s.startTime;
          match.endTime2 = s.endTime;
        }
      }
    });
  }

  getDayName(day: DayOfWeek): string {
    const names: Record<DayOfWeek, string> = {
      MONDAY: 'Lunes',
      TUESDAY: 'Martes',
      WEDNESDAY: 'Miércoles',
      THURSDAY: 'Jueves',
      FRIDAY: 'Viernes',
      SATURDAY: 'Sábado',
      SUNDAY: 'Domingo'
    };
    return names[day] || day;
  }

  saveSchedule(): void {
    const dentistId = this.selectedDentistId();
    if (!dentistId) {
      this.errorMessage.set('Por favor selecciona un odontólogo');
      return;
    }

    this.loading.set(true);
    this.successMessage.set(null);
    this.errorMessage.set(null);

    const payloadDays: ScheduleDayConfig[] = [];

    this.daysConfig.forEach(day => {
      if (day.active) {
        payloadDays.push({
          dayOfWeek: day.dayOfWeek,
          startTime: day.startTime,
          endTime: day.endTime,
          slotDurationMinutes: day.slotDurationMinutes,
          active: true
        });

        if (day.hasSecondShift && day.startTime2 && day.endTime2) {
          payloadDays.push({
            dayOfWeek: day.dayOfWeek,
            startTime: day.startTime2,
            endTime: day.endTime2,
            slotDurationMinutes: day.slotDurationMinutes,
            active: true
          });
        }
      }
    });

    if (payloadDays.length === 0) {
      this.loading.set(false);
      this.errorMessage.set('Debes seleccionar al menos un día de atención activo.');
      return;
    }

    this.scheduleService.setSchedule({
      dentistId: dentistId,
      days: payloadDays
    }).subscribe({
      next: () => {
        this.loading.set(false);
        this.successMessage.set('¡Configuración de horario semanal guardada con éxito!');
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMessage.set(err.message || 'Error al guardar horario');
      }
    });
  }

  generateSlots(): void {
    const dentistId = this.selectedDentistId();
    if (!dentistId) return;

    this.genLoading.set(true);
    this.genSuccessMessage.set(null);

    this.scheduleService.generateSlots({
      dentistId: dentistId,
      startDate: this.startDate,
      endDate: this.endDate
    }).subscribe({
      next: (slots) => {
        this.genLoading.set(false);
        this.genSuccessMessage.set(`¡${slots.length} slots de disponibilidad generados exitosamente!`);
      },
      error: () => {
        this.genLoading.set(false);
      }
    });
  }

  copyScheduleToAll(): void {
    const dentistId = this.selectedDentistId();
    if (!dentistId) return;

    this.loading.set(true);
    this.successMessage.set(null);
    this.errorMessage.set(null);

    this.scheduleService.copySchedule(dentistId, undefined, true).subscribe({
      next: () => {
        this.loading.set(false);
        this.successMessage.set('¡La plantilla de horario fue copiada exitosamente a TODOS los odontólogos!');
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMessage.set(err.message || 'Error al copiar horario');
      }
    });
  }
}
