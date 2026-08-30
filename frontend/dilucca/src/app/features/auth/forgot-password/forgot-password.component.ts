import { Component, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-forgot-password',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, RouterLink],
  templateUrl: './forgot-password.component.html',
  styleUrl: './forgot-password.component.css'
})
export class ForgotPasswordComponent {

  forgotForm: FormGroup;
  loading = signal(false);
  successMessage = signal<string | null>(null);
  errorMessage = signal<string | null>(null);

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router
  ) {
    this.forgotForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]]
    });
  }

  isFieldInvalid(field: string): boolean {
    const control = this.forgotForm.get(field);
    return !!(control && control.invalid && (control.dirty || control.touched));
  }

  onSubmit(): void {
    if (this.forgotForm.invalid) return;

    this.loading.set(true);
    this.successMessage.set(null);
    this.errorMessage.set(null);

    const email = this.forgotForm.value.email;

    this.authService.requestPasswordReset({ email }).subscribe({
      next: () => {
        this.loading.set(false);
        this.successMessage.set('Código de 6 dígitos generado exitosamente. Redirigiendo...');
        setTimeout(() => {
          this.router.navigate(['/reset-password'], { queryParams: { email } });
        }, 1500);
      },
      error: (err) => {
        this.loading.set(false);
        this.errorMessage.set(err.message || 'No se pudo enviar el código. Verifica el correo.');
      }
    });
  }
}
