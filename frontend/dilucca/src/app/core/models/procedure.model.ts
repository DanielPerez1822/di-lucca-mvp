export interface Procedure {
  id: string;
  name: string;
  description: string;
  price: number;
  duration?: number;
  durationInMinutes?: number;
  active: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CreateProcedureRequest {
  name: string;
  description: string;
  price: number;
  duration: number;
}

export interface UpdateProcedureRequest {
  name?: string;
  description?: string;
  price?: number;
  duration?: number;
}
