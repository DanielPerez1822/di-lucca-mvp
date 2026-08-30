import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Role, User } from '../models/user.model';

export interface CreateUserPayload {
  email: string;
  password: string;
  firstName: string;
  lastName: string;
  phone?: string;
  documentNumber?: string;
  roles: Role[];
}

export interface UpdateUserPayload {
  firstName: string;
  lastName: string;
  phone?: string;
  documentNumber?: string;
  roles: Role[];
  active?: boolean;
}

@Injectable({
  providedIn: 'root'
})
export class UserManagementService {
  private readonly apiUrl = 'http://localhost:9000/api/admin/users';

  constructor(private http: HttpClient) {}

  getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.apiUrl);
  }

  getUserById(id: string): Observable<User> {
    return this.http.get<User>(`${this.apiUrl}/${id}`);
  }

  createUser(payload: CreateUserPayload): Observable<User> {
    return this.http.post<User>(this.apiUrl, payload);
  }

  updateUser(id: string, payload: UpdateUserPayload): Observable<User> {
    return this.http.put<User>(`${this.apiUrl}/${id}`, payload);
  }

  deactivateUser(id: string): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
