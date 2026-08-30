export type Role = 'SECRETARY_ASSISTANT' | 'DENTIST' | 'ADMIN';

export interface User {
  id: string;
  email: string;
  firstName: string;
  lastName: string;
  phone?: string;
  documentNumber?: string;
  roles: Role[];
  active: boolean;
  createdAt: string;
  updatedAt: string;
}
