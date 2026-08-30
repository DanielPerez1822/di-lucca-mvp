export type DocumentType = 'CC' | 'CE' | 'PA' | 'TI' | 'RC' | 'OTHER';

export interface Patient {
  id: string;
  firstName: string;
  lastName: string;
  documentType: DocumentType;
  documentNumber: string;
  phone: string;
  email?: string;
  dateOfBirth?: string;
  address?: string;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CreatePatientRequest {
  firstName: string;
  lastName: string;
  documentType: DocumentType;
  documentNumber: string;
  phone: string;
  email?: string;
  dateOfBirth?: string;
  address?: string;
}

export interface UpdatePatientRequest {
  firstName?: string;
  lastName?: string;
  phone?: string;
  email?: string;
  dateOfBirth?: string;
  address?: string;
}
