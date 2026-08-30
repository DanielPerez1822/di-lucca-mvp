export interface MedicalRecordProcedureItem {
  id: string;
  procedureId: string;
  procedureName: string;
  appliedPrice: number;
  toothNumber?: number;
  notes?: string;
}

export interface MedicalRecord {
  id: string;
  patientId: string;
  dentistId: string;
  appointmentId?: string;
  entryDate: string;
  diagnosis: string;
  notes?: string;
  totalAmount: number;
  items: MedicalRecordProcedureItem[];
  createdAt: string;
  updatedAt: string;
}

export interface CreateMedicalRecordItemRequest {
  procedureId: string;
  appliedPrice?: number;
  toothNumber?: number;
  notes?: string;
}

export interface CreateMedicalRecordRequest {
  patientId: string;
  dentistId: string;
  appointmentId?: string;
  diagnosis: string;
  notes?: string;
  items: CreateMedicalRecordItemRequest[];
}
