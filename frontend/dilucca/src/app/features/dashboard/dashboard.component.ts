import { Component, OnInit, computed, signal, AfterViewInit, ElementRef, ViewChild } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { AuthService } from '../../core/services/auth.service';
import { AppointmentService } from '../../core/services/appointment.service';
import { PatientService } from '../../core/services/patient.service';
import { InvoiceService } from '../../core/services/invoice.service';
import { Appointment } from '../../core/models/appointment.model';
import { Patient } from '../../core/models/patient.model';
import { Invoice } from '../../core/models/invoice.model';
import { forkJoin } from 'rxjs';

interface WeekDay {
  label: string;
  count: number;
}

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit, AfterViewInit {

  @ViewChild('chartCanvas') chartCanvas!: ElementRef<HTMLCanvasElement>;

  userFirstName = computed(() => {
    const u = this.authService.currentUser();
    return u ? u.firstName : 'Usuario';
  });

  isAdmin = computed(() => this.authService.hasRole(['ADMIN']));

  // State
  loading = signal(true);
  allAppointments = signal<Appointment[]>([]);
  allPatients = signal<Patient[]>([]);
  allInvoices = signal<Invoice[]>([]);

  // Date range filter
  rangeStart = '';
  rangeEnd = '';

  // KPI signals
  filteredAppointmentsCount = signal(0);
  totalPatients = signal(0);
  monthlyRevenue = signal(0);
  pendingInvoices = signal(0);

  // Weekly chart data
  weeklyData = signal<WeekDay[]>([]);

  // Month picker for revenue
  selectedMonth = '';

  constructor(
    private authService: AuthService,
    private appointmentService: AppointmentService,
    private patientService: PatientService,
    private invoiceService: InvoiceService
  ) {}

  ngOnInit(): void {
    // Set default date range: current month
    const now = new Date();
    const firstDay = new Date(now.getFullYear(), now.getMonth(), 1);
    const lastDay = new Date(now.getFullYear(), now.getMonth() + 1, 0);
    this.rangeStart = this.toDateString(firstDay);
    this.rangeEnd = this.toDateString(lastDay);
    this.selectedMonth = `${now.getFullYear()}-${String(now.getMonth() + 1).padStart(2, '0')}`;

    if (this.isAdmin()) {
      this.loadData();
    }
  }

  ngAfterViewInit(): void {
    // Chart drawn after data loads via drawChart()
  }

  private toDateString(d: Date): string {
    return d.toISOString().split('T')[0];
  }

  loadData(): void {
    this.loading.set(true);
    forkJoin({
      appointments: this.appointmentService.getAppointments(),
      patients: this.patientService.getPatients('', false),
      invoices: this.invoiceService.getInvoices()
    }).subscribe({
      next: ({ appointments, patients, invoices }) => {
        this.allAppointments.set(appointments);
        this.allPatients.set(patients);
        this.allInvoices.set(invoices);
        this.loading.set(false);
        this.computeKpis();
        setTimeout(() => this.drawChart(), 80);
      },
      error: () => {
        this.loading.set(false);
      }
    });
  }

  applyDateRange(): void {
    this.computeKpis();
    setTimeout(() => this.drawChart(), 80);
  }

  private computeKpis(): void {
    const start = this.rangeStart ? new Date(this.rangeStart + 'T00:00:00') : null;
    const end = this.rangeEnd ? new Date(this.rangeEnd + 'T23:59:59') : null;

    // Appointments in range
    const filtered = this.allAppointments().filter(a => {
      const d = new Date((a.appointmentDate ?? a.date ?? '') + 'T00:00:00');
      if (isNaN(d.getTime())) return false;
      if (start && d < start) return false;
      if (end && d > end) return false;
      return true;
    });
    this.filteredAppointmentsCount.set(filtered.length);

    // Total registered patients
    this.totalPatients.set(this.allPatients().length);

    // Monthly revenue from selectedMonth
    const [year, month] = this.selectedMonth ? this.selectedMonth.split('-').map(Number) : [new Date().getFullYear(), new Date().getMonth() + 1];
    const monthRevenue = this.allInvoices()
      .filter(inv => {
        const d = new Date(inv.issueDate);
        return d.getFullYear() === year && (d.getMonth() + 1) === month;
      })
      .reduce((sum, inv) => sum + (inv.paidAmount ?? 0), 0);
    this.monthlyRevenue.set(monthRevenue);

    // Pending invoices
    this.pendingInvoices.set(this.allInvoices().filter(i => i.status === 'PENDING' || i.status === 'PARTIALLY_PAID').length);

    // Weekly data (last 7 days)
    const days: WeekDay[] = [];
    for (let i = 6; i >= 0; i--) {
      const d = new Date();
      d.setDate(d.getDate() - i);
      const label = d.toLocaleDateString('es-CO', { weekday: 'short', day: 'numeric' });
      const dayStr = this.toDateString(d);
      const count = this.allAppointments().filter(a => {
        const ad = (a.appointmentDate ?? a.date ?? '').slice(0, 10);
        return ad === dayStr;
      }).length;
      days.push({ label, count });
    }
    this.weeklyData.set(days);
  }

  /** True when no appointments exist in the last 7 days */
  get noWeeklyActivity(): boolean {
    return this.weeklyData().every(d => d.count === 0);
  }

  private drawChart(): void {
    const canvas = this.chartCanvas?.nativeElement;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    const data = this.weeklyData();
    const max = Math.max(...data.map(d => d.count), 1);

    const W = canvas.offsetWidth || 600;
    const H = 220;
    canvas.width = W;
    canvas.height = H;

    ctx.clearRect(0, 0, W, H);

    const padL = 36, padR = 16, padT = 20, padB = 48;
    const chartW = W - padL - padR;
    const chartH = H - padT - padB;
    const barW = Math.floor(chartW / data.length * 0.55);
    const gap = chartW / data.length;

    // Grid lines
    const gridLines = 4;
    ctx.strokeStyle = '#e2e8f0';
    ctx.lineWidth = 1;
    ctx.font = '11px Arial';
    ctx.fillStyle = '#94a3b8';
    for (let i = 0; i <= gridLines; i++) {
      const y = padT + chartH - (chartH / gridLines) * i;
      ctx.beginPath();
      ctx.moveTo(padL, y);
      ctx.lineTo(padL + chartW, y);
      ctx.stroke();
      const val = Math.round((max / gridLines) * i);
      ctx.fillText(String(val), 0, y + 4);
    }

    // Bars with gradient
    data.forEach((d, idx) => {
      const x = padL + gap * idx + (gap - barW) / 2;
      const barH = d.count === 0 ? 2 : (d.count / max) * chartH;
      const y = padT + chartH - barH;

      const grad = ctx.createLinearGradient(x, y, x, padT + chartH);
      grad.addColorStop(0, '#1EA296');
      grad.addColorStop(1, '#12756C');
      ctx.fillStyle = grad;

      // Rounded top corners
      const r = 6;
      ctx.beginPath();
      ctx.moveTo(x + r, y);
      ctx.lineTo(x + barW - r, y);
      ctx.quadraticCurveTo(x + barW, y, x + barW, y + r);
      ctx.lineTo(x + barW, padT + chartH);
      ctx.lineTo(x, padT + chartH);
      ctx.lineTo(x, y + r);
      ctx.quadraticCurveTo(x, y, x + r, y);
      ctx.closePath();
      ctx.fill();

      // Count label on top
      if (d.count > 0) {
        ctx.fillStyle = '#12756C';
        ctx.font = 'bold 12px Arial';
        ctx.textAlign = 'center';
        ctx.fillText(String(d.count), x + barW / 2, y - 6);
      }

      // Day label
      ctx.fillStyle = '#64748b';
      ctx.font = '11px Arial';
      ctx.textAlign = 'center';
      ctx.fillText(d.label, x + barW / 2, H - 8);
    });
  }
}
