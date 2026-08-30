export type DayOfWeek = 'MONDAY' | 'TUESDAY' | 'WEDNESDAY' | 'THURSDAY' | 'FRIDAY' | 'SATURDAY' | 'SUNDAY';

export interface ScheduleDayConfig {
  dayOfWeek: DayOfWeek;
  startTime: string;
  endTime: string;
  slotDurationMinutes: number;
  active: boolean;
}

export interface DentistSchedule {
  id: string;
  dentistId: string;
  days: ScheduleDayConfig[];
  createdAt: string;
  updatedAt: string;
}

export interface SetDentistScheduleRequest {
  dentistId: string;
  days: ScheduleDayConfig[];
}

export interface GenerateSlotsRequest {
  dentistId: string;
  startDate: string;
  endDate: string;
}

export type SlotStatus = 'AVAILABLE' | 'BOOKED' | 'BLOCKED';

export interface AvailabilitySlot {
  id: string;
  dentistId: string;
  date: string;
  startTime: string;
  endTime: string;
  status: SlotStatus;
}
