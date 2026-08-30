import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);

  return next(req).pipe(
    catchError(err => {
      if ((err.status === 401 || err.status === 403) && !req.url.includes('/auth/login')) {
        authService.logout();
      }
      const errorMsg = err.error?.message || err.statusText || 'Error en el servidor';
      return throwError(() => new Error(errorMsg));
    })
  );
};
