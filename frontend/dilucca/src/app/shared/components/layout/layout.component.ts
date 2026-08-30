import { Component, computed } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../../../core/services/auth.service';

@Component({
  selector: 'app-layout',
  standalone: true,
  imports: [CommonModule, RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './layout.component.html',
  styleUrl: './layout.component.css'
})
export class LayoutComponent {

  currentTime = new Date().toLocaleDateString('es-CO', { weekday: 'long', day: 'numeric', month: 'short' });

  userName = computed(() => {
    const u = this.authService.currentUser();
    return u ? `${u.firstName} ${u.lastName}` : 'Usuario';
  });

  userInitials = computed(() => {
    const u = this.authService.currentUser();
    if (!u) return 'US';
    return `${u.firstName.charAt(0)}${u.lastName.charAt(0)}`.toUpperCase();
  });

  userRole = computed(() => {
    const u = this.authService.currentUser();
    return u && u.roles && u.roles.length > 0 ? u.roles[0] : 'EMPLEADO';
  });

  isAdmin = computed(() => {
    return this.authService.hasRole(['ADMIN']);
  });

  constructor(private authService: AuthService) {}

  onLogout(): void {
    this.authService.logout();
  }
}
