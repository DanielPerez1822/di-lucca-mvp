import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth.guard';
import { adminGuard } from './core/guards/role.guard';
import { LoginComponent } from './features/auth/login/login.component';
import { ForgotPasswordComponent } from './features/auth/forgot-password/forgot-password.component';
import { ResetPasswordComponent } from './features/auth/reset-password/reset-password.component';
import { LayoutComponent } from './shared/components/layout/layout.component';
import { DashboardComponent } from './features/dashboard/dashboard.component';
import { PatientListComponent } from './features/patients/patient-list/patient-list.component';
import { ProcedureListComponent } from './features/procedures/procedure-list/procedure-list.component';
import { ScheduleManagementComponent } from './features/schedules/schedule-management/schedule-management.component';
import { AppointmentListComponent } from './features/appointments/appointment-list/appointment-list.component';
import { MedicalRecordListComponent } from './features/medical-records/medical-record-list/medical-record-list.component';
import { InvoiceListComponent } from './features/invoices/invoice-list/invoice-list.component';
import { CalendarViewComponent } from './features/calendar/calendar-view/calendar-view.component';
import { UserManagementComponent } from './features/users/user-management/user-management.component';
import { WelcomeComponent } from './features/welcome/welcome.component';

export const routes: Routes = [
  {
    path: '',
    component: WelcomeComponent,
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'forgot-password',
    component: ForgotPasswordComponent
  },
  {
    path: 'reset-password',
    component: ResetPasswordComponent
  },
  {
    path: '',
    component: LayoutComponent,
    canActivate: [authGuard],
    children: [
      {
        path: 'dashboard',
        component: DashboardComponent
      },
      {
        path: 'patients',
        component: PatientListComponent
      },
      {
        path: 'procedures',
        component: ProcedureListComponent
      },
      {
        path: 'calendar',
        component: CalendarViewComponent
      },
      {
        path: 'schedules',
        component: ScheduleManagementComponent
      },
      {
        path: 'appointments',
        component: CalendarViewComponent
      },
      {
        path: 'medical-records',
        component: MedicalRecordListComponent
      },
      {
        path: 'invoices',
        component: InvoiceListComponent
      },
      {
        path: 'user-management',
        component: UserManagementComponent,
        canActivate: [adminGuard]
      },
      {
        path: 'users',
        redirectTo: 'user-management',
        pathMatch: 'full'
      }
    ]
  },
  {
    path: '**',
    redirectTo: ''
  }
];
