import { Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { AuthResponse, ForgotPasswordRequest, LoginRequest, ResetPasswordRequest } from '../models/auth.model';
import { User } from '../models/user.model';

@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly API_URL = '/api/auth';
  private readonly TOKEN_KEY = 'dilucca_access_token';
  private readonly REFRESH_TOKEN_KEY = 'dilucca_refresh_token';
  private readonly USER_KEY = 'dilucca_user';

  public currentUser = signal<User | null>(this.getStoredUser());

  constructor(
    private http: HttpClient,
    private router: Router
  ) {}

  login(credentials: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.API_URL}/login`, credentials).pipe(
      tap(response => this.handleAuthentication(response))
    );
  }

  requestPasswordReset(request: ForgotPasswordRequest): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/forgot-password`, request);
  }

  resetPassword(request: ResetPasswordRequest): Observable<void> {
    return this.http.post<void>(`${this.API_URL}/reset-password`, request);
  }

  logout(): void {
    if (this.isBrowser()) {
      const refreshToken = localStorage.getItem(this.REFRESH_TOKEN_KEY);
      if (refreshToken) {
        this.http.post(`${this.API_URL}/logout`, { refreshToken }).subscribe({
          error: () => {}
        });
      }
      localStorage.removeItem(this.TOKEN_KEY);
      localStorage.removeItem(this.REFRESH_TOKEN_KEY);
      localStorage.removeItem(this.USER_KEY);
    }
    this.currentUser.set(null);
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    if (!this.isBrowser()) return null;
    return localStorage.getItem(this.TOKEN_KEY);
  }

  isAuthenticated(): boolean {
    return !!this.getToken() && !!this.currentUser();
  }

  hasRole(allowedRoles: string[]): boolean {
    const user = this.currentUser();
    if (!user) return false;
    return user.roles.some(role => allowedRoles.includes(role));
  }

  private handleAuthentication(response: AuthResponse): void {
    if (this.isBrowser()) {
      localStorage.setItem(this.TOKEN_KEY, response.accessToken);
      localStorage.setItem(this.REFRESH_TOKEN_KEY, response.refreshToken);
      localStorage.setItem(this.USER_KEY, JSON.stringify(response.user));
    }
    this.currentUser.set(response.user);
  }

  private getStoredUser(): User | null {
    if (!this.isBrowser()) return null;
    const stored = localStorage.getItem(this.USER_KEY);
    if (!stored) return null;
    try {
      return JSON.parse(stored);
    } catch {
      return null;
    }
  }

  private isBrowser(): boolean {
    return typeof window !== 'undefined' && typeof localStorage !== 'undefined';
  }
}
