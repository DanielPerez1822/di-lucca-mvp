import { Component, OnInit, signal, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AppointmentService } from '../../../core/services/appointment.service';
import { ScheduleService } from '../../../core/services/schedule.service';
import { AuthService } from '../../../core/services/auth.service';
import { Appointment } from '../../../core/models/appointment.model';
import { AvailabilitySlot } from '../../../core/models/schedule.model';
import { User } from '../../../core/models/user.model';
import { AppointmentFormComponent } from '../../appointments/appointment-form/appointment-form.component';

import { Router } from '@angular/router';
import { PatientService } from '../../../core/services/patient.service';
import { Patient } from '../../../core/models/patient.model';

export interface CalendarMonthDay {
  date: string; // YYYY-MM-DD
  dayNumber: number;
  isCurrentMonth: boolean;
  isToday: boolean;
  isSelected: boolean;
  appointments: Appointment[];
}

export interface WaitlistPatient {
  id: string;
  patientName: string;
  treatmentName: string;
  treatmentColor: string; // 'badge-blue' | 'badge-cyan' | 'badge-green' | 'badge-purple'
  preference: string;
}

export interface CalendarTimeRow {
  time: string; // e.g. '08:00'
  label: string; // e.g. '08:00 AM'
}

@Component({
  selector: 'app-calendar-view',
  standalone: true,
  imports: [CommonModule, FormsModule, AppointmentFormComponent],
  templateUrl: './calendar-view.component.html',
  styleUrl: './calendar-view.component.css'
})
export class CalendarViewComponent implements OnInit {

  currentDate = signal<Date>(new Date());
  selectedDateStr = signal<string>(new Date().toISOString().split('T')[0]);
  selectedDentistId = signal<string>('ALL');

  dentists = signal<User[]>([]);
  patients = signal<Patient[]>([]);
  monthlyAppointments = signal<Appointment[]>([]);
  dayAvailableSlots = signal<AvailabilitySlot[]>([]);
  loading = signal<boolean>(false);

  // Modal / Drawer states
  showDayDetailsModal = signal<boolean>(false);
  showAppointmentForm = signal<boolean>(false);
  selectedSlotForBooking = signal<AvailabilitySlot | null>(null);

  hours: CalendarTimeRow[] = [
    { time: '07:00', label: '07:00 AM' },
    { time: '07:30', label: '07:30 AM' },
    { time: '08:00', label: '08:00 AM' },
    { time: '08:30', label: '08:30 AM' },
    { time: '09:00', label: '09:00 AM' },
    { time: '09:30', label: '09:30 AM' },
    { time: '10:00', label: '10:00 AM' },
    { time: '10:30', label: '10:30 AM' },
    { time: '11:00', label: '11:00 AM' },
    { time: '11:30', label: '11:30 AM' },
    { time: '12:00', label: '12:00 PM' },
    { time: '12:30', label: '12:30 PM' },
    { time: '13:00', label: '01:00 PM' },
    { time: '13:30', label: '01:30 PM' },
    { time: '14:00', label: '02:00 PM' },
    { time: '14:30', label: '02:30 PM' },
    { time: '15:00', label: '03:00 PM' },
    { time: '15:30', label: '03:30 PM' },
    { time: '16:00', label: '04:00 PM' },
    { time: '16:30', label: '04:30 PM' },
    { time: '17:00', label: '05:00 PM' },
    { time: '17:30', label: '05:30 PM' },
    { time: '18:00', label: '06:00 PM' }
  ];

  currentUser = computed(() => this.authService.currentUser());
  isDentist = computed(() => this.authService.hasRole(['DENTIST']) && !this.authService.hasRole(['ADMIN']));
  canBook = computed(() => this.authService.hasRole(['SECRETARY_ASSISTANT']) || this.authService.hasRole(['ADMIN']));

  selectedDayAppointments = computed(() => {
    const dateStr = this.selectedDateStr();
    return this.monthlyAppointments().filter(a => {
      const aDate = a.appointmentDate || a.date;
      return aDate === dateStr;
    });
  });

  monthYearLabel = computed(() => {
    const months = ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'];
    const d = this.currentDate();
    return `${months[d.getMonth()]} ${d.getFullYear()}`;
  });

  monthGrid = computed<CalendarMonthDay[]>(() => {
    const d = this.currentDate();
    const year = d.getFullYear();
    const month = d.getMonth();

    const firstDay = new Date(year, month, 1);
    const lastDay = new Date(year, month + 1, 0);

    const startingDayOfWeek = firstDay.getDay(); // 0 = Sun
    const totalDays = lastDay.getDate();

    const todayStr = new Date().toISOString().split('T')[0];
    const selectedStr = this.selectedDateStr();
    const appts = this.monthlyAppointments();

    const grid: CalendarMonthDay[] = [];

    const isMatchDate = (a: Appointment, targetStr: string) => {
      const aDate = a.appointmentDate || a.date;
      return aDate === targetStr;
    };

    // Previous month padding
    const prevMonthLastDay = new Date(year, month, 0).getDate();
    for (let i = startingDayOfWeek - 1; i >= 0; i--) {
      const prevDate = new Date(year, month - 1, prevMonthLastDay - i);
      const dateStr = prevDate.toISOString().split('T')[0];
      grid.push({
        date: dateStr,
        dayNumber: prevDate.getDate(),
        isCurrentMonth: false,
        isToday: dateStr === todayStr,
        isSelected: dateStr === selectedStr,
        appointments: appts.filter(a => isMatchDate(a, dateStr))
      });
    }

    // Current month days
    for (let day = 1; day <= totalDays; day++) {
      const currDate = new Date(year, month, day);
      const dateStr = currDate.toISOString().split('T')[0];
      grid.push({
        date: dateStr,
        dayNumber: day,
        isCurrentMonth: true,
        isToday: dateStr === todayStr,
        isSelected: dateStr === selectedStr,
        appointments: appts.filter(a => isMatchDate(a, dateStr))
      });
    }

    // Next month padding to fill grid
    const remaining = (7 - (grid.length % 7)) % 7;
    for (let i = 1; i <= remaining; i++) {
      const nextDate = new Date(year, month + 1, i);
      const dateStr = nextDate.toISOString().split('T')[0];
      grid.push({
        date: dateStr,
        dayNumber: i,
        isCurrentMonth: false,
        isToday: dateStr === todayStr,
        isSelected: dateStr === selectedStr,
        appointments: appts.filter(a => isMatchDate(a, dateStr))
      });
    }

    return grid;
  });

  constructor(
    private appointmentService: AppointmentService,
    private scheduleService: ScheduleService,
    private patientService: PatientService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.loadPatients();
    this.loadDentists();
  }

  loadPatients(): void {
    this.patientService.getPatients('', true).subscribe({
      next: (list) => this.patients.set(list),
      error: () => {}
    });
  }

  getPatientName(patientId: string): string {
    const p = this.patients().find(pat => pat.id === patientId);
    return p ? `${p.firstName} ${p.lastName}` : 'Paciente Agendado';
  }

  goToMedicalRecord(patientId: string, event?: Event): void {
    if (event) event.stopPropagation();
    const p = this.patients().find(pat => pat.id === patientId);
    const searchName = p ? `${p.firstName} ${p.lastName}` : '';
    this.router.navigate(['/medical-records'], {
      queryParams: { patientId, search: searchName }
    });
  }

  loadDentists(): void {
    this.scheduleService.getDentists().subscribe({
      next: (list) => {
        this.dentists.set(list);
        if (this.isDentist()) {
          const u = this.currentUser();
          if (u) this.selectedDentistId.set(u.id);
        }
        this.loadMonthAppointments();
      },
      error: () => this.loadMonthAppointments()
    });
  }

  loadMonthAppointments(): void {
    this.loading.set(true);
    const dId = this.selectedDentistId() === 'ALL' ? undefined : this.selectedDentistId();

    this.appointmentService.getAppointments(undefined, dId).subscribe({
      next: (appts) => {
        this.monthlyAppointments.set(appts);
        this.loading.set(false);
      },
      error: () => this.loading.set(false)
    });
  }

  navigateMonth(direction: number): void {
    const d = new Date(this.currentDate());
    d.setMonth(d.getMonth() + direction);
    this.currentDate.set(d);
    this.loadMonthAppointments();
  }

  onSelectDay(day: CalendarMonthDay): void {
    this.selectedDateStr.set(day.date);
    this.loadAvailableSlotsForDay(day.date);
    this.showDayDetailsModal.set(true);
  }

  loadAvailableSlotsForDay(dateStr: string): void {
    const dId = this.selectedDentistId() === 'ALL'
      ? (this.dentists().length > 0 ? this.dentists()[0].id : '')
      : this.selectedDentistId();

    if (!dId) {
      this.dayAvailableSlots.set([]);
      return;
    }

    this.scheduleService.getAvailableSlots(dId, dateStr).subscribe({
      next: (slots) => this.dayAvailableSlots.set(slots),
      error: () => this.dayAvailableSlots.set([])
    });
  }

  openBookingForm(slot?: AvailabilitySlot): void {
    if (!this.canBook()) return; // Dentists cannot book
    this.selectedSlotForBooking.set(slot || null);
    this.showAppointmentForm.set(true);
  }

  assignWaitlistPatient(patient: WaitlistPatient): void {
    if (!this.canBook()) return;
    this.showDayDetailsModal.set(true);
  }

  onAppointmentCreated(): void {
    this.showAppointmentForm.set(false);
    this.selectedSlotForBooking.set(null);
    this.loadMonthAppointments();
    if (this.selectedDateStr()) {
      this.loadAvailableSlotsForDay(this.selectedDateStr());
    }
  }

  getTreatmentBadgeClass(reason?: string): string {
    if (!reason) return 'badge-blue';
    const lower = reason.toLowerCase();
    if (lower.includes('cirug')) return 'badge-cyan';
    if (lower.includes('ortod')) return 'badge-purple';
    if (lower.includes('limp') || lower.includes('profi')) return 'badge-blue';
    return 'badge-green';
  }

  getAppointmentForSlot(dentistId: string, time: string): Appointment | undefined {
    const dateStr = this.selectedDateStr();
    const timeClean = time.substring(0, 5);

    return this.monthlyAppointments().find(a => {
      const aDate = a.appointmentDate || a.date;
      if (aDate !== dateStr) return false;

      if (dentistId !== 'ALL' && a.dentistId) {
        if (a.dentistId.toLowerCase() !== dentistId.toLowerCase()) return false;
      }

      const aTimeClean = (a.startTime || '').substring(0, 5);
      return aTimeClean === timeClean || a.startTime?.startsWith(timeClean);
    });
  }

  getAvailableSlot(dentistId: string, time: string): AvailabilitySlot | undefined {
    const timeClean = time.substring(0, 5);
    return this.dayAvailableSlots().find(s => {
      if (dentistId !== 'ALL' && s.dentistId) {
        if (s.dentistId.toLowerCase() !== dentistId.toLowerCase()) return false;
      }
      const sTimeClean = (s.startTime || '').substring(0, 5);
      return (sTimeClean === timeClean || s.startTime?.startsWith(timeClean)) && s.status === 'AVAILABLE';
    });
  }
}
