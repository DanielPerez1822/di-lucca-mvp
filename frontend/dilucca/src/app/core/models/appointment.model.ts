export type AppointmentStatus = 'SCHEDULED' | 'CONFIRMED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';

export interface Appointment {
  id: string;
  patientId: string;
  dentistId: string;
  slotId: string;
  date?: string;
  appointmentDate?: string;
  startTime: string;
  endTime: string;
  status: AppointmentStatus;
  reason?: string;
  notes?: string;
  cancellationReason?: string;
  createdAt: string;
  updatedAt: string;
}

export interface BookAppointmentRequest {
  patientId: string;
  dentistId: string;
  slotId: string;
  reason?: string;
  notes?: string;
}

export interface RescheduleAppointmentRequest {
  newSlotId: string;
}

export interface CancelAppointmentRequest {
  cancellationReason?: string;
}
